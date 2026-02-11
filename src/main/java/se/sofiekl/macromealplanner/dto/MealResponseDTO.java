package se.sofiekl.macromealplanner.dto;

import java.time.LocalDate;
import java.util.List;

public record MealResponseDTO(
        Long id,
        String type,
        Integer orderIndex,
        LocalDate date,
        List<FoodItemSummaryDTO> foodItems
) {}
