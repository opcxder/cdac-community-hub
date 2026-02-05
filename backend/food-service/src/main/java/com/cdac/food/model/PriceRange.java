package com.cdac.food.model;

/**
 * Enum representing the price range of food places.
 * Values match database ENUM definition exactly.
 */
public enum PriceRange {
   
    BUDGET("Budget"),

   
    MEDIUM("Medium"),

    
    EXPENSIVE("Expensive");

    private final String displayValue;

    PriceRange(String displayValue) {
        this.displayValue = displayValue;
    }

    
    public String getDisplayValue() {
        return displayValue;
    }

    
    public static PriceRange fromDisplayValue(String value) {
        for (PriceRange range : PriceRange.values()) {
            if (range.displayValue.equalsIgnoreCase(value) || range.name().equalsIgnoreCase(value)) {
                return range;
            }
        }
        throw new IllegalArgumentException("Unknown price range: " + value);
    }
}
