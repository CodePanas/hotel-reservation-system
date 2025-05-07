# Hotel Reservation System

This is a simple Hotel Reservation System built with **Java 21**, **Spring Boot**, and **MongoDB**. It is designed to demonstrate system design principles, RESTful API architecture, and object-oriented modeling as part of a technical interview preparation.

## 📚 Features

- Object modeling of core entities: `Customer`, `Suite`, `Reservation`
- RESTful CRUD API for managing hotel reservations
- MongoDB as the database for persisting data
- Project structured using best practices (controller, service, repository layers)
- Ready to import and run in IntelliJ IDEA

## 🧱 Tech Stack

- Java 21
- Spring Boot 3
- Spring Data MongoDB
- Gradle
- MongoDB

## 📁 Project Structure
```
hotel-reservation-system/
├── src/
│   └── main/
│       ├── java/com/hotelreservation/
│       │   ├── controller/      # Handles REST API endpoints
│       │   ├── model/           # Contains domain models/entities
│       │   ├── repository/      # MongoDB data access layer
│       │   └── service/         # Business logic and service layer
│       └── resources/
│           └── application.properties  # App configuration
├── build.gradle
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- Java 21 installed
- Gradle installed (or use the Gradle wrapper)
- MongoDB running locally on default port (27017)

### 🛠️ Build & Run

1. Clone the repository:
   git clone https://github.com/CodePanas/hotel-reservation-system.git
   cd hotel-reservation-system

2. Build the project:
   `./gradlew build`

3. Run the application:
   `./gradlew bootRun`

4. Access the API:
   The application will start on http://localhost:8080

## 📬 API Endpoints (To Be Implemented)

| Method | Endpoint        | Description               |
|--------|-----------------|---------------------------|
| GET    | /reservations   | List all reservations     |
| POST   | /reservations   | Create a new reservation  |
| GET    | /customers      | List all customers        |
| POST   | /customers      | Register a new customer   |
| GET    | /suites         | List available suites     |
| POST   | /suites         | Add a new suite           |

## 🧠 Design Notes

- Models are designed using Java classes and annotated for MongoDB.
- SOLID principles were considered in the separation of layers.
- The `Reservation` model uses references to both `Customer` and `Suite`.
- The system is extensible and testable.

## 📄 License

This project is for educational and demonstration purposes only. No license applied.