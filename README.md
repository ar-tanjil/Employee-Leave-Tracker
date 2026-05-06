# Employee Leave Tracker

A comprehensive full-stack web application for managing employee leave requests, approvals, and organizational leave policies with role-based access control and real-time notifications.

## 🏗️ Architecture Overview

The Employee Leave Tracker is a modern full-stack application built with a scalable microservices architecture:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │   Database      │
│   (Angular)     │◄──►│  (Spring Boot)  │◄──►│    (MySQL)      │
│   Port: 4200    │    │   Port: 8099    │    │   Port: 3306    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

- **Frontend**: Angular 21 application with responsive UI and real-time updates
- **Backend**: Spring Boot REST API with JWT authentication and role-based access
- **Database**: MySQL database with comprehensive leave management schema
- **Containerization**: Docker support with nginx reverse proxy

## 🛠 Tech Stack

### Frontend
- **Framework**: Angular 21.2.0
- **Language**: TypeScript 5.9.2
- **Styling**: TailwindCSS 4.1.12
- **UI Components**: Angular CDK, SweetAlert2, ngx-spinner
- **Build Tool**: Angular CLI 21.2.6

### Backend
- **Framework**: Spring Boot 4.0.6
- **Language**: Java 25
- **Security**: Spring Security with JWT
- **Database**: Spring Data JPA with MySQL
- **Documentation**: Swagger/OpenAPI 3.0
- **Build Tool**: Maven

### DevOps & Infrastructure
- **Containerization**: Docker & Docker Compose
- **Web Server**: Nginx (production)
- **Database**: MySQL 8.0
- **Version Control**: Git

## 📁 Project Structure

```
Employee-Leave-Tracker-main/
├── backend/                    # Spring Boot REST API
│   ├── src/
│   │   ├── main/java/          # Java source code
│   │   └── main/resources/     # Configuration files
│   ├── Dockerfile              # Backend container configuration
│   ├── pom.xml                 # Maven dependencies
│   └── README.md               # Backend documentation
├── frontend/                   # Angular application
│   ├── src/
│   │   ├── app/                # Angular application code
│   │   └── environments/       # Environment configs
│   ├── Dockerfile              # Frontend container configuration
│   ├── package.json            # npm dependencies
│   ├── nginx.conf              # Nginx configuration
│   └── README.md               # Frontend documentation
├── docker-compose.yml          # Full stack deployment
├── docker-compose.dev.yml      # Development deployment
└── README.md                   # This file
```

## 🚀 Quick Start

### Prerequisites

Ensure you have the following installed:
- **Java 25** or higher
- **Node.js 18** or higher
- **MySQL 8.0** or Docker
- **Maven 3.6** or higher
- **Git**

### Option 1: Docker Deployment (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Employee-Leave-Tracker-main
   ```

2. **Start all services**
   ```bash
   docker-compose up -d
   ```

3. **Access the application**
   - Frontend: http://localhost:80
   - Backend API: http://localhost:8099/api
   - API Documentation: http://localhost:8099/api/swagger-ui.html

### Option 2: Local Development

1. **Clone and setup**
   ```bash
   git clone <repository-url>
   cd Employee-Leave-Tracker-main
   ```

2. **Setup Backend**
   ```bash
   cd backend
   # Create MySQL database 'employee_leave_tracker'
   ./mvnw spring-boot:run
   ```

3. **Setup Frontend** (in a new terminal)
   ```bash
   cd frontend
   npm install
   npm start
   ```

4. **Access the application**
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8099/api

## 🏃‍♂️ How to Run Full Project Locally

### Development Environment

1. **Database Setup**
   ```sql
   CREATE DATABASE employee_leave_tracker;
   CREATE USER 'employee_user'@'localhost' IDENTIFIED BY 'employee_pass';
   GRANT ALL PRIVILEGES ON employee_leave_tracker.* TO 'employee_user'@'localhost';
   ```

2. **Backend Setup**
   ```bash
   cd backend
   ./mvnw clean install
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

3. **Frontend Setup**
   ```bash
   cd frontend
   npm install
   npm start
   ```

### Production Environment

1. **Using Docker Compose**
   ```bash
   docker-compose up -d --build
   ```

2. **Manual Deployment**
   ```bash
   # Backend
   cd backend
   ./mvnw clean package -DskipTests
   java -jar target/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   
   # Frontend
   cd frontend
   npm run build --configuration production
   # Deploy dist/frontend/ to web server
   ```

## 🔧 Configuration

### Environment Variables

#### Backend
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/employee_leave_tracker
SPRING_DATASOURCE_USERNAME=employee_user
SPRING_DATASOURCE_PASSWORD=employee_pass
JWT_SECRET=your-jwt-secret-key
```

#### Frontend
```bash
# In src/environments/environment.ts
apiBaseUrl: "api"
apiVersion: "v1"
```

### Database Configuration

The application automatically initializes the database with:
- Users and roles
- Permissions and access control
- Sample data for testing

## 📚 API Documentation

- **Swagger UI**: http://localhost:8099/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8099/api/v3/api-docs


## 🚀 Deployment

### Docker Deployment
```bash
# Production
docker-compose up -d

# Development
docker-compose -f docker-compose.dev.yml up -d
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

