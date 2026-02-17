package se.sofiekl.macromealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofiekl.macromealplanner.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal,Long> {

    /**
     * Finds all meals for a specific day and user
     */
    List<Meal> findAllByUserIdAndDay_DateBetween(
            Long userId,
            LocalDate startOfDay,
            LocalDate endOfDay
    );

    List<Meal> getMealById(Long id);

    List<Meal> findAllByDayId(Long id);
}
