# Macro Meal Planner API
Backend API for macro-focused weekly meal planner.
The goal is to plan meals according to personal goals and track **calories** and **protein** per meal/day/week.

## Built with
- Java
- Spring Boot
- PostgreSQL
- Docker / docker-compose
- USDA FoodData Central api 
- Spring Security + JWT

## Status
This project is under active development.

### Roadmap
- [x] Core model (User, Week, Day, Meal, FoodItem)
- [x] Local dev setup with PostgreSQL (docker-compose)
- [x] Authentication (BCrypt + JWT)
- [x] Nutrition lookup via USDA FoodData
- [ ] Cloud deployment (AWS or similar)


## API endpoints
### Authentication
- POST /api/auth/register (Register a new user)
- POST /api/auth/login (Login and recieve jwt token)
### Nutrition Search
- GET /api/nutrition/search (Search USDA food database)
### Weeks
- POST /api/weeks (Create or get a week by date in week)
- GET /api/weeks/{weekId} (Get a specific week with days)
- GET /api/weeks (Get all weeks for logged-in user)
- GET /api/weeks/current (Get or create the current week)
- DELETE /api/weeks/{weekId} (Delete a week by id)
### Days
- POST /api/weeks/{weekId}/days (Get or create day from date)
- GET /api/weeks/{weekId}/days/{dayId} (Get day by ID)
- GET /api/weeks/{weekId}/days/all (Get all days for logged-in user)
- GET /api/weeks/{weekId}/days/ (Get all days for a week)
- DELETE /api/weeks/{weekId}/days/{dayId} (Delete a day)
### Meals
- POST /api/days/{dayId}/meals (Create a meal)
- GET /api/days/{dayId}/meals (Get all meals for a day)
- GET /api/days/{dayId}/meals/{mealID} (Get a meal by ID)
- DELETE /api/days/{dayId}/meals/{mealID} (Delete a meal)
### Food items
- POST /api/meals/{mealId}/food-items (Create a FoodItem, manually or from USDA search result)
- GET /api/meals/{mealId}/food-items (Get all food item for a meal)
- GET /api/meals/{mealId}/food-items/{foodItemId} (Get a specific food item)
- PUT /api/meals/{mealId}/food-items/{foodItemId} (Update a food item)
- DELETE /api/meals/{mealId}/food-items/{foodItemId} (Delete a food item)
## User (planned)
- PUT /api/users (Update username and or password for logged in user)
- PUT /api/users/goals/protein (Update protein goals for logged in user)
- PUT /api/users/goals/calories (Update calorie goal for logged-in user)
- DELETE /api/users (Delete logged-in user)

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