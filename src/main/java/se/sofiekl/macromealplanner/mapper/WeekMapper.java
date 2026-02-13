package se.sofiekl.macromealplanner.mapper;


import org.springframework.stereotype.Component;
import se.sofiekl.macromealplanner.dto.WeekResponseDTO;
import se.sofiekl.macromealplanner.model.Week;

import java.util.stream.Collectors;

@Component
public class WeekMapper {

    private final DayMapper dayMapper;
    public WeekMapper(DayMapper dayMapper) {
        this.dayMapper = dayMapper;
    }
    public WeekResponseDTO toDTO(Week week) {
        return new WeekResponseDTO(
                week.getId(),
                week.getYear(),
                week.getWeekNumber(),
                week.getStartDate(),
                week.getEndDate(),
                week.getDays().stream().map(dayMapper::toDTO).collect(Collectors.toList())
        );
    }
}
