package se.sofiekl.macromealplanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user in the mel planner application
 * <p>
 *     Each ser has a password and unique username, and can have aa protein goal,
 *     and/or a calorie goal.
 * </p>
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {
    /**
     * Unique identifier for the user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique username for the user
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * The user's password (STORED HASHED)
     */
    @Column(nullable = false)
    private String password;

    /**
     * The user's protein goal in grams
     */
    @Column(name = "protein_goal")
    private Double proteinGoal;

    /**
     * The user's calorie goal in kcal
     */
    @Column(name= "calorie_goal")
    private Integer calorieGoal;

    /**
     * Constructor
     * @param username the username
     * @param password the password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
