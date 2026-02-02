package se.sofiekl.macromealplanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a week in the macro meal planner application.
 */
@Entity
@Table(name="weeks",
uniqueConstraints ={ @UniqueConstraint(columnNames={"user_id", "year", "week_number"})}
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
     * The weeks weeknumber.
     */
    @Column(name="week_number", nullable = false)
    private int weekNumber;

    /**
     * The year the week is in.
     */
    @Column(nullable = false)
    private int year;

    /**
     * The user who is logging the week.
     * @see User
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

