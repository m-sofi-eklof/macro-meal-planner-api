package se.sofiekl.macromealplanner.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import se.sofiekl.macromealplanner.dto.FoodItemRequestDTO;
import se.sofiekl.macromealplanner.dto.FoodItemResponseDTO;
import se.sofiekl.macromealplanner.dto.usda.NutritionSearchResponseDTO;
import se.sofiekl.macromealplanner.mapper.FoodItemMapper;
import se.sofiekl.macromealplanner.model.*;
import se.sofiekl.macromealplanner.repository.FoodItemRepository;
import se.sofiekl.macromealplanner.repository.MealRepository;
import se.sofiekl.macromealplanner.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;
import se.sofiekl.macromealplanner.repository.WeekRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final MealRepository mealRepository;
    private final FoodItemMapper mapper;
    private final UserRepository userRepository;
    private final NutritionService nutritionService;
    private final WeekRepository weekRepository;

    public FoodItemService(FoodItemRepository foodItemRepository, MealRepository mealRepository, FoodItemMapper mapper, UserRepository userRepository, NutritionService nutritionService, WeekRepository weekRepository) {
        this.foodItemRepository = foodItemRepository;
        this.mealRepository = mealRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.nutritionService = nutritionService;
        this.weekRepository = weekRepository;
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

            foodItem = mapper.toFoodItemUSDA(nutrition, meal, user, request.grams());

            //scale calories/protein to logged grams (values are per 100g)
            Integer calories = (int) ((request.grams() / 100.0) * nutrition.calories());
            Double protein = (request.grams() / 100.0) * nutrition.protein();

            foodItem.setCalories(calories);
            foodItem.setProtein(protein);

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
        //get user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        //get foodItem
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new EntityNotFoundException("FoodItem not found with id: " + foodItemId));


        //check foodItem belongs to user
        if(!foodItem.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("You lack permission to access this food item");
        }

        return mapper.toFoodItemResponseDTO(foodItem);
    }

    /**
     * Update a FoodItem
     * @param foodItemId The ID of the FoodItem
     * @param dto The updated data for the meal
     * @return The response data with updated fields
     */
    @Transactional
    public FoodItemResponseDTO updateFoodItem(Long foodItemId, Long mealId, FoodItemRequestDTO dto) {
        //get user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        //get food item
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new EntityNotFoundException("FoodItem not found with id: " + foodItemId));


        //check food item belongs to user
        if(!foodItem.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("You lack permission to update this food item");
        }

        // Uppdate fields

        //For USDA
        if(foodItem.getSource() == FoodSource.USDA){
            //Update nutrition scaled to updated grams
            NutritionSearchResponseDTO nutrition = nutritionService.getByFdcId(dto.fdcId());
            if(nutrition == null){
                throw new EntityNotFoundException("Nutrition information found for requested USDA Food Data Central food item");
            }
            Double protein = (dto.grams() / 100.0) * nutrition.protein();
            Integer calories = (int) ((dto.grams() / 100.0) * nutrition.calories());

            //set updates
            foodItem.setCalories(calories);
            foodItem.setProtein(protein);
            foodItem.setGrams(dto.grams());


        //for MANUAL
        }else if(foodItem.getSource() == FoodSource.MANUAL){
            foodItem.setName(dto.name());
            foodItem.setCalories(dto.calories());
            foodItem.setProtein(dto.protein());
            foodItem.setGrams(dto.grams());
            foodItem.setSource(foodItem.getSource());
        }else{
            throw new IllegalArgumentException("Food source  is null or not supported");
        }

        FoodItem updated = foodItemRepository.save(foodItem);

        return mapper.toFoodItemResponseDTO(updated);
    }

    /**
     * Gets all food items in ll days and meals for a specific week
     * @param weekID The id of the week
     * @return The food item's data av dto
     */
    @Transactional
    public List<FoodItemResponseDTO> getAllFoodItemsForWeek(Long weekID){
        //get user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        //get week
        Week week = weekRepository.findByIdAndUserId(weekID, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Week not found with id: " + weekID));

        //get all food items
        List<Day> days = week.getDays();
        List<FoodItem> foods = new ArrayList<>();
        for (Day day : days){
            day.getMeals().forEach(meal -> {
                foods.addAll(meal.getFoodItems());
            });
        }

        //map and return
        return  foods.stream()
                .map(mapper::toFoodItemResponseDTO)
                .collect(Collectors.toList());
    }


    /**
     * Delete a FoodItem
     * @param foodItemId The id of the foodItem
     */
    @Transactional
    public void deleteFoodItem(Long foodItemId) {
        //get user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        //get food item
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new EntityNotFoundException("FoodItem not found with id: " + foodItemId));


        //check food item belongs to user
        if(!foodItem.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("You lack permission to delete this food item");
        }

        foodItemRepository.delete(foodItem);
    }

}
