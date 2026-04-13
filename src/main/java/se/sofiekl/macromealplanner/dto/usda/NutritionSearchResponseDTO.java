package se.sofiekl.macromealplanner.dto.usda;

public record NutritionSearchResponseDTO(
        Long fdcId,
        String name,
        Integer calories,
        Double protein
) {}
