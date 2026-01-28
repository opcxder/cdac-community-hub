package com.cdac.food.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a food place/restaurant submitted by users.
 * Food places must be approved by admin before being visible to public.
 */
@Entity
@Table(name = "food_places")
public class FoodPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "place_name", nullable = false, length = 200)
    private String placeName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "locality", length = 200)
    private String locality;

    @Column(name = "landmark", length = 200)
    private String landmark;

    @Column(name = "map_location", length = 500)
    private String mapLocation;

    @Column(name = "contact_info", length = 15)
    private String contactInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_range", length = 20)
    private PriceRange priceRange;

    /**
     * The primary/best category this place is known for.
     * References food_categories table.
     */
    @Column(name = "best_for_category_id")
    private Long bestForCategoryId;

    /**
     * ID of the user who submitted this food place.
     * No foreign key constraint - references auth_db.users
     */
    @Column(name = "submitted_by_user_id", nullable = false)
    private Long submittedByUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    // Relationships
    
    /**
     * Images associated with this food place
     */
    @OneToMany(mappedBy = "foodPlace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodPlaceImage> images = new ArrayList<>();

    /**
     * Categories this food place belongs to (many-to-many)
     */
    @OneToMany(mappedBy = "foodPlace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodPlaceCategory> placeCategories = new ArrayList<>();

    /**
     * Ratings and reviews for this food place
     */
    @OneToMany(mappedBy = "foodPlace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodPlaceRating> ratings = new ArrayList<>();

    // Constructors
    public FoodPlace() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMapLocation() {
        return mapLocation;
    }

    public void setMapLocation(String mapLocation) {
        this.mapLocation = mapLocation;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
    }

    public Long getBestForCategoryId() {
        return bestForCategoryId;
    }

    public void setBestForCategoryId(Long bestForCategoryId) {
        this.bestForCategoryId = bestForCategoryId;
    }

    public Long getSubmittedByUserId() {
        return submittedByUserId;
    }

    public void setSubmittedByUserId(Long submittedByUserId) {
        this.submittedByUserId = submittedByUserId;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public List<FoodPlaceImage> getImages() {
        return images;
    }

    public void setImages(List<FoodPlaceImage> images) {
        this.images = images;
    }

    public List<FoodPlaceCategory> getPlaceCategories() {
        return placeCategories;
    }

    public void setPlaceCategories(List<FoodPlaceCategory> placeCategories) {
        this.placeCategories = placeCategories;
    }

    public List<FoodPlaceRating> getRatings() {
        return ratings;
    }

    public void setRatings(List<FoodPlaceRating> ratings) {
        this.ratings = ratings;
    }
}
