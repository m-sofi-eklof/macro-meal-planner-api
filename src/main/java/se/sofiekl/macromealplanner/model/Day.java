package se.sofiekl.macromealplanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a day in the macro meal planner application
 */
@Entity
@Table(name="days")
@NoArgsConstructor
@Getter
@Setter
public class Day {

    /**
     * Unique identifier for the day
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The date of the day
     */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * The week the day exists within
     * @see Week
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_id", nullable = false)
    private Week week;
}
