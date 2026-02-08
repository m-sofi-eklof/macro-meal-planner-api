package se.sofiekl.macromealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofiekl.macromealplanner.model.Week;

public interface WeekRepository extends JpaRepository<Week,Long> {
}
