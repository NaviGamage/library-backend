# 📚 Library Management System (Backend API)

A production-ready Monolithic REST API built with **Spring Boot 3.x**, **Spring Security (JWT)**, and **MySQL**. Designed with database optimization and robust role-based security.

---

## 🏗️ System Architecture
[Backend: Spring Boot App]
↓ (Hibernate / JPA)
[Database: MySQL]

---

## 🛠️ Key Technical Highlights 

* **No More $N+1$ Problems:** Optimized all major database relationships using `JOIN FETCH` to load data in a single SQL query.
* **No Memory Crashes:** Explicitly excluded entity relationships from Lombok's `toString()` using `@ToString(exclude=...)` to prevent `StackOverflowError`.
* **Bulletproof Transactions:** Secured all DB writes with `@Transactional` for atomic rollbacks and used `readOnly = true` to speed up select queries.
* **Role-Based Security:** Endpoints are strictly guarded with `@PreAuthorize("hasRole('...')")`.
* **Clean Data Flow:** Strictly decoupled database entities from the API layer using the **DTO Pattern**.

---

## 📌 Core API Endpoints

### 🔐 1. Authentication (`/api/auth`)
* `POST /api/auth/signup` - Register a new account (User / Librarian).
* `POST /api/auth/login` - Login to receive a stateless **JWT Token**.

### 📖 2. Book Catalog (`/api/books`)
* `GET /api/books` - View all books *(Supports multi-filtering by: `categoryId`, `author`, `genre`, `language`)*.
* `GET /api/books/{id}` - View full details of a specific book.
* `POST /api/books` - Add a new book 🔑 *(Librarian Only)*.
* `PUT /api/books/{id}` - Update book profiles 🔑 *(Librarian Only)*.
* `PATCH /api/books/{id}/status` - Quick update for book availability 🔑 *(Librarian Only)*.
* `DELETE /api/books/{id}` - Remove a book 🔑 *(Librarian Only)*.

### 🗂️ 3. Categories (`/api/categories`)
* `GET /api/categories` - View all book categories.
* `POST | PUT | DELETE` - Manage categories 🔑 *(Librarian Only)*.

### ⏳ 4. Book Reservations (`/api/reservations`)
* `POST /api/reservations` - Reserve a book 👤 *(User Only)*.
  * *Rules:* Allowed durations are fixed to **7, 14, or 21 days**. Checks if the book is `AVAILABLE`, the user is not blacklisted, and prevents double reservations.
* `GET /api/reservations/my` - View my borrowing history 👤 *(User Only)*.
* `GET /api/reservations` - Monitor all system reservations 🔑 *(Librarian Only)*.

### 👥 5. User Management (`/api/users`)
* `GET /api/users` - View all registered members 🔑 *(Librarian Only)*.
* `PATCH /api/users/{id}/blacklist` - Restrict a user from reserving books 🔑 *(Librarian Only)*.
* `PATCH /api/users/{id}/unblacklist` - Restore a user account 🔑 *(Librarian Only)*.

---

## 🗄️ Database Schema

* **`users`** → id, name, email, password, role, is_blacklisted, created_at
* **`categories`** → id, name
* **`books`** → id, title, author, genre, language, status, cover_image, category_id, created_at
* **`reservations`** → id, user_id, book_id, start_date, end_date, duration_days, created_at

---

## ⚙️ Quick Start Setup

1. **Clone Repo:**
   ```bash
   git clone [https://github.com/NaviGamage/library-backend.git](https://github.com/NaviGamage/library-backend.git)
   
2. **Database Config:**
Create a MySQL database named library_db and update your src/main/resources/application.properties:

      * **`spring.datasource.url=jdbc:mysql://localhost:3306/library_db`** 
      * **`spring.datasource.username=your_username`** 
      * **`spring.datasource.password=your_password`** 
      * **`app.jwt.secret=your_base64_encoded_secret_key`** 
        
3.**Run Application **

      ./mvnw spring-boot:run





   
