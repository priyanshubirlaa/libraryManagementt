# Library Management System

This is a Spring Boot application designed to manage a library's book catalog. It provides RESTful APIs to add, view, search, update, delete books, and gracefully shut down the system. The application uses MySQL as its database and includes comprehensive exception handling.

## Features

- **Add a Book**: Add a new book with ID, title, author, genre, and availability status.
- **View All Books**: Retrieve a list of all books in the catalog.
- **Search Books**: Search for books by ID or title.
- **Update Book Details**: Modify existing book information.
- **Delete a Book**: Remove a book from the catalog.
- **Shutdown System**: Gracefully terminate the application.

## Technologies Used

- **Java 17**: Programming language
- **Spring Boot**: Framework for building the application
- **Spring Data JPA**: For database operations
- **MySQL (Railway Instance)**: Persistent storage
- **Maven**: Dependency management and build tool
- **JUnit & Mockito**: Unit testing
- **Postman**: API testing (recommended)

## Project Structure

```
src/
├── main/
│   ├── java/com/library/management/
│   │   ├── config/
│   │   │   └── ApplicationConfig.java
│   │   ├── controller/
│   │   │   ├── LibraryController.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── exception/
│   │   │   ├── LibraryException.java
│   │   │   ├── BookNotFoundException.java
│   │   │   └── ValidationException.java
│   │   ├── model/
│   │   │   └── Book.java
│   │   ├── repository/
│   │   │   └── BookRepository.java
│   │   ├── service/
│   │   │   └── LibraryService.java
│   │   └── LibraryApplication.java
│   └── resources/
│       └── application.properties
└── test/java/com/library/management/
│    ├── controller/
│    │   └── LibraryControllerTest.java
│    └── service/
│        └── LibraryServiceTest.java
└── dockerfile
```

## Prerequisites

- **Java 17**: Installed and configured (JDK)
- **Maven**: Installed for dependency management and building
- **Railway MySQL Instance**: Running database
- **Postman**: For manual API testing (optional)

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/priyanshubirlaa/libraryManagementt.git
```

### 2. Configure Railway MySQL

1. Set up a MySQL instance on Railway.
2. Retrieve the connection URL from Railway.
3. Update `src/main/resources/application.properties` with your Railway MySQL credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://your-railway-instance-url:3306/librarydb
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
   spring.jpa.hibernate.ddl-auto=update
   management.endpoint.shutdown.enabled=true
   management.endpoints.web.exposure.include=shutdown
   ```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

- **Via IDE**: Open `LibraryApplication.java` and run as a Java application.
- **Via Command Line**:
  ```bash
  mvn spring-boot:run
  ```
- The application runs on `http://localhost:8080` by default.

## Docker Deployment

### Build and Run with Docker

#### 1. Build Docker Image

```bash
docker build -t library-management .
```

#### 2. Run Docker Container

```bash
docker run -p 8080:8080 -e PORT=8080 library-management
```

### Dockerfile

```dockerfile
# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/library-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Fix ENTRYPOINT issue
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
```

## API Endpoints

### Base URLs

- **Localhost**: `http://localhost:8080`
- **Live Deployment**: `https://library-8lmm.onrender.com`

### 1. Add a Book

- **Method**: POST
- **URL**: `/api/books`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "bookId": "B001",
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "genre": "Fiction",
    "isAvailable": true
  }
  ```
- **Response**: `200 OK` with book JSON

### 2. Get All Books

- **Method**: GET
- **URL**: `/api/books`
- **Response**: `200 OK` with a list of books

### 3. Get Book by ID

- **Method**: GET
- **URL**: `/api/books/{id}`
- **Response**: `200 OK` with book details

### 4. Update Book

- **Method**: PUT
- **URL**: `/api/books/{id}`
- **Headers**: `Content-Type: application/json`
- **Body**: Updated book JSON
- **Response**: `200 OK` with updated book details

### 5. Delete Book

- **Method**: DELETE
- **URL**: `/api/books/{id}`
- **Response**: `204 No Content`

### 6. Shutdown Application

- **Method**: POST
- **URL**: `/actuator/shutdown`
- **Response**: Application shuts down

- ## Future Plans

- **Swagger API Documentation**: Implementing Swagger for API documentation.
- **SonarQube Integration**: Adding SonarQube for static code analysis and quality checks.
- **Spring Boot Actuator**: Enhancing application monitoring with Spring Boot Actuator.

## Challenges Faced

1. **JUnit Testing**: Writing effective unit tests with JUnit and Mockito required careful planning, especially for mocking dependencies and ensuring coverage of all edge cases.
2. **Exception Handling**: Identifying and handling potential exceptions was crucial. Possible exceptions included `BookNotFoundException` for invalid book searches and `ValidationException` for incorrect input data.

## Contributing

Contributions are welcome! Feel free to fork the repository and submit a pull request with improvements or new features. For any queries, reach out to me at **priyanshubirla02@gmail.com**. Happy coding!

