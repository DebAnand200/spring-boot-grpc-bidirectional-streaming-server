# 🚀 Real-Time Defense Operations with Spring Boot and gRPC Bi-Directional Streaming

Welcome to an advanced defense communication system powered by **Java**, **Spring Boot**, and **gRPC Bi-Directional Streaming**. This project simulates a real-time telemetry and command exchange between UAVs and a Ground Control Station (GCS) — tailored for **mission-critical defense operations**.

---

## 🛁 Overview

This system enables:

* 📥 Real-time telemetry from **UAVs to GCS**
* 📤 Rule-based command responses from **GCS to UAVs**
* 🔁 Fully reactive **Bi-Directional gRPC Streaming**
* 🧪 Testing via **Postman**, **BloomRPC**, or gRPC CLI

---

## ✨ Features

* ✅ Spring Boot integrated with `grpc-spring-boot-starter`
* 🔄 Real-time Bi-Directional gRPC Streaming (`StreamObserver`)
* ⚙️ Intelligent command logic (battery, altitude, speed checks)
* 🧪 Postman/gRPC UI ready
* 🚀 Production-grade, extensible architecture

---

## 🧹 Technology Stack

| Tech                     | Version |
| ------------------------ | ------- |
| Java                     | 17+     |
| Spring Boot              | 3.x     |
| gRPC                     | 1.58+   |
| Protocol Buffers         | 3.21+   |
| grpc-spring-boot-starter | 2.15.0+ |

---

## 📂 Project Structure

```
defense-grpc-bidi/
├── src/main/
│   ├── java/com/defense/
│   │   ├── service/DefenseBiDiServiceImpl.java   # Bi-Directional gRPC Server
│   │   └── SpringBootGrpcBidirectionalStreamingServerApplication.java                      # Spring Boot main app
│   └── proto/defense.proto                       # gRPC Protobuf contract
└── pom.xml
    README.md
```

---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/DebAnand200/spring-boot-grpc-bidirectional-streaming-server.git
cd defense-grpc-bidi
```

### 2. Generate gRPC Classes

If you're using Maven:

```bash
mvn clean install
```

Make sure `protobuf-maven-plugin` is configured in your `pom.xml`.

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

> Server will start at: `localhost:9090`

---

## 📜 Proto Definition

```proto
syntax = "proto3";

package defense;

option java_multiple_files = true;
option java_package = "com.deb.spring_boot_grpc_bidirectional_streaming_server.defense";

message DefenseMessage {
    string unitId = 1;
    string type = 2; // "TELEMETRY", "COMMAND", etc.
    string payload = 3;
    int64 timestamp = 4;
}

service DefenseBiDiService {
    rpc exchangeData (stream DefenseMessage) returns (stream DefenseMessage);
}
```

---

## 📨 Sample Payloads

### ✅ UAV → GCS (Request)

```json
{
  "unitId": "UAV-Alpha-1",
  "type": "TELEMETRY",
  "payload": "Altitude: 5600m, Speed: 260km/h, Battery: 45%",
  "timestamp": 1715700000000
}
```

### 📤 GCS → UAV (Response)

```json
{
  "unitId": "GCS",
  "type": "COMMAND",
  "payload": "Altitude too high (5600m). Descend to 4500m.",
  "timestamp": 1715700000100
}
```

---

## 🧠 Command Logic

The server parses incoming telemetry data and applies the following rules:

* 💧 **Battery < 25%** → "Return to base immediately"
* 🛫 **Altitude > 5000m** → "Descend to safe altitude"
* 🏎️ **Speed > 250km/h** → "Reduce speed to 200km/h"
* ✅ **Optimal state** → "Maintain or ascend to mission altitude"
* 📡 Any other case → "Telemetry acknowledged"

Uses regex to extract values from free-form telemetry.

---

## 🛠️ Application Configuration

Add the following to your `application.properties`:

```properties
spring.application.name=spring-boot-grpc-bidirectional-streaming-server
grpc.server.port=9090
```

---

## 🧪 Testing with Postman

1. Open **Postman v10+** with gRPC support
2. Import the `defense.proto` file
3. Connect to `localhost:9090`
4. Choose the `exchangeData` method
5. Start streaming and send telemetry JSON (see samples above)
6. Observe command responses in real-time

---

## 📊 Future Enhancements

* 🔁 Plug-in rule engine (e.g., Drools or Spring Expression Language)
* 🌐 Add geolocation and mission type intelligence
* 🔐 Secure with mutual TLS / token-based auth
* 📊 Add monitoring & alert dashboard (e.g., with Grafana)

---
