package se.sofiekl.macromealplanner.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DayRequestDTO(
        @NotNull(message = "Date can not be null")
        LocalDate date
){}
