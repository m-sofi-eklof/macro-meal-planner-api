package se.sofiekl.macromealplanner.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.dailySummaryFlow.DailySummaryDTO;
import se.sofiekl.macromealplanner.dto.MacroGoalsDTO;
import se.sofiekl.macromealplanner.dto.dailySummaryFlow.MacroProgressDTO;
import se.sofiekl.macromealplanner.dto.dailySummaryFlow.MacroTotalsDTO;
import se.sofiekl.macromealplanner.model.FoodItem;
import se.sofiekl.macromealplanner.model.User;
import se.sofiekl.macromealplanner.repository.FoodItemRepository;
import se.sofiekl.macromealplanner.repository.MealRepository;
import se.sofiekl.macromealplanner.repository.UserRepository;

import java.time.LocalDate;

@Service
public class DailySummaryService {
    private final MealRepository mealRepository;
    private final FoodItemRepository foodItemRepository;
    private final UserRepository userRepository;

    public DailySummaryService(MealRepository mealRepository,  FoodItemRepository foodItemRepository,  UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.foodItemRepository = foodItemRepository;
        this.userRepository = userRepository;
    }

    public DailySummaryDTO getDailySummary(LocalDate date) {

        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        Long userId = user.getId();

        //gets all meals of the day
        var meals = mealRepository.findAllByUserIdAndDay_DateBetween(userId, date, date);

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
