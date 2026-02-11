package se.sofiekl.macromealplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import se.sofiekl.macromealplanner.model.FoodSource;

public record FoodItemRequestDTO(
        @NotBlank(message = "Food name can not be blank")
        String name,

        @Positive(message = "Quantity must be positive")
        @NotNull(message = "Quantity can not be null")
        int quantityGrams,

        @Positive(message = "Calories must be positive")
        @NotNull(message = "Calories can not be null")
        int calories,

        @Positive(message = "Protein must be positive")
        @NotNull(message = "Protein can not be null")
        Double protein,

        @NotNull(message = "FoodSource can not be null")
        FoodSource source
) {}
