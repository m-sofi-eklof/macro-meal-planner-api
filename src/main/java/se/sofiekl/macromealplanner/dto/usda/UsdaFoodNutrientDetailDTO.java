package se.sofiekl.macromealplanner.dto.usda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaFoodNutrientDetailDTO(
        UsdaNutrientDTO nutrient,
        Double amount
) {
}
