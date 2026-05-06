# Employee Leave Tracker - Backend API

A comprehensive Spring Boot backend service for managing employee leave applications, approvals, and tracking with role-based access control and JWT authentication.

## 📋 Project Overview

The Employee Leave Tracker backend provides a robust REST API for managing employee leave requests, approvals, and organizational leave policies. It features role-based permissions, JWT authentication, and comprehensive leave management workflows with MySQL database integration.

## 🛠 Tech Stack

- **Framework**: Spring Boot 4.0.6
- **Language**: Java 25
- **Database**: MySQL 8.0
- **Authentication**: JWT (JSON Web Tokens)
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Documentation**: Swagger/OpenAPI 3.0
- **Security**: Spring Security
- **Validation**: Jakarta Bean Validation
- **Mapping**: MapStruct
- **Utilities**: Lombok
- **Containerization**: Docker

## 📁 Folder Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/employee_leave_tracker/backend/
│   │   │   ├── BackendApplication.java          # Main application class
│   │   │   ├── config/                          # Configuration classes
│   │   │   ├── constant/                        # Application constants
│   │   │   ├── controller/                      # REST API controllers
│   │   │   │   ├── AuthController.java          # Authentication endpoints
│   │   │   │   ├── EmployeeController.java      # Employee management
│   │   │   │   ├── LeaveController.java         # Leave management
│   │   │   │   └── UserController.java          # User management
│   │   │   ├── dto/                             # Data Transfer Objects
│   │   │   ├── exception/                       # Custom exception handlers
│   │   │   ├── mapper/                          # Entity-DTO mappers
│   │   │   ├── model/                           # JPA entities
│   │   │   ├── repository/                      # Data access layer
│   │   │   ├── security/                        # Security configurations
│   │   │   ├── service/                         # Service interfaces
│   │   │   ├── serviceImpl/                     # Service implementations
│   │   │   └── util/                            # Utility classes
│   │   └── resources/
│   │       ├── application.yaml                 # Base configuration
│   │       ├── application-dev.yaml             # Development profile
│   │       ├── application-docker.yaml          # Docker profile
│   │       └── data.sql                         # Initial data setup
│   └── test/                                    # Test classes
├── .mvn/                                        # Maven wrapper
├── mvnw, mvnw.cmd                              # Maven wrapper scripts
├── pom.xml                                      # Maven configuration
└── Dockerfile                                   # Docker build configuration
```

## 🚀 Setup & Installation

### Prerequisites

- Java 25 or higher
- Maven 3.6+ or use the provided Maven wrapper
- MySQL 8.0+ or Docker
- Git

### Installation Steps

#### Option 1: Docker Setup (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Employee-Leave-Tracker-main
   ```

2. **Start all services with Docker Compose**
   ```bash
   docker-compose up -d
   ```

3. **Verify services are running**
   ```bash
   docker-compose ps
   ```

#### Option 2: Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Employee-Leave-Tracker-main/backend
   ```

2. **Database Setup**
   - **Database Creation** (if needed):
     ```sql
     CREATE DATABASE employee_leave_tracker;
     ```
   - **Hibernate Auto-Configuration**: The application automatically creates and manages the database schema
   - **Prerequisites**: Just ensure MySQL is running and accessible
   - **Automatic Setup**: 
     - Tables are created automatically on first startup
     - Initial data is seeded from `data.sql`
     - Schema is updated automatically with `ddl-auto: update`

3. **Build the application**
   ```bash
   # Using Maven wrapper
   ./mvnw clean install
   
   # Or using system Maven
   mvn clean install
   ```

4. **Run the application**
   ```bash
   # Using Maven wrapper
   ./mvnw spring-boot:run
   
   # Or using system Maven
   mvn spring-boot:run
   ```

## ⚙️ Environment Variables & Configuration

The application uses YAML configuration with profile-specific settings:

### Database Configuration
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/employee_leave_tracker?useSSL=false&serverTimezone=UTC
    username: employee_user
    password: employee_pass
```

### JWT Configuration
```yaml
jwt:
  secret: your-secret-key-here
  expiration-ms: 86400000  # 24 hours
```

### Server Configuration
```yaml
server:
  port: 8099
  servlet:
    context-path: /api
```

### Environment Variables
You can override configuration using environment variables:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `SERVER_PORT`
- `SPRING_PROFILES_ACTIVE`

### Configuration Profiles
- **dev**: Development with local MySQL
- **docker**: Docker environment with container MySQL
- **prod**: Production settings (create as needed)

## 🏃‍♂️ How to Run

### Docker (Recommended)
```bash
# Production setup with frontend
docker-compose up -d

# Development setup (backend + MySQL only)
docker-compose -f docker-compose.dev.yml up -d

# View logs
docker-compose logs -f backend
```

### Local Development
```bash
# Run with dev profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Run with specific profile
java -jar target/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### Build JAR Only
```bash
# Build the JAR
./mvnw clean package -DskipTests

# Run the JAR
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## 🌐 API Base URL

- **Base URL**: `http://localhost:8099/api`
- **API Version**: v1
- **Full API Base**: `http://localhost:8099/api/v1`
- **Swagger UI**: `http://localhost:8099/api/swagger-ui.html`


## 🚨 Error Handling

The API uses standardized error responses:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/leaves",
  "errors": [
    {
      "field": "startDate",
      "message": "Start date cannot be in the past"
    }
  ]
}
```

### Common HTTP Status Codes
- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Validation errors or malformed request
- `401 Unauthorized` - Authentication required or invalid token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource conflict (e.g., duplicate entry)
- `500 Internal Server Error` - Server error

## 🚀 Deployment Notes

### Docker Deployment
```bash
# Build and start all services
docker-compose up -d --build

# Scale backend if needed
docker-compose up -d --scale backend=2

# Stop services
docker-compose down
```

### Production Considerations

1. **Security**
   - Change default JWT secret in production
   - Use environment variables for sensitive configuration
   - Enable HTTPS in production
   - Configure proper CORS settings

2. **Database**
   - Use `ddl-auto=validate` in production
   - Set up database connection pooling
   - Configure database backups
   - Use read replicas for scaling

3. **Monitoring**
   - Enable application monitoring
   - Set up log aggregation
   - Configure health checks
   - Monitor resource usage

4. **Environment Variables for Production**
   ```bash
   SPRING_PROFILES_ACTIVE=prod
   SPRING_DATASOURCE_URL=jdbc:mysql://prod-db:3306/employee_leave_tracker
   SPRING_DATASOURCE_USERNAME=app_user
   SPRING_DATASOURCE_PASSWORD=secure_password
   JWT_SECRET=your-very-secure-jwt-secret-key
   ```

### Health Checks
- **Application Health**: `GET /api/actuator/health`
- **Database Health**: `GET /api/actuator/health/db`

## 🔧 Development Tools

### Database Access
- **H2 Console** (dev only): `http://localhost:8099/api/public/h2-console`
- **MySQL**: Connect via your preferred MySQL client

### API Documentation
- **Swagger UI**: `http://localhost:8099/api/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8099/api/v3/api-docs`

### Logging
Configure logging levels in profile-specific YAML:
```yaml
logging:
  level:
    root: INFO
    com.employee_leave_tracker: DEBUG
    org.hibernate.SQL: DEBUG
```

## 🐳 Docker Commands

```bash
# Build backend image
docker build -t employee-leave-backend ./backend

# Run backend container
docker run -d \
  --name backend \
  -p 8099:8099 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/employee_leave_tracker \
  employee-leave-backend

# View container logs
docker logs -f backend

# Execute commands in container
docker exec -it backend bash
```

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
