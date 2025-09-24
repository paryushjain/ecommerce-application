# E-Commerce Store Application

## Overview
This is a Spring Boot-based e-commerce application that manages customer orders. It provides RESTful APIs for creating, retrieving, and managing orders.

## Features
- Create new customer orders.
- Retrieve orders by ID or status.
- Cancel pending orders.
- Automatically update pending orders to processing status every 30 seconds.

## Technologies Used
- **Java**: Programming language.
- **Spring Boot**: Framework for building the application.
- **Gradle**: Build tool.
- **H2 Database**: In-memory database for development and testing.
- **Lombok**: Simplifies Java code by reducing boilerplate.

## Project Structure
- `OrderController`: Handles HTTP requests for managing orders.
- `OrderService`: Contains business logic for order operations.
- `OrderRepository`: Provides database access for `CustomerOrder`.
- `CustomerOrder` and `OrderItem`: Represent the domain model for orders and their items.
- `OrderNotFoundException`: Custom exception for handling missing orders.

## Endpoints
### Order Management
- **POST /api/orders**: Create a new order.
- **GET /api/orders/{id}**: Retrieve an order by ID.
- **GET /api/orders?status={status}**: Retrieve orders by status.
- **PUT /api/orders/{id}/cancel**: Cancel a pending order.

## Configuration
### Database
The application uses an H2 in-memory database. The configuration is in `src/main/resources/application.propert