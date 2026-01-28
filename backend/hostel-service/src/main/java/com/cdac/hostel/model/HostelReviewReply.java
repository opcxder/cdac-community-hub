package com.cdac.hostel.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

/**
 * Entity representing a reply to a hostel review/rating.
 * Allows hostel owners or admins to respond to user reviews.
 * Only one reply is allowed per rating (enforced by unique constraint).
 */
@Entity
@Data
@Table(
    name = "hostel_review_replies",
    uniqueConstraints = @UniqueConstraint(
        name = "unique_rating_reply",
        columnNames = {"ratingId"}
    )
)
public class HostelReviewReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    /**
     * Reference to the rating being replied to.
     * One reply per rating (enforced by unique constraint).
     */
    @Column(nullable = false)
    private Long ratingId;

    /**
     * ID of the user who posted the reply (hostel owner or admin).
     * No foreign key constraint as user data is in separate auth_db.
     */
    @Column(nullable = false)
    private Long repliedByUserId;

    /**
     * The reply text content.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String replyText;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp repliedAt;
}
