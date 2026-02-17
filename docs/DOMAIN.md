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
    - start date
    - end date
    - belongs to User
    - has days
    - unique per (user, year, weekNumber)

- **Day**
    - date
    - belongs to Week
    - belongs to User
    - has Meals

- **Meal**
    - type (BREAKFAST/LUNCH/DINNER/SNACK)
    - orderIndex
    - belongs to Day
    - belongs to user

- **FoodItem**
    - name
    - quantityGrams
    - calories
    - protein
    - source (MANUAL/USDA)
    - belongs to Meal
    - belongs to User
