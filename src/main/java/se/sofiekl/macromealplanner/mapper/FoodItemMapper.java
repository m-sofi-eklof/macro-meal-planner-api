package se.sofiekl.macromealplanner.mapper;

import org.springframework.stereotype.Component;
import se.sofiekl.macromealplanner.dto.FoodItemRequestDTO;
import se.sofiekl.macromealplanner.dto.FoodItemResponseDTO;
import se.sofiekl.macromealplanner.dto.usda.NutritionSearchResponseDTO;
import se.sofiekl.macromealplanner.dto.usda.UsdaFoodDTO;
import se.sofiekl.macromealplanner.model.FoodItem;
import se.sofiekl.macromealplanner.model.FoodSource;
import se.sofiekl.macromealplanner.model.Meal;
import se.sofiekl.macromealplanner.model.User;
import se.sofiekl.macromealplanner.repository.MealRepository;

@Component
public class FoodItemMapper {

    private final MealRepository mealRepository;

    public FoodItemMapper(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public FoodItemResponseDTO toFoodItemResponseDTO(FoodItem foodItem){
        return new FoodItemResponseDTO(
                foodItem.getId(),
                foodItem.getName(),
                foodItem.getQuantityGrams(),
                foodItem.getCalories(),
                foodItem.getProtein(),
                foodItem.getSource(),
                foodItem.getMeal().getId(),
                foodItem.getUser().getId()
        );
    }

    public FoodItem toFoodItemManual(FoodItemRequestDTO dto, Meal meal, User user){
        FoodItem foodItem = new FoodItem();
        foodItem.setName(dto.name());
        foodItem.setCalories(dto.calories());
        foodItem.setProtein(dto.protein());
        foodItem.setSource(dto.source());
        foodItem.setQuantityGrams(dto.quantityGrams());
        foodItem.setMeal(meal);
        foodItem.setUser(user);
        return foodItem;
    }

    public FoodItem toFoodItemUSDA(NutritionSearchResponseDTO dto, Meal meal, User user){
        FoodItem foodItem = new FoodItem();
        foodItem.setSource(FoodSource.USDA);
        foodItem.setMeal(meal);
        foodItem.setUser(user);
        foodItem.setProtein(dto.protein());
        foodItem.setCalories(dto.calories());
        foodItem.setName(dto.name());
        return foodItem;
    }
}
