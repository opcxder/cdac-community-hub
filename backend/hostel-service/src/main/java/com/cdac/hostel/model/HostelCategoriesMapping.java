package com.cdac.hostel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

/**
 * Entity representing the many-to-many relationship between hostels and categories.
 * A hostel can belong to multiple categories (e.g., "PG", "Boys Only", "Co-living").
 * The unique constraint ensures a hostel-category pair is not duplicated.
 */
@Entity
@Data
@Table(
    name = "hostel_categories_mapping",
    uniqueConstraints = @UniqueConstraint(
        name = "unique_hostel_category",
        columnNames = {"hostelId", "categoryId"}
    )
)
public class HostelCategoriesMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hostelId;

    @Column(nullable = false)
    private Long categoryId;
}
