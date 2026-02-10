package se.sofiekl.macromealplanner.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaFoodDTO(
        Long fdcId,
        String description,
        List<UsdaFoodNutrientDTO> foodNutrients
) {}
