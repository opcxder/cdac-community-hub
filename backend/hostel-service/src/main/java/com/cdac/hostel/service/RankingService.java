package com.cdac.hostel.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdac.hostel.dto.RankedHostelDTO;
import com.cdac.hostel.model.Hostel;
import com.cdac.hostel.model.HostelStatus;
import com.cdac.hostel.repository.HostelRatingRepository;
import com.cdac.hostel.repository.HostelRepository;

/**
 * Service layer for hostel ranking operations.
 * Implements Bayesian average ranking algorithm to fairly rank hostels
 * considering both rating quality and quantity.
 * 
 * Formula: Bayesian Average = (C * m + R * v) / (m + v)
 * Where:
 * - C = Global average rating across all hostels (prior mean)
 * - m = Confidence parameter (minimum votes required)
 * - R = Average rating for this hostel
 * - v = Number of votes (ratings) for this hostel
 */
@Service
public class RankingService {

    private static final Logger logger = LoggerFactory.getLogger(RankingService.class);
    
    // Confidence parameter: minimum number of ratings to trust the average
    // Hostels with fewer ratings will be pulled toward the global average
    private static final int CONFIDENCE_PARAMETER = 5;

    @Autowired
    private HostelRepository hostelRepository;

    @Autowired
    private HostelRatingRepository ratingRepository;

    /**
     * Calculates the Bayesian average for a specific hostel.
     * 
     * @param hostelId The ID of the hostel
     * @return The Bayesian average rating (1-5 scale)
     */
    public double calculateBayesianAverage(Long hostelId) {
        logger.debug("Calculating Bayesian average for hostel: hostelId={}", hostelId);

        // Get hostel's average rating (R)
        Double hostelAverage = ratingRepository.getAverageOverallRating(hostelId);
        if (hostelAverage == null) {
            hostelAverage = 0.0;
        }

        // Get hostel's rating count (v)
        long ratingCount = ratingRepository.countByHostelId(hostelId);

        // Get global average rating (C)
        Double globalAverage = ratingRepository.getGlobalAverageRating();
        if (globalAverage == null) {
            globalAverage = 3.0; // Default to middle rating if no ratings exist
        }

        // Confidence parameter (m)
        int m = CONFIDENCE_PARAMETER;

        // Bayesian Average = (C * m + R * v) / (m + v)
        double bayesianAverage = (globalAverage * m + hostelAverage * ratingCount) / (m + ratingCount);

        logger.info("Bayesian average calculated: hostelId={}, bayesian={}, simple={}, count={}, global={}", 
                    hostelId, bayesianAverage, hostelAverage, ratingCount, globalAverage);

        return bayesianAverage;
    }

    /**
     * Retrieves all approved hostels ranked by Bayesian average.
     * Hostels with higher Bayesian averages appear first.
     *
     * @return List of ranked hostels sorted by Bayesian average (descending)
     */
    public List<RankedHostelDTO> getRankedHostels() {
        logger.info("Fetching ranked hostels");

        // Get all approved hostels
        List<Hostel> hostels = hostelRepository.findByStatus(HostelStatus.APPROVED);

        // Calculate Bayesian average for each and create DTOs
        List<RankedHostelDTO> rankedHostels = new ArrayList<>();

        for (Hostel hostel : hostels) {
            RankedHostelDTO dto = new RankedHostelDTO();
            dto.setHostelId(hostel.getHostelId());
            dto.setHostelName(hostel.getHostelName());
            
            // Calculate Bayesian average
            double bayesianAvg = calculateBayesianAverage(hostel.getHostelId());
            dto.setBayesianAverage(bayesianAvg);

            // Get simple average for comparison
            Double simpleAvg = ratingRepository.getAverageOverallRating(hostel.getHostelId());
            dto.setSimpleAverage(simpleAvg != null ? simpleAvg : 0.0);

            // Get rating count
            long count = ratingRepository.countByHostelId(hostel.getHostelId());
            dto.setRatingCount(count);

            rankedHostels.add(dto);
        }

        // Sort by Bayesian average (descending)
        List<RankedHostelDTO> sorted = rankedHostels.stream()
                .sorted(Comparator.comparing(RankedHostelDTO::getBayesianAverage).reversed())
                .collect(Collectors.toList());

        logger.info("Ranked {} hostels by Bayesian average", sorted.size());

        return sorted;
    }

    /**
     * Retrieves top N ranked hostels.
     *
     * @param limit Number of top hostels to retrieve
     * @return List of top N ranked hostels
     */
    public List<RankedHostelDTO> getTopRankedHostels(int limit) {
        logger.info("Fetching top {} ranked hostels", limit);

        List<RankedHostelDTO> allRanked = getRankedHostels();

        return allRanked.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
