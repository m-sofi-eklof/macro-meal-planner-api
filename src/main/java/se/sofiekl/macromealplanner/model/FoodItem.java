package se.sofiekl.macromealplanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Represents a food item in the macro meal planner
 */
@Entity
@Table(name= "food_items")
@NoArgsConstructor
@Getter
@Setter
public class FoodItem {

    /**
     * Unique identifier of the food item
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * fdcId for USDA sourced food items
     */
    Long fdcId;

    /**
     * Name of the food item
     */
    @Column(nullable = false)
    private String name;

    /**
     * Quantity in number of servings as a decimal value
     */
    @Column( nullable = false)
    private Double servings;

    /**
     * The total calories in kcal
     */
    @Column(nullable = false)
    private Integer calories;

    /**
     * The total protein in grams
     */
    @Column(nullable = false)
    private Double protein;

    /**
     * The source for the nutrient information
     * @see FoodSource
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodSource source;

    /**
     * The meal the food item belongs to
     * @see Meal
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    /**
     * The user who is logging the Food Item.
     * @see User
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
