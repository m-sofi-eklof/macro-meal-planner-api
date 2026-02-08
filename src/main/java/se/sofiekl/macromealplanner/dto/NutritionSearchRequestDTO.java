package se.sofiekl.macromealplanner.dto;

import jakarta.validation.constraints.NotBlank;

public record NutritionSearchRequestDTO(
        @NotBlank String query
) {}
