<h1 align="center">AirClone</h1>
<h4 align="center">Backend system provides restful API for web.</h4>

SonarQube:

Docker hub:
## Introduction
Developed a full-stack web application inspired by Airbnb using Spring Boot backend with Angular frontend.

---
## Tech Stack

![Static Badge](https://img.shields.io/badge/java_17-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Static Badge](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Static Badge](https://img.shields.io/badge/Postgresql-%234169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Static Badge](https://img.shields.io/badge/Hibernate-%2359666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Static Badge](https://img.shields.io/badge/JWT-%23000000?style=for-the-badge&logo=jsonwebtokens)
![Static Badge](https://img.shields.io/badge/Cloudinary-%233448C5?style=for-the-badge&logo=cloudinary)
![Static Badge](https://img.shields.io/badge/Redis-%23FF4438?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![image](https://img.shields.io/badge/maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
---
## Prerequisites
* Java Development Kit (`JDK`) 17 or higher installed.
* Build tool (e.g., `Maven`) installed.
* Database system `PostgreSQL` set up and configured.
* `Hibernate`, `JPA`.
* Authentication with `JWT` token.
* Login with Google `OAuth2`.
* Restfull API.
* Using `Redis` for data caching, management invalid token.
* Upload file with Cloudinary.
* OpenAPI for API documentation.
* `Docker` with docker-compose build.
---
## AirClone Features
- Authentication & Authorization :
    - Implemented JWT-based authentication.
    - Login with Google OAuth2.
    - Role-based access control (GUEST, HOST, ADMIN).
    - Redis-based token blacklisting for secure logout
- Core Features :
    - Property listing management.
    - Booking system.
    - User profile management.
    - Search and filter functionalities.
    - Geospatial search for nearby properties.
- Technical Highlights :
    - Backend: Spring Boot, Spring Security, JPA/Hibernate
    - Frontend: Angular
    - Database: PostgreSQL
    - Caching: Redis for performance optimization
    - Email Service: SMTP integration for user verification
    - Cloud Storage: Cloudinary for image management
- Performance Optimizations :
    - Implemented caching strategies using Redis.
    - Efficient data pagination.
    - Optimized database queries.
    - RESTful API design.
  
This project demonstrates proficiency in modern web development technologies, security implementation, and scalable architecture design.

---
## Getting Started
### Quick start with Docker
1. Clone the repository:
```
  git clone https://github.com/yourusername/airclone.git
  cd airclone
```

2. Create a .env file in the project root with the following variables:
```text
# Database
DBMS_USERNAME=postgres
DBMS_PASSWORD=123456

# JWT
JWT_SIGNERKEY=your_secure_jwt_key_here

# Google OAuth
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# Email
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_email_password

# Cloudinary
CLOUDINARY_NAME=your_cloudinary_name
CLOUDINARY_API_KEY=your_cloudinary_api_key
CLOUDINARY_API_SECRET=your_cloudinary_api_secret

# Admin
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123
ADMIN_EMAIL=admin@example.com
```

3. Run the application using Docker Compose:
```
docker-compose up -d
```

4. The application will be available at:
- API: http://localhost:8080
- API Documentation: http://localhost:8080/api-docs

### Using Docker hub
If you don't want to build the image yourself, you can use the pre-built image from Docker Hub:

```
docker pull yourusername/airclone:latest
```

## API Documentation
Once the application is running, you can access the API documentation at:

- http://localhost:8080/api-docs

The documentation provides detailed information about all available endpoints, request/response formats, and authentication requirements.

## Contact

[//]: # (For any questions or suggestions, please contact:)

[//]: # ()
[//]: # (- Email: qhaofdev@gmail.com)

[//]: # (- GitHub: https://github.com/qhaof26)