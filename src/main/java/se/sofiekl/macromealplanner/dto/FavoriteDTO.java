package se.sofiekl.macromealplanner.dto;

import se.sofiekl.macromealplanner.model.FoodSource;

public record FavoriteDTO(
        Long id,
        String name,
        FoodSource source,
        Long fdcId,
        Integer calories,
        Double protein,
        Double servings,
        String servingDescription
) {}
