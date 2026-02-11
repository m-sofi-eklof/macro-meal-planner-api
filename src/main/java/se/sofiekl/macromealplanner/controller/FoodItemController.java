package se.sofiekl.macromealplanner.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import se.sofiekl.macromealplanner.dto.FoodItemRequestDTO;
import se.sofiekl.macromealplanner.dto.dailySummaryFlow.FoodItemResponseDTO;
import se.sofiekl.macromealplanner.service.FoodItemService;

@RestController
@RequestMapping("/api/meals/{mealId}/food-items")
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @PostMapping
    public FoodItemResponseDTO create(
            @PathVariable Long mealId,
            @RequestBody @Valid FoodItemRequestDTO dto
    ){
        return foodItemService.create(mealId, dto);
    }
}
