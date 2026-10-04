# 💰 FinTrack

### Personal Finance Management System

FinTrack is a full-stack personal finance management system that helps users track income, expenses, budgets, savings goals, and recurring payments through a modern Flutter mobile application backed by a secure Spring Boot REST API.

Built as a Software Engineering & Mobile App Development project using **Flutter**, **Spring Boot**, **PostgreSQL**, and **JWT Authentication**.

---

## 📱 Preview

> Screenshots will be added after development.

| Dashboard | Transactions | Analytics |
|-----------|--------------|-----------|
| 📷 | 📷 | 📷 |

---

## ✨ Features

### 🔐 Authentication
- User Registration
- Secure Login
- JWT Authentication
- Password Encryption
- Persistent Login

### 💳 Transactions
- Add Income & Expenses
- Edit & Delete Transactions
- Categories
- Payment Methods
- Search & Filters
- Transaction History

### 📊 Financial Analytics
- Expense Distribution (Donut Chart)
- Monthly Spending Trend
- Category Spending
- Cash Flow Overview
- Weekly & Monthly Insights

### 🎯 Budget Management
- Monthly Budgets
- Budget Progress
- Overspending Alerts
- Budget Utilization

### 💰 Savings Goals
- Create Goals
- Track Progress
- Contribution History
- Circular Progress Indicators

### 🔄 Recurring Expenses
- Monthly Bills
- Subscription Tracking
- Upcoming Payments
- Reminder Notifications

---

## 🏗️ System Architecture

```text
                Flutter Mobile App
                       │
                Riverpod + Dio
                       │
                REST API (HTTPS)
                       │
          Spring Boot + Spring Security
                       │
                 JWT Authentication
                       │
                PostgreSQL Database
```

---

## 🛠️ Tech Stack

### Mobile
- Flutter
- Dart
- Riverpod
- Dio / Retrofit
- Flutter Secure Storage
- FL Chart

### Backend
- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Maven

### Database
- PostgreSQL

### Tools
- Git & GitHub
- Postman
- Figma / Stitch
- Android Studio

---

## 📂 Project Structure

```text
fintrack/
│
├── fintrack_app/          # Flutter Application
│
├── fintrack_backend/      # Spring Boot Backend
│
├── docs/                  # Screenshots & Documentation
│
├── README.md
│
└── docker-compose.yml     # Future deployment
```

---

## 📱 Screens

- Splash Screen
- Login / Register
- Home Dashboard
- Transactions
- Add Transaction
- Analytics
- Budgets
- Savings Goals
- Recurring Expenses
- Notifications
- Profile

---

## 🗄️ Database Modules

- Users
- Categories
- Transactions
- Budgets
- Savings Goals
- Recurring Expenses

---

## 🔑 API Modules

| Module | Status |
|---------|--------|
| Authentication | ✅ |
| Transactions | ✅ |
| Budgets | ✅ |
| Savings Goals | ✅ |
| Analytics | ✅ |
| Recurring Expenses | ✅ |

---

## 🎯 Learning Objectives

This project demonstrates:

- Full-stack Mobile Development
- Clean Flutter Architecture
- REST API Development
- Spring Security & JWT
- PostgreSQL Database Design
- Financial Data Visualization
- State Management with Riverpod
- Client-Server Architecture

---

## 🚀 Future Improvements

- AI Spending Insights
- Receipt OCR
- PDF & CSV Export
- Multi-Currency Support
- Cloud Backup
- Family Shared Budgets
- Dark Mode
- Web Dashboard

---

## 👨‍💻 Author

**Muhammad Umer Nadeem**

Flutter • Spring Boot • PostgreSQL

> A modern FinTech portfolio project built for learning full-stack software engineering.

---

## License

FinTrack is source-available for viewing and educational purposes.

© 2026 Muhammad Umer Nadeem. All Rights Reserved.

You may view and study the source code for educational purposes.

You may not:

- use FinTrack or its source code commercially;
- sell or monetize the application or substantial portions of it;
- redistribute the source code as your own;
- publish a substantially similar copy as your own project;
- submit this project or substantial portions of it as your own
  academic/university project;
- remove copyright or attribution notices.

For permissions outside these terms, please contact the copyright
holder.
