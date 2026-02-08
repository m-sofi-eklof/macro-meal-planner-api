package se.sofiekl.macromealplanner.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiNinjasNutritionItemDTO(
        String name,
        @JsonProperty("calories")
        Object calories,
        @JsonProperty("protein_g")
        Object protein_g,
        @JsonProperty("serving_size_g")
        Object serving_size_g
) {
}
