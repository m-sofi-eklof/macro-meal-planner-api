package se.sofiekl.macromealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofiekl.macromealplanner.model.Day;

public interface DayRepository extends JpaRepository<Day,Long> {
}
