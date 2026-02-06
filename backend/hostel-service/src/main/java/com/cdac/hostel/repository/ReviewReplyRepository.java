package com.cdac.hostel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cdac.hostel.model.HostelReviewReply;


@Repository
public interface ReviewReplyRepository extends JpaRepository<HostelReviewReply, Long> {

   
    Optional<HostelReviewReply> findByRatingId(Long ratingId);
}
