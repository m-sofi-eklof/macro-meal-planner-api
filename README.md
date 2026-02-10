# Macro Meal Planner API
Backend API for macro-focused weekly meal planner.
The goal is to plan meals according to personal goals and track **calories** and **protein** per meal/day/week.

## Built with
- Java
- Spring Boot
- PostgreSQL
- Docker / docker-compose
- USDA FoodData Central api
- (Planned) Spring Security + JWT

## Status
This project is under active development.

### Roadmap
- [x] Core model (User, Week, Day, Meal, FoodItem)
- [x] Local dev setup with PostgreSQL (docker-compose)
- [ ] Authentication (BCrypt + JWT)
- [x] Nutrition lookup via USDA FoodData
- [ ] Cloud deployment (AWS or similar)


## API (planned)
- POST /auth/register
- POST /auth/login
- GET /weeks
- GET /weeks/{id}
- POST /weeks/{id}/days


## Quick start (local)
### Requirements
- Java 21
- Maven (or use Maven wrapper)
- Docker (for PostgreSQL via docker-compose)

## Configuration
Environment variables are documented in `.env.example`.

### Run the API 
```bash
./mvnw spring-boot:run