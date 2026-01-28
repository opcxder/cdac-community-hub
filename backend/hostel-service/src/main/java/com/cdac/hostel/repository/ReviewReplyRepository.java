package com.cdac.hostel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cdac.hostel.model.HostelReviewReply;

/**
 * Repository interface for HostelReviewReply entity operations.
 * Manages review reply storage and retrieval.
 */
@Repository
public interface ReviewReplyRepository extends JpaRepository<HostelReviewReply, Long> {

    /**
     * Finds a reply for a specific rating.
     * Used to check if a rating already has a reply.
     *
     * @param ratingId The ID of the rating
     * @return Optional containing the reply if found
     */
    Optional<HostelReviewReply> findByRatingId(Long ratingId);
}
