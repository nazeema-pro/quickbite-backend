# 🥑 QuickBite — Food Delivery Platform

A full-stack food delivery web application built with Java Spring Boot and MySQL.

## 🚀 Features
- User registration and login with JWT
- Browse and search restaurants
- Filter by cuisine type
- Add to cart and place orders
- Real-time order tracking
- Email notifications via JavaMail
- SMS notifications via Twilio
- Restaurant owner dashboard
- Admin dashboard
- Order history and reviews

## 🛠️ Tech Stack
- **Backend:** Java, Spring Boot 3.5.1
- **Security:** Spring Security, JWT
- **Database:** MySQL, Spring Data JPA
- **Frontend:** HTML, CSS, JavaScript
- **Notifications:** JavaMail, Twilio
- **Build Tool:** Maven

## 📦 How to Run
1. Clone the repository
2. Create MySQL database called quickbite_db
3. Update application.properties with your credentials
4. Run mvn spring-boot:run
5. Open http://localhost:8080

## 📡 API Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/auth/register | Register user |
| POST | /api/auth/login | Login and get token |
| GET | /api/restaurants | Get all restaurants |
| POST | /api/orders | Place an order |
| GET | /api/orders/user/{id} | Get order history |
