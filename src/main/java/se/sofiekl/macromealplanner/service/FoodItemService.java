package se.sofiekl.macromealplanner.service;

import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.FoodItemRequestDTO;
import se.sofiekl.macromealplanner.dto.FoodItemResponseDTO;
import se.sofiekl.macromealplanner.mapper.FoodItemMapper;
import se.sofiekl.macromealplanner.model.FoodItem;
import se.sofiekl.macromealplanner.model.Meal;
import se.sofiekl.macromealplanner.repository.FoodItemRepository;
import se.sofiekl.macromealplanner.repository.MealRepository;

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

    public FoodItemResponseDTO create(Long mealId, FoodItemRequestDTO dto) {
        Meal meal = mealRepository.findById(mealId).orElseThrow();
        FoodItem foodItem = mapper.toFoodItem(dto, meal);
        FoodItem savedFoodItem = foodItemRepository.save(foodItem);
        return mapper.toFoodItemResponseDTO(savedFoodItem);
    }
}
