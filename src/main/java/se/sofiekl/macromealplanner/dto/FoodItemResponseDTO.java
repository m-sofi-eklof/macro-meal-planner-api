package se.sofiekl.macromealplanner.dto;

import se.sofiekl.macromealplanner.model.FoodSource;

public record FoodItemResponseDTO(
        Long id,
        String name,
        int quantityGrams,
        int calories,
        Double protein,
        FoodSource source,
        Long mealId,
        Long userId
){}
