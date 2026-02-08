package se.sofiekl.macromealplanner.dto;

public record NutritionSearchResponseDTO(
        String name,
        Integer calories,
        Double protein
) {}
