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
     * Name of the food item
     */
    @Column(nullable = false)
    private String name;

    /**
     * Quantity in grams
     */
    @Column(name="quantity_grams", nullable = false)
    private int quantityGrams;

    /**
     * The total calories in kcal
     */
    @Column(nullable = false)
    private int calories;

    /**
     * The total protein in grams
     */
    @Column(nullable = false)
    private BigDecimal protein;

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
}
