package se.sofiekl.macromealplanner.mapper;

import org.springframework.stereotype.Component;
import se.sofiekl.macromealplanner.dto.FoodItemSummaryDTO;
import se.sofiekl.macromealplanner.dto.MealRequestDTO;
import se.sofiekl.macromealplanner.dto.MealResponseDTO;
import se.sofiekl.macromealplanner.dto.MealSummaryDTO;
import se.sofiekl.macromealplanner.model.FoodItem;
import se.sofiekl.macromealplanner.model.Meal;
import se.sofiekl.macromealplanner.repository.FoodItemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MealMapper {

    private final FoodItemRepository foodItemRepository;

    public MealMapper(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    /**
     * Maps to DTO containing summarised meal data for Day-overview
     * @param meal The meal
     * @return The MealSummaryDTO with the data
     */
    public MealSummaryDTO toMealSummaryDTO(Meal meal) {
        List<FoodItem> foodItems = foodItemRepository.findAllByMeal(meal);

        return new MealSummaryDTO(
                meal.getId(),
                meal.getType().toString(),
                foodItems.stream()
                        .mapToInt(FoodItem::getCalories)
                        .sum(),
                foodItems.stream()
                        .mapToDouble(item -> item.getProtein() != null ? item.getProtein() : 0.0)
                        .sum()
        );
    }


    /**
     * Maps to DTO containing more detailed Meal data including all foritems
     * @param meal
     * @return
     */
    public MealResponseDTO toMealResponseDTO(Meal meal) {
        List<FoodItem> foodItems = foodItemRepository.findAllByMeal(meal);

        List<FoodItemSummaryDTO> foodItemDTOs = foodItems.stream()
                .map(this::toFoodItemSummaryDTO)
                .collect(Collectors.toList());

        return new MealResponseDTO(
                meal.getId(),
                meal.getType().toString(),
                meal.getOrderIndex(),
                meal.getDay().getDate(),
                foodItemDTOs
        );
    }

    /**
     * Converting FoodItem to FoodItemSummaryDTO
     * @param item The FoodItem
     * @return The DTO
     */
    private FoodItemSummaryDTO toFoodItemSummaryDTO(FoodItem item) {
        return new FoodItemSummaryDTO(
                item.getId(),
                item.getName(),
                item.getCalories(),
                item.getProtein(),
                item.getServings()
        );
    }
}
