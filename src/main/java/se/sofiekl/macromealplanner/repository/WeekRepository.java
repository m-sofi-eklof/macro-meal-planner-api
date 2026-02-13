package se.sofiekl.macromealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofiekl.macromealplanner.model.Week;

import java.util.List;
import java.util.Optional;

public interface WeekRepository extends JpaRepository<Week,Long> {
    Optional<Week> findByUserIdAndWeekNumberAndYear(Long userId, Integer weekNumber, Integer year);
    List<Week> findAllByUserId(Long userId);
}
