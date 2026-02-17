package se.sofiekl.macromealplanner.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.FoodItemRequestDTO;
import se.sofiekl.macromealplanner.dto.FoodItemResponseDTO;
import se.sofiekl.macromealplanner.dto.usda.NutritionSearchResponseDTO;
import se.sofiekl.macromealplanner.mapper.FoodItemMapper;
import se.sofiekl.macromealplanner.model.FoodItem;
import se.sofiekl.macromealplanner.model.FoodSource;
import se.sofiekl.macromealplanner.model.Meal;
import se.sofiekl.macromealplanner.model.User;
import se.sofiekl.macromealplanner.repository.FoodItemRepository;
import se.sofiekl.macromealplanner.repository.MealRepository;
import se.sofiekl.macromealplanner.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final MealRepository mealRepository;
    private final FoodItemMapper mapper;
    private final UserRepository userRepository;
    private final NutritionService nutritionService;

    public FoodItemService(FoodItemRepository foodItemRepository, MealRepository mealRepository, FoodItemMapper mapper, UserRepository userRepository, NutritionService nutritionService) {
        this.foodItemRepository = foodItemRepository;
        this.mealRepository = mealRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.nutritionService = nutritionService;
    }

    /**
     * Create a FoodItem
     * @param mealId The ID of the meal
     * @param request
     * @return
     */
    @Transactional
    public FoodItemResponseDTO create(
            Long mealId,
            FoodItemRequestDTO request
    ) {
        //get user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        //get meal
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(()-> new EntityNotFoundException("Meal not found with id: " + mealId));

        //check meal belongs to user
        if(!meal.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("You lack permission to add food to this meal");
        }

        FoodItem foodItem = new FoodItem();

        // manual food source
        if(request.source()== FoodSource.MANUAL){
            foodItem = mapper.toFoodItemManual(request,meal,user);

        // USDA food source
        }else if(request.source()== FoodSource.USDA){
            NutritionSearchResponseDTO nutrition = nutritionService.getByFdcId(request.fdcId());
            if(nutrition == null){
                throw new EntityNotFoundException("Nutrition not found for food item with id: " + request.fdcId());
            }
            foodItem = mapper.toFoodItemUSDA(nutrition, meal, user);
        }

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
