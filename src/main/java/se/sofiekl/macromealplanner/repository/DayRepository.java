package se.sofiekl.macromealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.sofiekl.macromealplanner.model.Day;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayRepository extends JpaRepository<Day,Long> {
    /**
     * Find all days for a specific user
     */
    List<Day> findByUserId(Long userId);

    /**
     * Find a specific day by date and useer
     * Check for already existing day
     */
    Optional<Day> findByUserIdAndDate(Long userId, LocalDate date);

    /**
     * Find all days between two dates for a specific user
     * For week overviews
     */
    List<Day> findDayByUserIdAndDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );

    /**
     * Find all days belonging to a specific week
     * @param weekId The ID of the Week
     * @return A list of Day objects
     */
    List<Day> findAllByWeekId(Long weekId);

}
