package se.sofiekl.macromealplanner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sofiekl.macromealplanner.dto.MealRequestDTO;
import se.sofiekl.macromealplanner.dto.MealResponseDTO;
import se.sofiekl.macromealplanner.service.MealService;

import java.util.List;

@RestController
public class MealController {
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    /**
     * Create a new meal
     * @param userId The user logging the meal
     * @param dayId The day the meal is logged into
     * @param mealRequestDTO The request data for the meal
     * @return The data object the created meal + 201CREATED
     */
    @PostMapping
    public ResponseEntity<MealResponseDTO> createMeal(
            @PathVariable Long userId,
            @PathVariable Long dayId,
            @RequestBody MealRequestDTO mealRequestDTO
    ){
        MealResponseDTO response = mealService.createMeal(userId, dayId, mealRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all meals for a day
     * @param userId The user who logged the meals
     * @param dayId The day containing the meals
     * @return A list of data objects for each of the meals + 200OK
     */
    @GetMapping
    public ResponseEntity<List<MealResponseDTO>> getAllMealsForDay(
            @PathVariable Long userId,
            @PathVariable Long dayId
    ){
        List<MealResponseDTO> response = mealService.getAllMealsForDay(userId, dayId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Get a specific meal
     * @param userId The user who logged the meal
     * @param dayId The day the meal belongs to (for URL purposes)
     * @param mealId The id of the meal
     * @return The return data of the meal + 200OK
     */
    @GetMapping("/{mealId}")
    public ResponseEntity<MealResponseDTO> getMealById(
            @PathVariable Long userId,
            @PathVariable Long dayId, //unused, for URL consistency
            @PathVariable Long mealId
    ){
        MealResponseDTO meal = mealService.getMeal(userId, mealId);
        return ResponseEntity.ok().body(meal);
    }

    /**
     * Remove a meal
     * @param userId The user who owns the meal
     * @param dayId The day the meal belongs to (URL purposes)
     * @param mealId The ID of the meal
     * @return ResponseEntity NOCONTENT
     */
    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(
            @PathVariable Long userId,
            @PathVariable Long dayId, //unsused, for URL consistency
            @PathVariable Long mealId
    ){
        mealService.deleteMeal(userId, mealId);
        return ResponseEntity.noContent().build();
    }
}
