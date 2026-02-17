package se.sofiekl.macromealplanner.dto.usda;

import jakarta.validation.constraints.NotBlank;

public record NutritionSearchRequestDTO(
        @NotBlank String query
) {}
