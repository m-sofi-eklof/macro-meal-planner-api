package se.sofiekl.macromealplanner.dto;

public record FoodItemSummaryDTO(
        Long id,
        String name,
        Integer calories,
        Double protein,
        Double servings
) {}
