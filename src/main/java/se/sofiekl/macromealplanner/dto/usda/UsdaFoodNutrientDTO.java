package se.sofiekl.macromealplanner.dto.usda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaFoodNutrientDTO(
        Integer nutrientId,
        String nutrientName,
        String nutrientNumber,
        String unitName,
        Double value
) {}
