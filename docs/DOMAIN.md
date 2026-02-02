# Domain model

## Entities
- **User**
    - username (unique)
    - password (stored hashed)
    - proteinGoal (optional)
    - calorieGoal (optional)

- **Week**
    - weekNumber
    - year
    - belongs to User
    - unique per (user, year, weekNumber)

- **Day**
    - date
    - belongs to Week

- **Meal**
    - type (BREAKFAST/LUNCH/DINNER/SNACK)
    - orderIndex
    - belongs to Day

- **FoodItem**
    - name
    - quantityGrams
    - calories
    - protein
    - source (MANUAL/API_NINJAS)
    - belongs to Meal
