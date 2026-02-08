package se.sofiekl.macromealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofiekl.macromealplanner.model.FoodItem;

public interface FoodItemRepository extends JpaRepository<FoodItem,Long> {
}
