package se.sofiekl.macromealplanner.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import se.sofiekl.macromealplanner.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class FoodItemRepositoryTest {

    @Autowired FoodItemRepository foodItemRepository;
    @Autowired MealRepository mealRepository;
    @Autowired DayRepository dayRepository;
    @Autowired WeekRepository weekRepository;
    @Autowired UserRepository userRepository;

    @Test
    void saveAndFindById_shouldPersistFoodItem() {
        //arrange
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPass123");

        Week testWeek = new Week();
        testWeek.setWeekNumber(1);
        testWeek.setYear(2026);
        testWeek.setUser(testUser);

        Day testDay = new Day();
        testDay.setDate(LocalDate.now());
        testDay.setWeek(testWeek);

        Meal testMeal = new Meal();
        testMeal.setType(MealType.BREAKFAST);
        testMeal.setOrderIndex(0);
        testMeal.setDay(testDay);

        FoodItem foodItem = new FoodItem();
        foodItem.setName("Test food item");
        foodItem.setCalories(10);
        foodItem.setProtein(new BigDecimal("10.00"));
        foodItem.setQuantityGrams(100);
        foodItem.setSource(FoodSource.MANUAL);
        foodItem.setMeal(testMeal);

        userRepository.save(testUser);
        weekRepository.save(testWeek);
        dayRepository.save(testDay);
        mealRepository.save(testMeal);

        //act
        FoodItem saved= foodItemRepository.save(foodItem);
        FoodItem foundFoodItem = foodItemRepository.findById(saved.getId()).orElseThrow();

        //assert
        assertEquals(foundFoodItem.getName(), foodItem.getName());
        assertEquals(foundFoodItem.getCalories(), foodItem.getCalories());
        assertEquals(foundFoodItem.getProtein(), foodItem.getProtein());
        assertEquals(foundFoodItem.getSource(), foodItem.getSource());
        assertEquals(foundFoodItem.getMeal().getId(), foodItem.getMeal().getId());

    }

}
