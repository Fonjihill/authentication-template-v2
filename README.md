# authentication-template-v2

## Description

This project implements a comprehensive authentication system for a Spring Boot application. It includes multiple utilisateur authentication methods, such as traditional login and registration, OAuth2 login (Google, GitHub), and an email service for account verification and password recovery.

### Key Features
* #### User Registration:

  * Allows users to register by providing a username, email, and password.
  *  Passwords are securely hashed using the BCrypt algorithm.
  * A verification email is sent to the utilisateur to confirm their account.
* #### User Login:

  * Standard form-based login with secure session management.
  * Users are redirected after successful login.
* #### OAuth2 Login:

  * OAuth2 integration with third-party providers (e.g., Google, GitHub).
  * Users can log in using external accounts, bypassing the need for a traditional account.
* #### Email Sending:

  * ##### Email service for:
    * Account verification upon registration.
    * Password reset functionality (optional).
    * Uses Java Mail API to interact with SMTP servers (e.g., Gmail).
* #### Role-Based Authorization:

    * Secure access control for authenticated users.
    * Route protection based on utilisateur roles (e.g., admin, regular utilisateur).
* #### Account Verification:

    * A verification token is generated during registration and sent via email.
    * The account remains inactive until the utilisateur verifies their email address using the token.
### Technologies Used
* #### Spring Boot: Core framework for application development.
* #### Spring Security: Provides authentication and authorization functionality.
* #### Spring Data JPA: For data persistence and interaction with the database.
* #### Spring Boot OAuth2 Client: To integrate third-party login providers (e.g., Google, GitHub).
* #### Spring Mail: For sending emails (e.g., verification, password reset).
* #### Thymeleaf: For rendering dynamic HTML content.
* #### H2 Database (Development): In-memory database for development purposes. Switch to MySQL/PostgreSQL for production environments.
* #### BCrypt: For secure password hashing.