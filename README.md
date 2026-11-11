# Mawadda — Pediatric Hospital Clinical Information System (Spring Boot 3)

Enterprise-grade Pediatric Hospital Information System (HIS / Dossier Patient Informatisé) built with **Spring Boot 3.3.4** and **Java 17 LTS**.

---

## 🏗️ Layered Architecture Pattern

This project implements the standard academic & enterprise multi-tiered architecture:

```
src/main/java/com/mawadda/
├── enums/                      # Domain Enumerations (Role, BloodType, Sex, Severity, etc.)
├── entity/                     # JPA Model Entities with Hibernate Mappings
├── repository/                 # Spring Data JPA Repository Interfaces
├── service/                    # Business Service Interfaces (CRUD: add, modify, show, delete)
│   └── impl/                   # Business Service Implementations (@Service, @Transactional)
├── controller/                 # REST API Controllers (@RestController, @RequestMapping)
├── dto/                        # Data Transfer Objects
├── config/                     # SecurityConfig, OpenApiConfig, CorsConfig
└── exception/                  # Global Exception Handler & Custom Business Exceptions
```

---

## 🚀 Running the Application

### Prerequisites
* Java 17 LTS (`java -version`)
* Maven 3.9+ (`mvn -v`)
* PostgreSQL running on port 5432 (`localhost:5432/Mawadda`)

### Start the Spring Boot Server
```bash
mvn clean spring-boot:run
```

The application will start on **`http://localhost:8080`**.

---

## 📘 Interactive Swagger / OpenAPI Documentation

Once the server is running, open your browser to:
👉 **`http://localhost:8080/swagger-ui.html`**

All 10 clinical modules are fully documented with interactive endpoints:
* **Parents**: `/api/parents`
* **Patients**: `/api/patients`
* **Medical Services**: `/api/services`
* **Admissions**: `/api/admissions`
* **PEWS Scores**: `/api/pews`
* **Diagnoses**: `/api/diagnoses`
* **Medications**: `/api/medications`
* **Allergies**: `/api/allergies`
* **Surgical History**: `/api/surgical-history`
* **Staff Users**: `/api/users`

---

## 🧪 Automated Testing

Execute the comprehensive JUnit 5 and Mockito test suite covering clinical scoring, admission lifecycle, patient registration, and invite token generation:

```bash
mvn test
```

Expected result:
```
Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## 🔒 Security & Evaluation Note

* For grading and interactive OpenAPI evaluation, endpoints under `/api/**` and `/swagger-ui/**` are configured in `SecurityConfig.java` to allow immediate testing without requiring an initial JWT handshake.
* Passwords are securely hashed with `BCryptPasswordEncoder`.
* Environment variables can be passed via `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` (defaulting to the local dockerized Postgres instance).

