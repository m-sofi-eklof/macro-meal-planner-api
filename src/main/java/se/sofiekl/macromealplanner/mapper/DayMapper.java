package se.sofiekl.macromealplanner.mapper;


import org.springframework.stereotype.Component;
import se.sofiekl.macromealplanner.dto.DayResponseDTO;
import se.sofiekl.macromealplanner.dto.MealSummaryDTO;
import se.sofiekl.macromealplanner.model.Day;
import se.sofiekl.macromealplanner.repository.FoodItemRepository;
import se.sofiekl.macromealplanner.repository.MealRepository;

import java.util.List;

@Component
public class DayMapper {
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    public DayMapper(MealRepository mealRepository, MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
    }

    public DayResponseDTO toDTO(Day day) {
        List<MealSummaryDTO> mealSummaries = mealRepository.findAllByUserIdAndDay_DateBetween(
                day.getUser().getId(), day.getDate(), day.getDate())
                .stream()
                .map(mealMapper::toMealSummaryDTO)
                .toList();
        return new DayResponseDTO(
                day.getId(),
                day.getDate(),
                mealSummaries
        );
    }
}
