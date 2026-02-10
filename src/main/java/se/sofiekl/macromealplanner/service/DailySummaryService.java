package se.sofiekl.macromealplanner.service;

import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.dailySummaryFlow.DailySummaryDTO;
import se.sofiekl.macromealplanner.dto.dailySummaryFlow.MacroGoalsDTO;
import se.sofiekl.macromealplanner.dto.dailySummaryFlow.MacroProgressDTO;
import se.sofiekl.macromealplanner.dto.dailySummaryFlow.MacroTotalsDTO;
import se.sofiekl.macromealplanner.model.FoodItem;
import se.sofiekl.macromealplanner.repository.FoodItemRepository;
import se.sofiekl.macromealplanner.repository.MealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DailySummaryService {
    private final MealRepository mealRepository;
    private final FoodItemRepository foodItemRepository;

    public DailySummaryService(MealRepository mealRepository,  FoodItemRepository foodItemRepository) {
        this.mealRepository = mealRepository;
        this.foodItemRepository = foodItemRepository;
    }

    public DailySummaryDTO getDailySummary(Long userId, LocalDate date) {
        //gets all meals of the day
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        var meals = mealRepository.findAllByUserIdAndDateBetween(userId, startOfDay, endOfDay);

        //calculate total calories and protein
        int totalCalories = meals.stream()
                .flatMap(meal -> foodItemRepository.findAllByMeal(meal).stream())
                .mapToInt(FoodItem::getCalories)
                .sum();
        double totalProtein = meals.stream()
                .flatMap(meal -> foodItemRepository.findAllByMeal(meal).stream())
                .mapToDouble(FoodItem::getProtein)
                .sum();

        //goals hardcoded for now, todo implement user goals
        int calorieGoal = 2000;
        double proteinGoal = 130.0;

        //calculate precent of goal
        int caloriePercent = (int) ((totalCalories/(double) calorieGoal)*100);
        int proteinPercent = (int)((totalProtein/proteinGoal)*100);

        return new DailySummaryDTO(
                date,
                new MacroTotalsDTO(totalCalories, totalProtein),
                new MacroGoalsDTO(calorieGoal,proteinGoal),
                new MacroProgressDTO(caloriePercent, proteinPercent)
        );

    }
}
