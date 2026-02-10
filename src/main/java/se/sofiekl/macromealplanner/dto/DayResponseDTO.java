package se.sofiekl.macromealplanner.dto;

import java.time.LocalDate;
import java.util.List;

public record DayResponseDTO(
        Long id,
        LocalDate date,
        List<MealSummaryDTO> meals
) {}
