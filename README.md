# Macro Meal Planner API
Backend API for a macro-focused weekly meal planner.
Plan meals around personal goals and track **calories** and **protein** per meal, day, and week.

## Built with
- Java 21
- Spring Boot
- PostgreSQL
- Docker / docker-compose
- USDA FoodData Central API
- Spring Security + JWT

## Status
Active development.

### Roadmap
- [x] Core model (User, Week, Day, Meal, FoodItem)
- [x] Local dev setup with PostgreSQL (docker-compose)
- [x] Authentication (BCrypt + JWT)
- [x] Nutrition lookup via USDA FoodData Central
- [x] Cloud deployment (Koyeb)
- [x] Daily macro summary with progress tracking
- [x] User macro goals (calories + protein)
- [x] Body stats + macro calculator (BMR/TDEE)
- [x] Favourite food items with quick-add
- [x] Favourite meal templates with quick-add
- [x] Shopping list (all food items for a week)

---

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive JWT token |

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| PATCH | `/api/users` | Update username and/or password |
| GET | `/api/users/goals` | Get current calorie and protein goals |
| PUT | `/api/users/goals` | Update calorie and protein goals |
| GET | `/api/users/stats` | Get saved body stats |
| PUT | `/api/users/stats` | Save body stats (gender, age, weight, height, activity level) |
| POST | `/api/users/calculate-macros` | Calculate recommended macros from body stats and goal |
| DELETE | `/api/users` | Delete logged-in user |

### Nutrition Search
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/nutrition/search` | Search USDA food database |

### Daily Summary
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/daily-summary/{date}` | Get macro totals, goals and progress for a date |
| GET | `/api/daily-summary/today` | Get macro summary for today |

### Weeks
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/weeks` | Create or get a week by date |
| GET | `/api/weeks` | Get all weeks for logged-in user |
| GET | `/api/weeks/current` | Get or create the current week |
| GET | `/api/weeks/{weekId}` | Get a specific week |
| GET | `/api/weeks/{weekId}/next` | Get or create the following week |
| GET | `/api/weeks/{weekId}/prev` | Get or create the previous week |
| GET | `/api/weeks/{weekId}/foods` | Get all food items for a week (for shopping list) |
| DELETE | `/api/weeks/{weekId}` | Delete a week |

### Days
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/weeks/{weekId}/days` | Get or create a day by date |
| GET | `/api/weeks/{weekId}/days` | Get all days for a week |
| GET | `/api/weeks/{weekId}/days/all` | Get all days for logged-in user |
| GET | `/api/weeks/{weekId}/days/{dayId}` | Get a day by ID |
| DELETE | `/api/weeks/{weekId}/days/{dayId}` | Delete a day |

### Meals
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/days/{dayId}/meals` | Create a meal |
| GET | `/api/days/{dayId}/meals` | Get all meals for a day |
| GET | `/api/days/{dayId}/meals/{mealId}` | Get a meal by ID |
| PUT | `/api/days/{dayId}/meals/{mealId}` | Update a meal |
| DELETE | `/api/days/{dayId}/meals/{mealId}` | Delete a meal |

### Food Items
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/meals/{mealId}/food-items` | Add a food item (manual or USDA) |
| GET | `/api/meals/{mealId}/food-items` | Get all food items for a meal |
| GET | `/api/meals/{mealId}/food-items/{foodItemId}` | Get a specific food item |
| PUT | `/api/meals/{mealId}/food-items/{foodItemId}` | Update a food item |
| DELETE | `/api/meals/{mealId}/food-items/{foodItemId}` | Delete a food item |

### Favourite Food Items
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/favorites` | Get all favourite food items |
| POST | `/api/favorites` | Save a food item as favourite |
| DELETE | `/api/favorites/{id}` | Remove a favourite |
| POST | `/api/favorites/{favoriteId}/quick-add/{mealId}` | Quick-add favourite to a meal |

### Favourite Meal Templates
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/favorite-meals` | Get all favourite meal templates |
| POST | `/api/favorite-meals` | Save a meal as a template |
| DELETE | `/api/favorite-meals/{id}` | Remove a meal template |
| POST | `/api/favorite-meals/{favoriteMealId}/quick-add/{mealId}` | Quick-add all items from template to a meal |

---

## Quick Start (local)

### Requirements
- Java 21
- Docker

### Configuration
Environment variables are documented in `.env.example`.

### Run the API
```bash
./mvnw spring-boot:run
```
