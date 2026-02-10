package se.sofiekl.macromealplanner.dto;

public record MealSummaryDTO(
        Long id,
        String type,
        Integer totalCalories,
        Double totalProtein

) {}
