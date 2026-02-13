package se.sofiekl.macromealplanner.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record WeekRequestDTO(
        /**
         * Any date within the week
         */
        @NotNull(message = "Date cannot be null")
        LocalDate date
) {}
