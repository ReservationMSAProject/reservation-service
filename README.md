
# 🎟️ Reservation Service

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.8-blue.svg)](https://gradle.org)

**Reservation Service**는 콘서트 및 공연 예약을 관리하는 백엔드 API 서버입니다. 사용자는 콘서트 정보를 조회하고, 좌석을 예약, 확인, 취소할 수 있습니다.

## ✨ 주요 기능

- **🎤 콘서트 관리**: 콘서트 정보 등록, 조회, 수정, 삭제
- **🪑 좌석 관리**: 공연별 좌석 정보 관리
- **🎫 예약 관리**: 좌석 예약, 예약 확인, 예약 취소
- **🔐 인증/인가**: AOP를 활용한 사용자 인증 처리
- **📢 이벤트 기반 아키텍처**: Kafka를 이용한 예약 이벤트 발행

## 🛠️ 기술 스택 및 의존성

- **Language**: `Java 17`
- **Framework**: `Spring Boot 3.5.3`
- **Database**: `PostgreSQL` (운영), `H2` (테스트)
- **Build Tool**: `Gradle`
- **Key Dependencies**:
  - `Spring Web`: RESTful API 개발
  - `Spring Data JPA`: 데이터베이스 연동 및 ORM
  - `Spring Security`: 인증 및 인가
  - `Spring for Apache Kafka`: 메시지 큐 연동
  - `Spring Cloud Config`: 외부 설정 관리
  - `Spring Cloud Eureka Client`: 서비스 디스커버리
  - `Lombok`: 보일러플레이트 코드 제거
  - `PostgreSQL Driver`: PostgreSQL 데이터베이스 드라이버

## 📂 프로젝트 구조

```
src
└── main
    ├── java
    │   └── com
    │       └── reservation
    │           └── reserve
    │               ├── ReservationServiceApplication.java  # 애플리케이션 시작점
    │               ├── aop                 # AOP 기반 인증 처리
    │               ├── config              # Jpa, Kafka 등 설정
    │               ├── event               # Kafka 이벤트 핸들링
    │               ├── filter              # 사용자 컨텍스트 필터
    │               ├── reserve             # 핵심 비즈니스 로직
    │               │   ├── controller      # API 엔드포인트
    │               │   ├── domain          # JPA 엔티티
    │               │   ├── dto             # 데이터 전송 객체
    │               │   ├── repository      # 데이터 접근 계층
    │               │   └── service         # 비즈니스 서비스
    │               └── security            # Spring Security 설정
    └── resources
        ├── application.yaml    # 애플리케이션 설정 파일
        └── ...
```

## 📖 API 문서

API 문서는 Postman이나 Swagger를 통해 제공될 수 있습니다. 주요 엔드포인트는 다음과 같습니다.

### 인증

- 대부분의 API는 요청 헤더에 `X-USER-ID`를 포함하여 사용자 인증을 수행합니다.
- 예: `GET /api/concerts`
- Header: `X-USER-ID: 1`

### 콘서트 (Concert)

- `GET /api/concerts`: 모든 콘서트 목록 조회
- `GET /api/concerts/{concertId}`: 특정 콘서트 상세 정보 조회
- `POST /api/concerts`: 새 콘서트 등록 (관리자)

**요청 예시 (`POST /api/concerts`)**
```json
{
  "name": "Awesome Concert",
  "description": "The best concert ever",
  "concertDate": "2025-12-25T20:00:00",
  "venueName": "Grand Hall"
}
```

**응답 예시 (`GET /api/concerts/{concertId}`)**
```json
{
  "id": 1,
  "name": "Awesome Concert",
  "concertDate": "2025-12-25T20:00:00",
  "venueName": "Grand Hall",
  "seats": [
    { "id": 1, "seatNumber": "A1", "status": "AVAILABLE" },
    { "id": 2, "seatNumber": "A2", "status": "RESERVED" }
  ]
}
```

### 예약 (Reservation)

- `POST /api/reservations`: 좌석 예약
- `POST /api/reservations/confirm`: 예약 확정
- `POST /api/reservations/cancel`: 예약 취소

**요청 예시 (`POST /api/reservations`)**
```json
{
  "concertId": 1,
  "seatId": 1
}
```

**응답 예시**
```json
{
  "reservationId": 101,
  "status": "PENDING",
  "message": "예약이 접수되었습니다. 10분 내에 확정해주세요."
}
```

## 🗄️ 데이터베이스 스키마

주요 엔티티 간의 관계는 다음과 같습니다.

- **Venue (1) : (N) Concert**: 하나의 공연장은 여러 콘서트를 가질 수 있습니다.
- **Concert (1) : (N) Seat**: 하나의 콘서트는 여러 좌석을 가질 수 있습니다.
- **Concert (1) : (N) Reservation**: 하나의 콘서트에 여러 예약이 발생할 수 있습니다.
- **Seat (1) : (1) Reservation**: 하나의 좌석은 하나의 예약만 가질 수 있습니다. (특정 시점)


## ⚙️ 환경 변수 설정

`application.yaml` 파일에 다음 설정을 추가해야 합니다.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/reservation_db
    username: your_username
    password: your_password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  kafka:
    producer:
      bootstrap-servers: localhost:9092
    consumer:
      bootstrap-servers: localhost:9092
      group-id: reservation-group

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```


## 📜 라이선스

이 프로젝트는 개인프로젝트 입니다.
