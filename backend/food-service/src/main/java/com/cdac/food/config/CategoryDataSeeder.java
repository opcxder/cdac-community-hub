package com.cdac.food.config;

import com.cdac.food.model.ApprovalStatus;
import com.cdac.food.model.FoodCategory;
import com.cdac.food.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Data seeder for predefined food categories.
 * Seeds the database with common food categories on application startup.
 */
@Configuration
public class CategoryDataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(CategoryDataSeeder.class);

    /**
     * Predefined food categories that will be seeded on startup.
     * These are created by the system (userId = -1 represents system/admin).
     * Categories focus on CUISINE TYPE and DINING STYLE (price range is a separate
     * field).
     */
    private static final List<String> PREDEFINED_CATEGORIES = Arrays.asList(
            // Indian Cuisines
            "North Indian",
            "South Indian",
            "Biryani",
            "Street Food",
            "Vegetarian",

            // International Cuisines
            "Chinese",
            "Italian",
            "Mexican",
            "Continental",

            // Dining Styles
            "Fast Food",
            "Cafe",
            "Bakery",
            "Fine Dining",
            "Food Court",

            // Specific Items
            "Pizza",
            "Burger",
            "Sandwich",
            "Desserts",
            "Beverages");

    @Bean
    public CommandLineRunner seedCategories(CategoryRepository categoryRepository) {
        return args -> {
            logger.info("🌱 Starting category seeding...");

            int seededCount = 0;
            int skippedCount = 0;

            for (String categoryName : PREDEFINED_CATEGORIES) {
                // Check if category already exists
                if (categoryRepository.findByCategoryName(categoryName).isEmpty()) {
                    FoodCategory category = new FoodCategory();
                    category.setCategoryName(categoryName);
                    category.setCreatedByUserId(-1L); // System/Admin user
                    category.setStatus(ApprovalStatus.APPROVED); // Pre-approved
                    category.setCreatedAt(LocalDateTime.now());
                    category.setApprovedAt(LocalDateTime.now());

                    categoryRepository.save(category);
                    seededCount++;
                    logger.debug("✅ Seeded category: {}", categoryName);
                } else {
                    skippedCount++;
                    logger.debug("⏭️  Skipped existing category: {}", categoryName);
                }
            }

            logger.info("🎉 Category seeding complete! Seeded: {}, Skipped: {}, Total: {}",
                    seededCount, skippedCount, PREDEFINED_CATEGORIES.size());

            // Log total approved categories
            long totalApproved = categoryRepository.findByStatus(ApprovalStatus.APPROVED).size();
            logger.info("📊 Total approved categories in database: {}", totalApproved);
        };
    }
}
