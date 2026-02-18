package se.sofiekl.macromealplanner.dto;

import se.sofiekl.macromealplanner.model.FoodSource;

public record FoodItemResponseDTO(
        Long id,
        String name,
        Double servings,
        Integer calories,
        Double protein,
        FoodSource source,
        Long mealId,
        Long userId,
        Long fdcId
){}
