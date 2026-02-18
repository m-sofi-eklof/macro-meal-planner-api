package se.sofiekl.macromealplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import se.sofiekl.macromealplanner.model.FoodSource;
import se.sofiekl.macromealplanner.validation.FoodItemRequestConstraint;

@FoodItemRequestConstraint
public record FoodItemRequestDTO(

        @NotNull(message = "FoodSource can not be null")
        FoodSource source,

        @NotNull(message = "Quantity can not be null")
        Double servings,

        //fields for manual input source
        String name,
        Integer calories,
        Double protein,

        //usda fields
        Long fdcId
) {}
