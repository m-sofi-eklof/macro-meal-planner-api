package se.sofiekl.macromealplanner.dto;

public record MacroCalculationResponseDTO(
        Integer recommendedCalories,
        Double recommendedProtein,
        Integer tdee,
        Integer bmr
) {}
