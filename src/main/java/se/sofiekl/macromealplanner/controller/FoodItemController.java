package se.sofiekl.macromealplanner.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sofiekl.macromealplanner.dto.FoodItemRequestDTO;
import se.sofiekl.macromealplanner.dto.FoodItemResponseDTO;
import se.sofiekl.macromealplanner.service.FoodItemService;

import java.util.List;

@RestController
@RequestMapping("/api/meals/{mealId}/food-items")
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    /**
     * Create a FoodItem
     *
     * @param mealId The meal the food item belongs to
     * @param dto    The request data of the food item
     * @return The response data of the FoodItem + 201CREATED
     */
    @PostMapping
    public ResponseEntity<FoodItemResponseDTO> createFoodItem(
            @PathVariable Long mealId,
            @RequestBody @Valid FoodItemRequestDTO dto
    ) {
        FoodItemResponseDTO response = foodItemService.create(mealId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all FoodItems in a meal
     *
     * @param mealId The Meal the FoodItems belong to
     * @return A list of FoodItem response data + 200OK
     */
    @GetMapping
    public ResponseEntity<List<FoodItemResponseDTO>> getAllFoodItems(
            @PathVariable Long mealId
    ) {
        List<FoodItemResponseDTO> foodItems = foodItemService.getAllFoodItemsForMeal(mealId);
        return ResponseEntity.ok(foodItems);
    }

    /**
     * Get a specifc FoodItem
     *
     * @param mealId     The meal the FoodItem belongs to (URL purposes)
     * @param foodItemId The FoodItem
     * @return The response data of the FoodItem + 200OK
     */
    @GetMapping("/{foodItemId}")
    public ResponseEntity<FoodItemResponseDTO> getFoodItemById(
            @PathVariable Long mealId,
            @PathVariable Long foodItemId
    ) {
        FoodItemResponseDTO foodItem = foodItemService.getFoodItemById(foodItemId);
        return ResponseEntity.ok(foodItem);
    }

    /**
     * Update a FoodItem
     *
     * @param mealId     The meal ID (path purposes)
     * @param foodItemId The food item's ID
     * @param dto        The updated data of the FoodItem
     * @return
     */
    @PutMapping("/{foodItemId}")
    public ResponseEntity<FoodItemResponseDTO> updateFoodItem(
            @PathVariable Long mealId,
            @PathVariable Long foodItemId,
            @Valid @RequestBody FoodItemRequestDTO dto
    ) {
        FoodItemResponseDTO updated = foodItemService.updateFoodItem(foodItemId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a FoodItem
     *
     * @param mealId     The ID of the meal (path)
     * @param foodItemId (The food item)
     * @return response entity NOCONTENT
     */
    @DeleteMapping("/{foodItemId}")
    public ResponseEntity<Void> deleteFoodItem(
            @PathVariable Long mealId,
            @PathVariable Long foodItemId
    ) {
        foodItemService.deleteFoodItem(foodItemId);
        return ResponseEntity.noContent().build();
    }
}