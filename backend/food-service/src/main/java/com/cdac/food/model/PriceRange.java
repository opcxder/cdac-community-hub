package com.cdac.food.model;

/**
 * Enum representing the price range of food places.
 * Values match database ENUM definition exactly.
 */
public enum PriceRange {
    /**
     * Budget-friendly pricing
     */
    BUDGET("Budget"),

    /**
     * Medium pricing
     */
    MEDIUM("Medium"),

    /**
     * Expensive pricing
     */
    EXPENSIVE("Expensive");

    private final String displayValue;

    PriceRange(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * Get the database/display value with proper formatting
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Convert from database string value to enum
     */
    public static PriceRange fromDisplayValue(String value) {
        for (PriceRange range : PriceRange.values()) {
            if (range.displayValue.equalsIgnoreCase(value) || range.name().equalsIgnoreCase(value)) {
                return range;
            }
        }
        throw new IllegalArgumentException("Unknown price range: " + value);
    }
}
