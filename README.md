# CDAC Project - Campus Services Platform

A microservices-based platform for managing campus food places, hostels, and student suggestions.

## 🏗️ Architecture

- **Eureka Server** (8761) - Service discovery
- **API Gateway** (8080) - Single entry point
- **Auth Service** (8081) - Authentication & user management
- **Admin Service** (8101) - Admin dashboard & moderation
- **Food Service** (8086) - Food places management
- **Hostel Service** (8092) - Hostel management
- **Suggestion Service** (8096) - Student suggestions

## 🚀 Quick Start

### Prerequisites
- Java 21
- MySQL 8.x
- Maven 3.x

### Database Setup
Create the following databases:
```sql
CREATE DATABASE auth_db;
CREATE DATABASE food_db;
CREATE DATABASE hostel_db;
CREATE DATABASE suggestion_db;
```

### Configuration
1. Copy `application.properties.template` to `application.properties` in each service
2. Update database credentials and API keys
3. Set JWT secret (use same secret across all services)

### Running Services

**Start in this order:**
```bash
# 1. Eureka Server
cd backend/eureka-server
mvn spring-boot:run

# 2. API Gateway
cd backend/api-gateway
mvn spring-boot:run

# 3. Auth Service
cd backend/auth-service
mvn spring-boot:run

# 4. Other Services (any order)
cd backend/admin-service
mvn spring-boot:run

cd backend/food-service
mvn spring-boot:run

cd backend/hostel-service
mvn spring-boot:run

cd backend/suggestion-service
mvn spring-boot:run
```

## 🔑 Default Admin Credentials
- Email: `admin@cdac.in`
- Password: `adminpassword`

## 📡 API Endpoints

### Via Gateway (Port 8080)
- Auth: `http://localhost:8080/api/auth/*`
- Admin: `http://localhost:8080/api/admin/*`
- Food: `http://localhost:8080/api/food/*`
- Hostel: `http://localhost:8080/api/hostel/*`
- Suggestions: `http://localhost:8080/api/suggestions/*`

### Direct Access
- Eureka Dashboard: `http://localhost:8761`
- Auth Service: `http://localhost:8081/api/auth/*`
- Admin Service: `http://localhost:8101/api/admin/*`

## 🔧 Development

All services run in **dev mode** by default for local development.

### Testing
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@cdac.in","password":"adminpassword"}'

# Get Dashboard (use token from login)
curl -X GET http://localhost:8080/api/admin/dashboard \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 📝 Notes
- JWT tokens expire after 24 hours
- All services must use the same JWT secret
- Services communicate via Eureka service discovery
- Application properties files are gitignored (contain sensitive data)

## 🛠️ Tech Stack
- Spring Boot 4.0
- Spring Cloud Gateway
- Spring Security
- MySQL
- Eureka (Netflix OSS)
- JWT Authentication
