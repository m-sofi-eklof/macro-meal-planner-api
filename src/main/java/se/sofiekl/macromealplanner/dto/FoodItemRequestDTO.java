package se.sofiekl.macromealplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import se.sofiekl.macromealplanner.model.FoodSource;

public record FoodItemRequestDTO(
        @NotBlank String name,
        @Positive int quantityGrams,
        @Positive int calories,
        @Positive Double protein,
        @NotNull FoodSource source
) {}
