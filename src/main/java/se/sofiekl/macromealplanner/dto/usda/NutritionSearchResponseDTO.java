package se.sofiekl.macromealplanner.dto.usda;

public record NutritionSearchResponseDTO(
        String name,
        Integer calories,
        Double protein
) {}
