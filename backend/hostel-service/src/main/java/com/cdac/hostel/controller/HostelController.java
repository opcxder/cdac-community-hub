package com.cdac.hostel.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
 * Authentication is handled by API Gateway, which forwards userId via X-User-Id header.
 */
@RestController
@RequestMapping("/api/hostel/hostels")
public class HostelController {

    private static final Logger logger = LoggerFactory.getLogger(HostelController.class);

    @Autowired
    private HostelService hostelService;

    @Autowired
    private HostelRatingService ratingService;

    @Autowired
    private RankingService rankingService;

    /**
     * Parse userId from X-User-Id header
     */
    private Long parseUserId(String userIdHeader) {
        if (userIdHeader == null || userIdHeader.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(userIdHeader);
        } catch (NumberFormatException e) {
            logger.error("Invalid X-User-Id header: {}", userIdHeader);
            return null;
        }
    }

    @PostMapping
    public ResponseEntity<?> createHostel(
            @RequestBody com.cdac.hostel.dto.HostelRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {

        Long userId = parseUserId(userIdHeader);
        if (userId == null) {
            logger.warn("⚠️ No userId in X-User-Id header for creating hostel");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
        }

        logger.info("🏠 [HOSTEL-CONTROLLER] Received hostel submission: name={}, userId={}",
                request.getHostelName(), userId);
        logger.info("🏠 [HOSTEL-CONTROLLER] Request data: {}", request);

        return ResponseEntity.ok(hostelService.createHostel(request, userId));
    }

    @GetMapping("/approved")
    public List<com.cdac.hostel.dto.HostelDTO> getApprovedHostels() {
        return hostelService.getApprovedHostels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.cdac.hostel.dto.HostelDTO> getHostelById(@PathVariable Long id) {
        logger.info("🏠 [HOSTEL-CONTROLLER] Fetching hostel details for ID: {}", id);
        com.cdac.hostel.dto.HostelDTO hostel = hostelService.getHostelDTOById(id);
        return ResponseEntity.ok(hostel);
    }

    @GetMapping("/pending")
    public List<com.cdac.hostel.dto.HostelDTO> getPendingHostels() {
        return hostelService.getPendingHostels();
    }

    // ========== Review Reply Endpoints ==========

    @PostMapping("/ratings/{ratingId}/reply")
    public ResponseEntity<?> createReply(
            @PathVariable Long ratingId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestBody ReplyRequest request) {

        logger.info("📡 [HOSTEL-CONTROLLER] Received reply request for ratingId={}, X-User-Id={}", ratingId, userIdHeader);

        Long userId = parseUserId(userIdHeader);
        if (userId == null) {
            logger.error("❌ [HOSTEL-CONTROLLER] No userId in X-User-Id header for creating reply to rating: {}", ratingId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
        }

        logger.info("📝 [HOSTEL-CONTROLLER] Processing reply: ratingId={}, userId={}", ratingId, userId);
        HostelReviewReply reply = ratingService.createReply(ratingId, userId, request.getReplyText());
        return ResponseEntity.status(HttpStatus.CREATED).body(reply);
    }

   
    @GetMapping("/ratings/{ratingId}/replies")
    public ResponseEntity<List<HostelReviewReply>> getRepliesForRating(@PathVariable Long ratingId) {
        List<HostelReviewReply> replies = ratingService.getRepliesForRating(ratingId);
        return ResponseEntity.ok(replies);
    }

   
    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<?> deleteReply(
            @PathVariable Long replyId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {

        Long userId = parseUserId(userIdHeader);
        if (userId == null) {
            logger.warn("⚠️ No userId in X-User-Id header for deleting reply: {}", replyId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
        }

        ratingService.deleteReply(replyId, userId);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/ranked")
    public ResponseEntity<List<RankedHostelDTO>> getRankedHostels() {
        List<RankedHostelDTO> ranked = rankingService.getRankedHostels();
        return ResponseEntity.ok(ranked);
    }

   
    @GetMapping("/ranked/top")
    public ResponseEntity<List<RankedHostelDTO>> getTopRankedHostels(
            @RequestParam(defaultValue = "10") int limit) {

        List<RankedHostelDTO> topRanked = rankingService.getTopRankedHostels(limit);
        return ResponseEntity.ok(topRanked);
    }
}
