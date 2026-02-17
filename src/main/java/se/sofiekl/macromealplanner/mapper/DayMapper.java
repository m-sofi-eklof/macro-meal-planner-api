package se.sofiekl.macromealplanner.mapper;


import org.springframework.stereotype.Component;
import se.sofiekl.macromealplanner.dto.DayResponseDTO;
import se.sofiekl.macromealplanner.dto.MealSummaryDTO;
import se.sofiekl.macromealplanner.model.Day;
import se.sofiekl.macromealplanner.repository.FoodItemRepository;
import se.sofiekl.macromealplanner.repository.MealRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DayMapper {
    private final MealMapper mealMapper;

    public DayMapper(MealMapper mealMapper) {
        this.mealMapper = mealMapper;
    }

    public DayResponseDTO toDTO(Day day) {
        List<MealSummaryDTO> mealSummaries = day.getMeals()
                .stream().map(mealMapper::toMealSummaryDTO)
                        .collect(Collectors.toList());

        return new DayResponseDTO(
                day.getId(),
                day.getDate(),
                mealSummaries
        );
    }
}
