package se.sofiekl.macromealplanner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sofiekl.macromealplanner.dto.FavoriteMealDTO;
import se.sofiekl.macromealplanner.dto.FoodItemResponseDTO;
import se.sofiekl.macromealplanner.service.FavoriteMealService;

import java.util.List;

@RestController
@RequestMapping("/api/favorite-meals")
public class FavoriteMealController {

    private final FavoriteMealService favoriteMealService;

    public FavoriteMealController(FavoriteMealService favoriteMealService) {
        this.favoriteMealService = favoriteMealService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteMealDTO>> getUserFavoriteMeals() {
        return ResponseEntity.ok(favoriteMealService.getUserFavoriteMeals());
    }

    @PostMapping
    public ResponseEntity<FavoriteMealDTO> saveMealAsFavorite(
            @RequestParam Long fromMealId,
            @RequestParam String name
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteMealService.saveMealAsFavorite(fromMealId, name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavoriteMeal(@PathVariable Long id) {
        favoriteMealService.deleteFavoriteMeal(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{favoriteMealId}/quick-add/{mealId}")
    public ResponseEntity<List<FoodItemResponseDTO>> quickAddMeal(
            @PathVariable Long favoriteMealId,
            @PathVariable Long mealId
    ) {
        return ResponseEntity.ok(favoriteMealService.quickAddMeal(mealId, favoriteMealId));
    }
}
