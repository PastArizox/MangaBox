# MangaBox Server

MangaBox Server is a **Spring Boot** REST API with **JWT authentication** and **PostgreSQL** database.  
It provides secure endpoints for user authentication and data management.

## **Installation & Setup**

### **Prerequisites**

- **Java 17+**
- **Maven**
- **PostgreSQL**

### **Clone the Repository**

```bash
git clone https://github.com/PastArizox/MangaBox.git
cd MangaBox/server
```

## Environment setup

1. Copy the `.env.example` file and rename it to `.env`.  
2. Modify values if necessary  
3. Run the application with `mvn spring-boot:run`.

⚠ **Never push `.env` to GitHub!** Add it to `.gitignore`:

```gitignore
.env
```

### **Install Dependencies**

```bash
mvn clean install
```

### **Start the Server**

```bash
mvn spring-boot:run
```

The API will be available at **`http://localhost:8080`**.

## **API Endpoints**

### **Authentication**

| Method | Endpoint             | Description              | Security |
|--------|----------------------|--------------------------|----------|
| POST   | `/api/auth/register` | User registration        | Public   |



## **Running Tests**

Run unit tests with **JUnit and Mockito**:

```bash
mvn test
```

## **Project Structure**

```
src/main/java/com/mangabox/server
│── controller       # REST endpoints
│── service          # Business logic
│── repository       # Database access
│── security         # Spring Security, JWT configuration
│── exception        # Global error handling
│── dto              # Data Transfer Objects (Request/Response)
└── entity           # JPA Entities
```

## **Security Features**

- **Spring Security & JWT** for authentication.
- **BCrypt** for password hashing.

## **License**

This project is licensed under the **MIT License**.

## **TODO**

- [x] Create user account
- [x] Connect to user account
- [ ] Modify user account
- [ ] Delete user account
- [ ] Password recovery

**Developed with ❤️ by [PastArizox](https://github.com/PastArizox)**