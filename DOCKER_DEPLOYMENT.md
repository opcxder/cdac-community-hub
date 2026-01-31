# 🐳 CDAC Project - Docker Deployment Guide

## Prerequisites
- Docker Desktop installed and running
- At least 8GB RAM allocated to Docker
- Ports 3307-3310, 5173, 8080-8085, 8761 available

---

## Quick Start

### 1. **Configure Environment**
```bash
# Copy and edit .env file
cp .env .env.local
# Edit .env.local with your actual credentials
```

**Required Changes in `.env.local`:**
- `MYSQL_ROOT_PASSWORD` - Strong password for MySQL root
- `MYSQL_PASSWORD` - Password for cdac_user
- `JWT_SECRET` - Minimum 256-bit secret key
- `CLOUDINARY_FOOD_CLOUD_NAME` - Your food Cloudinary cloud name
- `CLOUDINARY_HOSTEL_CLOUD_NAME` - Your hostel Cloudinary cloud name

### 2. **Build All Images**
```bash
docker-compose build
```
⏱️ **First build takes 10-15 minutes** (downloads dependencies, builds JARs)

### 3. **Start the Stack**
```bash
docker-compose up -d
```

### 4. **Check Status**
```bash
docker-compose ps
```

All services should show "healthy" status after ~2-3 minutes.

---

## Access Points

| Service | URL | Description |
|---------|-----|-------------|
| **Eureka Dashboard** | http://localhost:8761 | Service registry |
| **API Gateway** | http://localhost:8080 | Main API endpoint |
| **Admin Frontend** | http://localhost:5173 | React admin dashboard |
| **Auth DB** | localhost:3307 | MySQL auth database |
| **Food DB** | localhost:3308 | MySQL food database |
| **Hostel DB** | localhost:3309 | MySQL hostel database |
| **Suggestion DB** | localhost:3310 | MySQL suggestion database |

---

## Useful Commands

### **View Logs**
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f auth-service
docker-compose logs -f api-gateway
docker-compose logs -f admin-frontend
```

### **Restart Service**
```bash
docker-compose restart auth-service
```

### **Stop All Services**
```bash
docker-compose down
```

### **Stop and Remove Volumes** (⚠️ Deletes all data!)
```bash
docker-compose down -v
```

### **Rebuild Specific Service**
```bash
docker-compose build auth-service
docker-compose up -d auth-service
```

---

## Troubleshooting

### **Service Won't Start**
```bash
# Check logs
docker-compose logs <service-name>

# Check if port is already in use
netstat -ano | findstr :<PORT>
```

### **Database Connection Issues**
```bash
# Connect to MySQL container
docker exec -it cdac-auth-db mysql -u cdac_user -p

# Check database exists
SHOW DATABASES;
```

### **Eureka Registration Issues**
1. Wait 2-3 minutes for services to register
2. Check Eureka dashboard: http://localhost:8761
3. Verify `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` in service logs

### **Frontend Can't Connect to Backend**
1. Verify API Gateway is running: http://localhost:8080/actuator/health
2. Check CORS configuration in API Gateway
3. Verify `VITE_API_BASE_URL` in frontend environment

---

## Development Workflow

### **Make Code Changes**

**Backend Service:**
```bash
# Rebuild and restart
docker-compose build <service-name>
docker-compose up -d <service-name>
```

**Frontend:**
```bash
# Rebuild and restart
docker-compose build admin-frontend
docker-compose up -d admin-frontend
```

### **Database Migrations**
```bash
# Connect to database
docker exec -it cdac-auth-db mysql -u cdac_user -p auth_db

# Run migrations manually or via Flyway/Liquibase
```

---

## Production Deployment

### **Build for Production**
```bash
# Use production compose file
docker-compose -f docker-compose.prod.yml build
docker-compose -f docker-compose.prod.yml up -d
```

### **Security Checklist**
- [ ] Change all default passwords in `.env`
- [ ] Use strong JWT secret (minimum 256 bits)
- [ ] Enable HTTPS/TLS
- [ ] Configure firewall rules
- [ ] Set up database backups
- [ ] Enable Docker logging
- [ ] Use secrets management (Docker Secrets, Vault)

---

## Monitoring

### **Health Checks**
```bash
# Check all services
curl http://localhost:8761/actuator/health
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
curl http://localhost:8085/actuator/health
```

### **Resource Usage**
```bash
docker stats
```

---

## Backup & Restore

### **Backup Databases**
```bash
# Backup auth database
docker exec cdac-auth-db mysqldump -u cdac_user -p auth_db > backup_auth.sql

# Backup all databases
docker exec cdac-auth-db mysqldump -u cdac_user -p --all-databases > backup_all.sql
```

### **Restore Database**
```bash
docker exec -i cdac-auth-db mysql -u cdac_user -p auth_db < backup_auth.sql
```

---

## Scaling (Optional)

```bash
# Scale food service to 3 instances
docker-compose up -d --scale food-service=3

# Load balancing handled by Eureka + API Gateway
```

---

## Clean Up

### **Remove Everything**
```bash
# Stop and remove containers, networks
docker-compose down

# Remove volumes (⚠️ deletes data)
docker-compose down -v

# Remove images
docker-compose down --rmi all
```

---

## Support

**Common Issues:**
- Port conflicts → Change ports in docker-compose.yml
- Out of memory → Increase Docker memory allocation
- Slow builds → Use Docker BuildKit: `DOCKER_BUILDKIT=1 docker-compose build`

**Logs Location:**
- Container logs: `docker-compose logs`
- Application logs: Inside containers at `/app/logs` (if configured)
