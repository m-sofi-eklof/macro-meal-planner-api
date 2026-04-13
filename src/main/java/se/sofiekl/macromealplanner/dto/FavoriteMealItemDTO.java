package se.sofiekl.macromealplanner.dto;

import se.sofiekl.macromealplanner.model.FoodSource;

public record FavoriteMealItemDTO(
        Long id,
        String name,
        FoodSource source,
        Long fdcId,
        Integer calories,
        Double protein,
        Double grams
) {}
