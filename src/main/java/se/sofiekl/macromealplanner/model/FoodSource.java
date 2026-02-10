package se.sofiekl.macromealplanner.model;

/**
 * Represents the source of the nutrient info for a food item in the macro meal plnner.
 * The source being either manually input by user,
 * or from USDA FoodDate API.
 */
public enum FoodSource {
    MANUAL,
    USDA_API
}
