package se.sofiekl.macromealplanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a meal in the macro meal planner.
 */
@Entity
@Table(name = "meals")
@NoArgsConstructor
@Getter
@Setter
public class Meal {
    /**
     * Unique identifier for the meal
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * A name for the meal
     */
    String name;

    /**
     * The type of meal, "Breakfast", "Lunch", "Dinner" or "Snack"
     * @see MealType
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealType type;

    /**
     * The day this meal belongs to.
     * @see Day
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="day_id", nullable = false)
    private Day day;

    /**
     * The order index in the list of meals within the day the meal belongs to.
     */
    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    /**
     * The user who is logging the meal.
     * @see User
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The Food Items in the Meal
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meal",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodItem> foodItems = new ArrayList<>();

}
