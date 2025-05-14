package com.deb.spring_boot_grpc_bidirectional_streaming_server.defense.service;

import com.deb.spring_boot_grpc_bidirectional_streaming_server.defense.DefenseBiDiServiceGrpc;
import com.deb.spring_boot_grpc_bidirectional_streaming_server.defense.DefenseMessage;
import io.grpc.stub.StreamObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@GrpcService
public class DefenseBiDiServiceImpl extends DefenseBiDiServiceGrpc.DefenseBiDiServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(DefenseBiDiServiceImpl.class);

    @Override
    public StreamObserver<DefenseMessage> exchangeData(StreamObserver<DefenseMessage> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(DefenseMessage message) {
                log.info("📡 Received from [{}]: {}", message.getUnitId(), message.getPayload());

                String commandResponse = analyzeTelemetry(message.getPayload());

                DefenseMessage response = DefenseMessage.newBuilder()
                        .setUnitId("GCS")
                        .setType("COMMAND")
                        .setPayload(commandResponse)
                        .setTimestamp(System.currentTimeMillis())
                        .build();

                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                log.error("❌ Stream error: ", t);
            }

            @Override
            public void onCompleted() {
                log.info("✅ Stream completed by client.");
                responseObserver.onCompleted();
            }

            private String analyzeTelemetry(String payload) {
                int battery = extract(payload, "Battery:\\s*(\\d+)%");
                int altitude = extract(payload, "Altitude:\\s*(\\d+)m");
                int speed = extract(payload, "Speed:\\s*(\\d+)km/h");

                if (battery > 0 && battery < 25) {
                    return "Battery critical (" + battery + "%). Return to base immediately.";
                } else if (altitude > 5000) {
                    return "Altitude too high (" + altitude + "m). Descend to 4500m.";
                } else if (speed > 250) {
                    return "Speed too high (" + speed + "km/h). Reduce to 200km/h.";
                } else if (battery >= 90 && altitude < 1000) {
                    return "All systems optimal. Ascend to mission altitude (1500m).";
                } else {
                    return "Telemetry acknowledged. Maintain current trajectory.";
                }
            }

            private int extract(String payload, String regex) {
                try {
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(payload);
                    if (matcher.find()) {
                        return Integer.parseInt(matcher.group(1));
                    }
                } catch (Exception e) {
                    log.warn("⚠️ Parsing failed for regex [{}]: {}", regex, e.getMessage());
                }
                return -1; // Invalid or not found
            }
        };
    }
}
