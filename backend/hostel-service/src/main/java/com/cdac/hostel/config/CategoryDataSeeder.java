package com.cdac.hostel.config;

import com.cdac.hostel.model.CategoryStatus;
import com.cdac.hostel.model.HostelCategory;
import com.cdac.hostel.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Data seeder for predefined hostel categories.
 * Seeds the database with common hostel categories on application startup.
 */
@Configuration
public class CategoryDataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(CategoryDataSeeder.class);

    /**
     * Predefined hostel categories that will be seeded on startup.
     * These are created by the system (userId = -1 represents system/admin).
     * Categories focus on ACCOMMODATION TYPE only (amenities, gender, facilities
     * are separate form fields).
     */
    private static final List<String> PREDEFINED_CATEGORIES = Arrays.asList(
            "PG (Paying Guest)",
            "Hostel",
            "Flat",
            "Apartment",
            "Studio",
            "1 BHK",
            "2 BHK",
            "3+ BHK");

    @Bean
    public CommandLineRunner seedHostelCategories(CategoryRepository categoryRepository) {
        return args -> {
            logger.info("🏠 Starting hostel category seeding...");

            int seededCount = 0;
            int skippedCount = 0;

            for (String categoryName : PREDEFINED_CATEGORIES) {
                // Check if category already exists
                if (categoryRepository.findByCategoryName(categoryName).isEmpty()) {
                    HostelCategory category = new HostelCategory();
                    category.setCategoryName(categoryName);
                    category.setCreatedByUserId(-1L); // System/Admin user
                    category.setStatus(CategoryStatus.APPROVED); // Pre-approved

                    categoryRepository.save(category);
                    seededCount++;			
                    logger.debug("✅ Seeded hostel category: {}", categoryName);
                } else {
                    skippedCount++;
                    logger.debug("⏭️  Skipped existing hostel category: {}", categoryName);
                }
            }

            logger.info("🎉 Hostel category seeding complete! Seeded: {}, Skipped: {}, Total: {}",
                    seededCount, skippedCount, PREDEFINED_CATEGORIES.size());

            // Log total approved categories
            long totalApproved = categoryRepository.findByStatus(CategoryStatus.APPROVED).size();
            logger.info("📊 Total approved hostel categories in database: {}", totalApproved);
        };
    }
}
