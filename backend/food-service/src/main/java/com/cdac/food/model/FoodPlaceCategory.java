package com.cdac.food.model;

import jakarta.persistence.*;

/**
 * Junction table entity for many-to-many relationship between FoodPlace and FoodCategory.
 * A food place can belong to multiple categories (e.g., a restaurant can serve both Breakfast and Lunch).
 */
@Entity
@Table(name = "food_place_categories", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"place_id", "category_id"}))
public class FoodPlaceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private FoodPlace foodPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private FoodCategory category;

    // Constructors
    public FoodPlaceCategory() {
    }

    public FoodPlaceCategory(FoodPlace foodPlace, FoodCategory category) {
        this.foodPlace = foodPlace;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FoodPlace getFoodPlace() {
        return foodPlace;
    }

    public void setFoodPlace(FoodPlace foodPlace) {
        this.foodPlace = foodPlace;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }
}
