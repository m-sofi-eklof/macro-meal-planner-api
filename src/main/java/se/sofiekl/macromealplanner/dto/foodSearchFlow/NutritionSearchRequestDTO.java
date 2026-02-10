package se.sofiekl.macromealplanner.dto.foodSearchFlow;

import jakarta.validation.constraints.NotBlank;

public record NutritionSearchRequestDTO(
        @NotBlank String query
) {}
