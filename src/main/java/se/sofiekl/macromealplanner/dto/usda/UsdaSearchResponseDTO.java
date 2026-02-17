package se.sofiekl.macromealplanner.dto.usda;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsdaSearchResponseDTO(
        List<UsdaFoodItemDTO> foods,
        Integer totalHits,
        Integer currentPage,
        Integer totalPages
) {}
