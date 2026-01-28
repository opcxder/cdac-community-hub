package com.cdac.hostel.service;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdac.hostel.client.AuthServiceClient;
import com.cdac.hostel.model.CategoryStatus;
import com.cdac.hostel.model.HostelCategory;
import com.cdac.hostel.repository.CategoryRepository;
import com.cdac.hostel.exception.DuplicateResourceException;
import com.cdac.hostel.exception.ResourceNotFoundException;

/**
 * Service layer for hostel category management.
 * Handles category creation, retrieval, and admin approval workflows.
 * Categories allow dynamic classification of hostels (e.g., PG, Boys Only, Co-living).
 */
@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthServiceClient authClient;

    /**
     * Creates a new category submission.
     * Validates user existence and checks for duplicate category names.
     * New categories start in PENDING status and require admin approval.
     *
     * @param categoryName The name of the category to create
     * @param userId The ID of the user creating the category
     * @return The created category entity
     * @throws RuntimeException if user not found or category name already exists
     */
    public HostelCategory createCategory(String categoryName, Long userId) {
        logger.info("Creating new category: name={}, createdBy={}", categoryName, userId);

        // Validate user exists
        if (!authClient.userExists(userId)) {
            logger.error("Category creation failed - User not found: userId={}", userId);
            throw new ResourceNotFoundException("User not found");
        }

        // Check for duplicate category name
        categoryRepository.findByCategoryName(categoryName).ifPresent(existing -> {
            logger.warn("Category creation failed - Duplicate name: name={}, existingId={}", 
                       categoryName, existing.getCategoryId());
            throw new DuplicateResourceException("Category with this name already exists");
        });

        // Create new category
        HostelCategory category = new HostelCategory();
        category.setCategoryName(categoryName);
        category.setCreatedByUserId(userId);
        category.setStatus(CategoryStatus.PENDING);

        HostelCategory savedCategory = categoryRepository.save(category);
        logger.info("Category created successfully: categoryId={}, name={}, status={}", 
                    savedCategory.getCategoryId(), savedCategory.getCategoryName(), 
                    savedCategory.getStatus());

        return savedCategory;
    }

    /**
     * Retrieves all approved categories visible to public users.
     * Only approved categories can be used when creating hostels.
     *
     * @return List of approved categories
     */
    public List<HostelCategory> getApprovedCategories() {
        logger.debug("Fetching all approved categories");
        List<HostelCategory> categories = categoryRepository.findByStatus(CategoryStatus.APPROVED);
        logger.info("Retrieved {} approved categories", categories.size());
        return categories;
    }

    /**
     * Retrieves all pending categories awaiting admin approval.
     * Used by admin interface to review category submissions.
     *
     * @return List of pending categories
     */
    public List<HostelCategory> getPendingCategories() {
        logger.debug("Fetching all pending categories for admin review");
        List<HostelCategory> categories = categoryRepository.findByStatus(CategoryStatus.PENDING);
        logger.info("Retrieved {} pending categories", categories.size());
        return categories;
    }

    /**
     * Retrieves a single category by its ID.
     *
     * @param categoryId The ID of the category
     * @return The category entity
     * @throws RuntimeException if category not found
     */
    public HostelCategory getCategoryById(Long categoryId) {
        logger.debug("Fetching category by ID: categoryId={}", categoryId);
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    logger.error("Category not found: categoryId={}", categoryId);
                    throw new ResourceNotFoundException("Category", categoryId);
                });
    }

    /**
     * Approves a pending category, making it available for use.
     * Sets the approval timestamp and changes status to APPROVED.
     *
     * @param categoryId The ID of the category to approve
     * @return The approved category entity
     * @throws RuntimeException if category not found
     */
    public HostelCategory approveCategory(Long categoryId) {
        logger.info("Approving category: categoryId={}", categoryId);

        HostelCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    logger.error("Category approval failed - Category not found: categoryId={}", 
                                categoryId);
                    throw new ResourceNotFoundException("Category", categoryId);
                });

        category.setStatus(CategoryStatus.APPROVED);
        category.setApprovedAt(new Timestamp(System.currentTimeMillis()));

        HostelCategory approvedCategory = categoryRepository.save(category);
        logger.info("Category approved successfully: categoryId={}, name={}", 
                    approvedCategory.getCategoryId(), approvedCategory.getCategoryName());

        return approvedCategory;
    }

    /**
     * Rejects a pending category.
     * Rejected categories cannot be used for hostel classification.
     *
     * @param categoryId The ID of the category to reject
     * @return The rejected category entity
     * @throws RuntimeException if category not found
     */
    public HostelCategory rejectCategory(Long categoryId) {
        logger.info("Rejecting category: categoryId={}", categoryId);

        HostelCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    logger.error("Category rejection failed - Category not found: categoryId={}", 
                                categoryId);
                    throw new ResourceNotFoundException("Category", categoryId);
                });

        category.setStatus(CategoryStatus.REJECTED);

        HostelCategory rejectedCategory = categoryRepository.save(category);
        logger.info("Category rejected successfully: categoryId={}, name={}", 
                    rejectedCategory.getCategoryId(), rejectedCategory.getCategoryName());

        return rejectedCategory;
    }
}
