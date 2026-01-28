package com.cdac.hostel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cdac.hostel.model.HostelRating;

/**
 * Repository interface for HostelRating entity operations.
 * Provides methods for rating retrieval and statistical calculations for ranking.
 */
@Repository
public interface HostelRatingRepository extends JpaRepository<HostelRating, Long> {

    /**
     * Finds a rating by hostel and user combination.
     */
    Optional<HostelRating> findByHostelIdAndUserId(Long hostelId, Long userId);

    /**
     * Finds all ratings for a specific hostel.
     */
    List<HostelRating> findByHostelId(Long hostelId);

    /**
     * Counts the number of ratings for a specific hostel.
     */
    long countByHostelId(Long hostelId);

    // ========== Ranking Algorithm Query Methods ==========

    /**
     * Calculates the average overall rating for a hostel.
     * Overall rating is the average of all 5 criteria.
     */
    @Query("SELECT AVG((r.cleanlinessRating + r.foodQualityRating + r.safetyRating + " +
           "r.locationRating + r.affordabilityRating) / 5.0) " +
           "FROM HostelRating r WHERE r.hostelId = :hostelId")
    Double getAverageOverallRating(Long hostelId);

    /**
     * Calculates the global average rating across all hostels.
     * Used as the prior mean (C) in Bayesian average calculation.
     */
    @Query("SELECT AVG((r.cleanlinessRating + r.foodQualityRating + r.safetyRating + " +
           "r.locationRating + r.affordabilityRating) / 5.0) " +
           "FROM HostelRating r")
    Double getGlobalAverageRating();

    /**
     * Counts total number of ratings across all hostels.
     */
    @Query("SELECT COUNT(r) FROM HostelRating r")
    long getTotalRatingCount();
}
