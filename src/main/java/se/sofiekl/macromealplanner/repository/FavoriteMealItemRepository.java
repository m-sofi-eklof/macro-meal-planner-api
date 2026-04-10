package se.sofiekl.macromealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofiekl.macromealplanner.model.FavoriteMealItem;

public interface FavoriteMealItemRepository extends JpaRepository<FavoriteMealItem, Long> {
}
