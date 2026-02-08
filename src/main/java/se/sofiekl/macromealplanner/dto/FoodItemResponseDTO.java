package se.sofiekl.macromealplanner.dto;

import se.sofiekl.macromealplanner.model.FoodSource;

import java.math.BigDecimal;

public record FoodItemResponseDTO(
        Long id,
        String name,
        int quantityGrams,
        int calories,
        BigDecimal protein,
        FoodSource source,
        Long mealId
){}
