package se.sofiekl.macromealplanner.mapper;

import org.springframework.stereotype.Component;
import se.sofiekl.macromealplanner.dto.MealSummaryDTO;
import se.sofiekl.macromealplanner.model.FoodItem;
import se.sofiekl.macromealplanner.model.Meal;
import se.sofiekl.macromealplanner.repository.FoodItemRepository;

@Component
public class MealMapper {

    private final FoodItemRepository foodItemRepository;

    public MealMapper(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    public MealSummaryDTO toMealSummaryDTO(Meal meal){
        return new MealSummaryDTO(
                meal.getId(),
                meal.getType().toString(),

                foodItemRepository.findAllByMeal(meal).stream()
                .mapToInt(FoodItem::getCalories)
                .sum(),

                foodItemRepository.findAllByMeal(meal).stream()
                .mapToDouble(FoodItem::getProtein)
                .sum()
        );
    }
}
