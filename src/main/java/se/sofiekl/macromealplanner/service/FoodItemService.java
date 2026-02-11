package se.sofiekl.macromealplanner.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.FoodItemRequestDTO;
import se.sofiekl.macromealplanner.dto.dailySummaryFlow.FoodItemResponseDTO;
import se.sofiekl.macromealplanner.mapper.FoodItemMapper;
import se.sofiekl.macromealplanner.model.FoodItem;
import se.sofiekl.macromealplanner.model.Meal;
import se.sofiekl.macromealplanner.repository.FoodItemRepository;
import se.sofiekl.macromealplanner.repository.MealRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final MealRepository mealRepository;
    private final FoodItemMapper mapper;

    public FoodItemService(FoodItemRepository foodItemRepository, MealRepository mealRepository, FoodItemMapper mapper) {
        this.foodItemRepository = foodItemRepository;
        this.mealRepository = mealRepository;
        this.mapper = mapper;
    }

    /**
     * Create a FoodItem
     * @param mealId The ID of the meal
     * @param dto
     * @return
     */
    @Transactional
    public FoodItemResponseDTO create(
            Long mealId,
            FoodItemRequestDTO dto
    ) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(()-> new EntityNotFoundException("Meal not found with id: " + mealId));

        FoodItem foodItem = mapper.toFoodItem(dto, meal);
        FoodItem savedFoodItem = foodItemRepository.save(foodItem);

        return mapper.toFoodItemResponseDTO(savedFoodItem);
    }

    /**
     * Get all FoodItems for a meal
     * @param mealId The ID of the meal
     * @return
     */
    public List<FoodItemResponseDTO> getAllFoodItemsForMeal(Long mealId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + mealId));

        List<FoodItem> foodItems = foodItemRepository.findAllByMeal(meal);

        return foodItems.stream()
                .map(mapper::toFoodItemResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific FoodItem
     * @param foodItemId The ID of the FoodItem
     * @return The response data for the FoodItem
     */
    public FoodItemResponseDTO getFoodItemById(Long foodItemId) {
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new EntityNotFoundException("FoodItem not found with id: " + foodItemId));

        return mapper.toFoodItemResponseDTO(foodItem);
    }

    /**
     * Update a FoodItem
     * @param foodItemId The ID of the FoodItem
     * @param dto The updated data for the meal
     * @return The response data with updated fields
     */
    @Transactional
    public FoodItemResponseDTO updateFoodItem(Long foodItemId, FoodItemRequestDTO dto) {
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new EntityNotFoundException("FoodItem not found with id: " + foodItemId));

        // Uppdate fields
        foodItem.setName(dto.name());
        foodItem.setCalories(dto.calories());
        foodItem.setProtein(dto.protein());
        foodItem.setQuantityGrams(dto.quantityGrams());
        foodItem.setSource(foodItem.getSource());

        FoodItem updated = foodItemRepository.save(foodItem);

        return mapper.toFoodItemResponseDTO(updated);
    }

    /**
     * Delete a FoodItem
     * @param foodItemId The id of the foodItem
     */
    @Transactional
    public void deleteFoodItem(Long foodItemId) {
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new EntityNotFoundException("FoodItem not found with id: " + foodItemId));

        foodItemRepository.delete(foodItem);
    }

}
