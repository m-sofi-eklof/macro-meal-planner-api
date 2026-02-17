package se.sofiekl.macromealplanner.mapper;


import org.springframework.stereotype.Component;
import se.sofiekl.macromealplanner.dto.DayResponseDTO;
import se.sofiekl.macromealplanner.dto.WeekResponseDTO;
import se.sofiekl.macromealplanner.model.Week;
import se.sofiekl.macromealplanner.repository.DayRepository;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class WeekMapper {

    private final DayMapper dayMapper;
    private final DayRepository dayRepository;

    public WeekMapper(DayMapper dayMapper, DayRepository dayRepository) {
        this.dayMapper = dayMapper;
        this.dayRepository = dayRepository;
    }

    /**
     * Maps from Week object to corresponding response data transfer object
     * @param week The Week object
     * @return A WeekResponseDTO
     */
    public WeekResponseDTO toDTO(Week week) {
        //get all days from repository
        var days = dayRepository.findAllByWeekId(week.getId());

        //map
        return new WeekResponseDTO(
                week.getId(),
                week.getYear(),
                week.getWeekNumber(),
                week.getStartDate(),
                week.getEndDate(),
                days.stream().map(dayMapper::toDTO).collect(Collectors.toList())
        );
    }
}
