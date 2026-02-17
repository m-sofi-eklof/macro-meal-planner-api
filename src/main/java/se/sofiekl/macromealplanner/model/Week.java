package se.sofiekl.macromealplanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a week in the macro meal planner application.
 */
@Entity
@Table(name="weeks",
uniqueConstraints ={ @UniqueConstraint(columnNames={"user_id", "year_value", "week_number"})}
)
@NoArgsConstructor
@Getter
@Setter
public class Week {
    /**
     * Unique identifier for the week.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The year the week is in
     */
    @Column(name = "year_value", nullable = false)
    private Integer year;

    /**
     * The weeks weeknumber.
     */
    @Column(name="week_number", nullable = false)
    private Integer weekNumber;

    /**
     * The date of the first day of the Week (Monday)
     */
    @Column(name= "start_date")
    private LocalDate startDate;

    /**
     * The date of the last day of the Week (Sunday)
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * The user who is logging the week.
     * @see User
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The Days in the Week
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "week",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Day> days = new ArrayList<>();

}

