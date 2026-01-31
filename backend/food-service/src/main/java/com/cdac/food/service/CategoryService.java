package com.cdac.food.service;

import com.cdac.food.dto.CategoryDTO;
import com.cdac.food.exception.ResourceNotFoundException;
import com.cdac.food.exception.UserNotFoundException;
import com.cdac.food.model.ApprovalStatus;
import com.cdac.food.model.FoodCategory;
import com.cdac.food.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for managing food categories.
 * Handles category creation, approval workflow, and retrieval.
 */
@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final AuthServiceClient authServiceClient;

    public CategoryService(CategoryRepository categoryRepository,
            AuthServiceClient authServiceClient) {
        this.categoryRepository = categoryRepository;
        this.authServiceClient = authServiceClient;
    }

    /**
     * Create a new food category.
     * Category is created with PENDING status and requires admin approval.
     * 
     * @param categoryName the name of the category
     * @param userId       the ID of the user creating the category
     * @return CategoryDTO of the created category
     * @throws RuntimeException if category name already exists
     * @throws RuntimeException if user does not exist
     */
    @Transactional
    public CategoryDTO createCategory(String categoryName, Long userId) {
        logger.info("Creating category: name={}, userId={}", categoryName, userId);

        // Validate user exists
        if (!authServiceClient.userExists(userId)) {
            logger.error("Category creation failed - user not found: userId={}", userId);
            throw new UserNotFoundException("User not found: " + userId);
        }

        // Check if category name already exists
        if (categoryRepository.findByCategoryName(categoryName).isPresent()) {
            logger.error("Category creation failed - duplicate name: {}", categoryName);
            throw new IllegalArgumentException("Category already exists: " + categoryName);
        }

        // Create new category with PENDING status
        FoodCategory category = new FoodCategory();
        category.setCategoryName(categoryName);
        category.setCreatedByUserId(userId);
        category.setStatus(ApprovalStatus.PENDING);
        category.setCreatedAt(LocalDateTime.now());

        FoodCategory saved = categoryRepository.save(category);

        logger.info("Category created successfully: categoryId={}, name={}, status={}",
                saved.getCategoryId(), saved.getCategoryName(), saved.getStatus());

        return mapToDTO(saved);
    }

    /**
     * Get all approved categories.
     * Only approved categories are visible to public users.
     * 
     * @return list of approved categories
     */
    public List<CategoryDTO> getAllApprovedCategories() {
        logger.debug("Fetching all approved categories");

        List<FoodCategory> categories = categoryRepository.findByStatus(ApprovalStatus.APPROVED);

        logger.debug("Found {} approved categories", categories.size());

        return categories.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all categories (for admin use).
     * Returns categories with all statuses.
     * 
     * @return list of all categories
     */
    public List<CategoryDTO> getAllCategories() {
        logger.debug("Fetching all categories (admin)");

        List<FoodCategory> categories = categoryRepository.findAllByOrderByCreatedAtDesc();

        logger.debug("Found {} total categories", categories.size());

        return categories.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all pending categories awaiting admin approval.
     * 
     * @return list of pending categories
     */
    public List<CategoryDTO> getPendingCategories() {
        logger.debug("Fetching pending categories for admin approval");

        List<FoodCategory> categories = categoryRepository.findByStatus(ApprovalStatus.PENDING);

        logger.info("Found {} pending categories", categories.size());

        return categories.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a category by ID.
     * 
     * @param categoryId the ID of the category
     * @return CategoryDTO
     * @throws RuntimeException if category not found
     */
    public CategoryDTO getCategoryById(Long categoryId) {
        logger.debug("Fetching category by ID: {}", categoryId);

        FoodCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    logger.error("Category not found: categoryId={}", categoryId);
                    return new ResourceNotFoundException("Category not found: " + categoryId);
                });

        return mapToDTO(category);
    }

    /**
     * Approve a pending category.
     * Sets status to APPROVED and records approval timestamp.
     * 
     * @param categoryId the ID of the category to approve
     * @return CategoryDTO of the approved category
     * @throws RuntimeException if category not found
     */
    @Transactional
    public CategoryDTO approveCategory(Long categoryId) {
        logger.info("Approving category: categoryId={}", categoryId);

        FoodCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    logger.error("Category approval failed - not found: categoryId={}", categoryId);
                    return new ResourceNotFoundException("Category not found: " + categoryId);
                });

        // Update status and approval timestamp
        category.setStatus(ApprovalStatus.APPROVED);
        category.setApprovedAt(LocalDateTime.now());

        FoodCategory saved = categoryRepository.save(category);

        logger.info("Category approved successfully: categoryId={}, name={}",
                saved.getCategoryId(), saved.getCategoryName());

        return mapToDTO(saved);
    }

    /**
     * Reject a pending category.
     * Sets status to REJECTED and stores the rejection reason.
     * 
     * @param categoryId the ID of the category to reject
     * @param reason     the reason for rejection
     * @return CategoryDTO of the rejected category
     * @throws RuntimeException if category not found
     */
    @Transactional
    public CategoryDTO rejectCategory(Long categoryId, String reason) {
        logger.info("Rejecting category: categoryId={}, reason={}", categoryId, reason);

        FoodCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    logger.error("Category rejection failed - not found: categoryId={}", categoryId);
                    return new ResourceNotFoundException("Category not found: " + categoryId);
                });

        // Update status to REJECTED and set rejection reason
        category.setStatus(ApprovalStatus.REJECTED);
        category.setRejectionReason(reason);

        FoodCategory saved = categoryRepository.save(category);

        logger.info("Category rejected successfully: categoryId={}, name={}",
                saved.getCategoryId(), saved.getCategoryName());

        return mapToDTO(saved);
    }

    /**
     * Map FoodCategory entity to CategoryDTO.
     * 
     * @param category the FoodCategory entity
     * @return CategoryDTO
     */
    private CategoryDTO mapToDTO(FoodCategory category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        dto.setStatus(category.getStatus());
        dto.setCreatedByUserId(category.getCreatedByUserId());
        return dto;
    }
}
