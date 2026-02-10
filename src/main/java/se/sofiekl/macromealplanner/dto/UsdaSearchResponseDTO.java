package se.sofiekl.macromealplanner.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaSearchResponseDTO(
        List<UsdaFoodItemDTO> foods,
        Integer totalHits,
        Integer currentPage,
        Integer totalPages
) {}
