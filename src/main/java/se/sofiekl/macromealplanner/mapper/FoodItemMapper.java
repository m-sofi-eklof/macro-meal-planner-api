package se.sofiekl.macromealplanner.mapper;

import org.springframework.stereotype.Component;
import se.sofiekl.macromealplanner.dto.FoodItemRequestDTO;
import se.sofiekl.macromealplanner.dto.FoodItemResponseDTO;
import se.sofiekl.macromealplanner.model.FoodItem;
import se.sofiekl.macromealplanner.model.Meal;
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
                foodItem.getMeal().getId()
        );
    }

    public FoodItem toFoodItem(FoodItemRequestDTO dto, Meal meal){
        FoodItem foodItem = new FoodItem();
        foodItem.setName(dto.name());
        foodItem.setCalories(dto.calories());
        foodItem.setProtein(dto.protein());
        foodItem.setSource(dto.source());
        foodItem.setQuantityGrams(dto.quantityGrams());
        foodItem.setMeal(meal);
        return foodItem;
    }
}
