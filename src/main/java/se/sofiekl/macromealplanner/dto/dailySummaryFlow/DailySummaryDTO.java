package se.sofiekl.macromealplanner.dto.dailySummaryFlow;


import se.sofiekl.macromealplanner.dto.MacroGoalsDTO;

import java.time.LocalDate;

public record DailySummaryDTO(
        LocalDate date,
        MacroTotalsDTO totals,
        MacroGoalsDTO goals,
        MacroProgressDTO progress
) {}
