package se.sofiekl.macromealplanner.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MealRequestDTO(
        @NotNull(message = "Meal type can not be null")
        String type,

        @NotNull(message = "Order index can not be null")
        @Positive(message = "Order index must be positive")
        Integer orderIndex
){}
