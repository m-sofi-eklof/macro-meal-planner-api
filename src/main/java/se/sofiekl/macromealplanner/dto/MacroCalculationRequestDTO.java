package se.sofiekl.macromealplanner.dto;

import se.sofiekl.macromealplanner.model.ActivityLevel;
import se.sofiekl.macromealplanner.model.Gender;
import se.sofiekl.macromealplanner.model.MacroGoal;

public record MacroCalculationRequestDTO(
        Gender gender,
        Integer age,
        Double weightKg,
        Double heightCm,
        ActivityLevel activityLevel,
        MacroGoal goal
) {}
