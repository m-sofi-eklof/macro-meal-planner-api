package se.sofiekl.macromealplanner.dto;

import java.time.LocalDate;
import java.util.List;

public record WeekResponseDTO(
        Long id,
        Integer year,
        Integer weekNumber,
        LocalDate startDate,
        LocalDate endDate,
        List<DayResponseDTO> days
) {}
