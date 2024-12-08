# Portfolio Manager

> A portfolio manager application to showcase your professional profile, projects, and CV. This project includes an admin panel to modify content, built with Angular (frontend) and Spring Boot (backend). It supports multilingual functionality for English and German using ngx-translate.

## Table of Contents

- [Description](#description)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Admin Panel](#admin-panel)
- [JWT Authentication](#jwt-authentication)
---

## Description

The **Portfolio Manager** is a web application developed to display a professional profile, showcase projects, and include a detailed CV. Built with **Angular** for the frontend and **Spring Boot** for the backend, the project features an **admin panel** that allows for easy content management (projects, CV, personal information). 

The application supports **multilingual capabilities** in **English** and **German** and utilizes **JWT** for secure user authentication. The goal of the project is to offer a dynamic platform where you can manage and present your professional achievements.

---

## Features

- **Dynamic Portfolio**: Showcase your personal profile, projects, and CV.
- **Admin Panel**: A secure panel for managing portfolio content (with JWT authentication).
- **Multi-language Support**: Supports English and German using ngx-translate.
- **JWT Authentication**: Secure login and session management.
- **Profile & Projects Management**: Easy-to-use interface for updating your profile and projects.
- **Responsive Design**: Mobile-friendly layout for all devices.
- **Spring Boot Backend**: Robust backend using Spring Boot to handle business logic, data persistence, and authentication.
- **Angular Frontend**: Fast, modern frontend using Angular to display portfolio information dynamically.

---

## Technologies Used

- **Frontend**:
  - Angular
  - ngx-translate (for multi-language support)
  - Bootstrap (or any other UI framework you use)
  - HttpClientModule (for HTTP requests)
  
- **Backend**:
  - Spring Boot (for REST API and backend services)
  - JWT (JSON Web Token for authentication)
  - Spring Security (for securing the application)

- **Other**:
  - PostgreSQL

---

## Installation

To set up and run the project locally, follow these steps:

### 1. Clone the repository:

```bash
git clone https://https://github.com/waelchabchoub/portfolio-manager-springboot-and-angular-web-application.git
cd portfolio-manager
```

### 2. Install dependencies:

#### Frontend (Angular)

Navigate to the `src/main/app` directory and install the required dependencies:

```bash
cd src/main/app
npm install
```

### 3. Configure environment:

#### Backend:

Ensure you have the necessary database PostgreSQL with portfolio-manager set up.
Configure the database connection in application.yml in the Spring Boot project.

Navigate to the `root` directory and install the required dependencies:

```bash
mvn clean install
```
### 4. Running the Project Locally

#### Frontend
To start the Angular development server, run the following command from the frontend directory:

```bash
ng serve
```
#### Backend
To run the Spring Boot application, navigate to the backend directory and run::

```bash
mvn spring-boot:run
```

## Usage

### Admin Panel
The Admin Panel allows you to modify the content displayed on the portfolio, including your profile, projects, and CV. It uses JWT authentication to ensure that only authorized users can update the content.

#### Features:
- **Login**: Use the JWT login system for authentication. After logging in, you'll be granted access to the admin panel.
- **Manage Content**: Update your portfolio, CV, and project details through simple forms.
- **Save Changes**: Once you’ve updated the information, it will be automatically reflected in your portfolio.

### Language Switching
The application supports English and German languages. The language can be switched using the language selector in the UI, and the content will be translated dynamically.

To change the language, use the translation options in the UI.

### Configuration

#### JWT Authentication
The backend uses JWT to authenticate users securely. The authentication process works as follows:

1. **Login**: Users can log in using their credentials (e.g., username and password).
2. **Token Generation**: Upon successful login, the backend generates a JWT token, which is sent to the client.
3. **Secure Endpoints**: The Angular frontend sends the JWT token with every request to protected endpoints (like the admin panel) in the HTTP authorization header.
4. **Token Expiration**: The token expires after a set period, and users will need to log in again to receive a new token.

For testing purposes, you can configure your JWT secret and expiration time in the `application.properties` file of the Spring Boot backend.

#### Multi-language Support
The project uses `ngx-translate` for multi-language support. Currently, the languages supported are:

- **English**: `en.json`
- **German**: `de.json`

Ensure that translation files are located in the `assets/i18n/` directory and follow the proper format.

#### Example translation file (`en.json`):

```json
{
  "PROFILE": {
    "TITLE": "My Portfolio",
    "DESCRIPTION": "Welcome to my portfolio website."
  },
  "PROJECTS": {
    "HEADER": "Projects",
    "DESCRIPTION": "These are some of the projects I've worked on."
  }
}
```
