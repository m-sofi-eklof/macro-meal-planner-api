package se.sofiekl.macromealplanner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sofiekl.macromealplanner.dto.MealRequestDTO;
import se.sofiekl.macromealplanner.dto.MealResponseDTO;
import se.sofiekl.macromealplanner.service.MealService;

import java.util.List;

@RestController
@RequestMapping("/api/days/{dayId}/meals")
public class MealController {
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    /**
     * Create a new meal
     * @param dayId The day the meal is logged into
     * @param mealRequestDTO The request data for the meal
     * @return The data object the created meal + 201CREATED
     */
    @PostMapping
    public ResponseEntity<MealResponseDTO> createMeal(
            @PathVariable Long dayId,
            @RequestBody MealRequestDTO mealRequestDTO
    ){
        MealResponseDTO response = mealService.createMeal(dayId, mealRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all meals for a day
     * @param dayId The day containing the meals
     * @return A list of data objects for each of the meals + 200OK
     */
    @GetMapping
    public ResponseEntity<List<MealResponseDTO>> getAllMealsForDay(
            @PathVariable Long dayId
    ){
        List<MealResponseDTO> response = mealService.getAllMealsForDay(dayId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Get a specific meal
     * @param mealId The id of the meal
     * @return The return data of the meal + 200OK
     */
    @GetMapping("/{mealId}")
    public ResponseEntity<MealResponseDTO> getMealById(
            @PathVariable Long mealId
    ){
        MealResponseDTO meal = mealService.getMeal(mealId);
        return ResponseEntity.ok().body(meal);
    }

    /**
     * Update/edit a meal
     * @param mealId the Id of the meal
     * @param mealRequestDTO the updated values
     * @return dataobject for updated meal + 200ok
     */
    @PutMapping("/{mealId}")
    public ResponseEntity<MealResponseDTO> updateMeal(
            @PathVariable Long mealId,
            @RequestBody MealRequestDTO mealRequestDTO
    ){
        return ResponseEntity.ok(mealService.updateMeal(mealId, mealRequestDTO));
    }
    /**
     * Remove a meal
     * @param mealId The ID of the meal
     * @return ResponseEntity NOCONTENT
     */
    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(
            @PathVariable Long mealId
    ){
        mealService.deleteMeal(mealId);
        return ResponseEntity.noContent().build();
    }
}
