package se.sofiekl.macromealplanner.dto;

import se.sofiekl.macromealplanner.model.ActivityLevel;
import se.sofiekl.macromealplanner.model.Gender;

public record BodyStatsDTO(
        Gender gender,
        Integer age,
        Double weightKg,
        Double heightCm,
        ActivityLevel activityLevel
) {}
