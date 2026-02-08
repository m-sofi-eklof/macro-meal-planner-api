package se.sofiekl.macromealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofiekl.macromealplanner.model.Meal;

public interface MealRepository extends JpaRepository<Meal,Long> {
}
