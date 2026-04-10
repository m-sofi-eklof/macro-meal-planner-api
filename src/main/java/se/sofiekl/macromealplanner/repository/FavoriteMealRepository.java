package se.sofiekl.macromealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofiekl.macromealplanner.model.FavoriteMeal;

import java.util.List;
import java.util.Optional;

public interface FavoriteMealRepository extends JpaRepository<FavoriteMeal, Long> {
    List<FavoriteMeal> findAllByUserId(Long userId);
    Optional<FavoriteMeal> findByIdAndUserId(Long id, Long userId);
}
