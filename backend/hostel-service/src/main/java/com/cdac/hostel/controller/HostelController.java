package com.cdac.hostel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.hostel.dto.RankedHostelDTO;
import com.cdac.hostel.dto.ReplyRequest;
import com.cdac.hostel.model.Hostel;
import com.cdac.hostel.model.HostelReviewReply;
import com.cdac.hostel.service.HostelRatingService;
import com.cdac.hostel.service.HostelService;
import com.cdac.hostel.service.RankingService;

/**
 * REST controller for public hostel operations.
 * Handles hostel creation, retrieval, review replies, and ranking.
 */
@RestController
@RequestMapping("/api/hostel/hostels")
public class HostelController {

    @Autowired
    private HostelService hostelService;

    @Autowired
    private HostelRatingService ratingService;

    @Autowired
    private RankingService rankingService;

    @PostMapping
    public Hostel createHostel(
            @RequestBody Hostel hostel,
            @RequestParam Long userId) {

        return hostelService.createHostel(hostel, userId);
    }

    @GetMapping("/approved")
    public List<Hostel> getApprovedHostels() {
        return hostelService.getApprovedHostels();
    }
    
    @GetMapping("/pending")
    public List<Hostel> getPendingHostels() {
        return hostelService.getPendingHostels();
    }

    // ========== Review Reply Endpoints ==========

    /**
     * Creates a reply to a hostel rating/review.
     */
    @PostMapping("/ratings/{ratingId}/reply")
    public ResponseEntity<HostelReviewReply> createReply(
            @PathVariable Long ratingId,
            @RequestParam Long userId,
            @RequestBody ReplyRequest request) {

        HostelReviewReply reply = ratingService.createReply(ratingId, userId, request.getReplyText());
        return ResponseEntity.status(HttpStatus.CREATED).body(reply);
    }

    /**
     * Retrieves the reply for a specific rating.
     */
    @GetMapping("/ratings/{ratingId}/reply")
    public ResponseEntity<HostelReviewReply> getReplyForRating(@PathVariable Long ratingId) {
        HostelReviewReply reply = ratingService.getReplyForRating(ratingId);
        if (reply == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reply);
    }

    /**
     * Deletes a reply to a rating.
     */
    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId) {
        ratingService.deleteReply(replyId);
        return ResponseEntity.noContent().build();
    }

    // ========== Ranking Endpoints ==========

    /**
     * Retrieves all approved hostels ranked by Bayesian average.
     * Hostels with higher quality and quantity of ratings rank higher.
     *
     * @return List of ranked hostels sorted by Bayesian average (descending)
     */
    @GetMapping("/ranked")
    public ResponseEntity<List<RankedHostelDTO>> getRankedHostels() {
        List<RankedHostelDTO> ranked = rankingService.getRankedHostels();
        return ResponseEntity.ok(ranked);
    }

    /**
     * Retrieves top N ranked hostels.
     *
     * @param limit Number of top hostels to retrieve (default: 10)
     * @return List of top N ranked hostels
     */
    @GetMapping("/ranked/top")
    public ResponseEntity<List<RankedHostelDTO>> getTopRankedHostels(
            @RequestParam(defaultValue = "10") int limit) {
        
        List<RankedHostelDTO> topRanked = rankingService.getTopRankedHostels(limit);
        return ResponseEntity.ok(topRanked);
    }
}
