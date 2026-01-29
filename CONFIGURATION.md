# Configuration Setup Guide

## Important Files to Configure

Before running the project, you need to create `application.properties` files for each service from the provided templates.

### Steps:

1. **Copy template files:**
   ```bash
   # For each service, copy the template
   cp application.properties.template application.properties
   ```

2. **Update the following values:**
   - Database username and password
   - JWT secret (generate using: `openssl rand -base64 32`)
   - API keys (if using Cloudinary or other services)

3. **Keep JWT secret consistent:**
   - Use the **same JWT secret** across all services
   - This is critical for token validation

### Services requiring configuration:
- auth-service
- admin-service
- food-service
- hostel-service
- suggestion-service

### Example JWT Secret Generation:
```bash
openssl rand -base64 32
```

Copy the output and use it as `jwt.secret` in all service configurations.

## Security Note
**Never commit `application.properties` files to Git!**
They are already excluded in `.gitignore`.
