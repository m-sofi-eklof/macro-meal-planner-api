package se.sofiekl.macromealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofiekl.macromealplanner.model.Meal;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal,Long> {

    /**
     * Finds all meals for a specific day
     */
    List<Meal> findAllByUserIdAndDateBetween(
            Long userId,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );
}
