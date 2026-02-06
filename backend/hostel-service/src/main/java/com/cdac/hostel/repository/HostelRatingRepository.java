package com.cdac.hostel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cdac.hostel.model.HostelRating;


@Repository
public interface HostelRatingRepository extends JpaRepository<HostelRating, Long> {

  
    Optional<HostelRating> findByHostelIdAndUserId(Long hostelId, Long userId);

   
    List<HostelRating> findByHostelId(Long hostelId);

   
    long countByHostelId(Long hostelId);

   
    @Query("SELECT AVG((r.cleanlinessRating + r.foodQualityRating + r.safetyRating + " +
           "r.locationRating + r.affordabilityRating) / 5.0) " +
           "FROM HostelRating r WHERE r.hostelId = :hostelId")
    Double getAverageOverallRating(Long hostelId);

   
    @Query("SELECT AVG((r.cleanlinessRating + r.foodQualityRating + r.safetyRating + " +
           "r.locationRating + r.affordabilityRating) / 5.0) " +
           "FROM HostelRating r")
    Double getGlobalAverageRating();

  
    @Query("SELECT COUNT(r) FROM HostelRating r")
    long getTotalRatingCount();
}
