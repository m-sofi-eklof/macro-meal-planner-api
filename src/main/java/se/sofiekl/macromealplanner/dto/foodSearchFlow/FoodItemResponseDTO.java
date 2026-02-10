package se.sofiekl.macromealplanner.dto.foodSearchFlow;

import se.sofiekl.macromealplanner.model.FoodSource;

import java.math.BigDecimal;

public record FoodItemResponseDTO(
        Long id,
        String name,
        int quantityGrams,
        int calories,
        Double protein,
        FoodSource source,
        Long mealId
){}
