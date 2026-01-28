-- ============================================
-- Hostel Service - Seed Data
-- ============================================
-- This file contains sample data for testing the Hostel Service
-- Run this after the application creates the schema
-- ============================================

-- ============================================
-- 1. HOSTEL CATEGORIES
-- ============================================
-- Sample categories for hostel classification
INSERT INTO hostel_categories (category_name, created_by_user_id, status, created_at, approved_at) VALUES
('Budget Friendly', 1, 'APPROVED', NOW(), NOW()),
('Premium', 1, 'APPROVED', NOW(), NOW()),
('Girls Only', 1, 'APPROVED', NOW(), NOW()),
('Boys Only', 1, 'APPROVED', NOW(), NOW()),
('Co-Living', 1, 'APPROVED', NOW(), NOW()),
('Near Metro', 1, 'APPROVED', NOW(), NOW()),
('AC Rooms', 1, 'APPROVED', NOW(), NOW()),
('Food Included', 1, 'APPROVED', NOW(), NOW()),
('WiFi Available', 1, 'APPROVED', NOW(), NOW()),
('Gym Facility', 1, 'PENDING', NOW(), NULL);

-- ============================================
-- 2. HOSTELS
-- ============================================
-- Sample hostels with various configurations
INSERT INTO hostels (
    hostel_name, description, address, city, locality, landmark, map_location,
    distance_from_cdac, monthly_rent_min, monthly_rent_max, room_capacity,
    has_wifi, has_ac, has_mess, has_laundry,
    contact_person_name, contact_person_phone,
    submitted_by_user_id, status, created_at, approved_at
) VALUES
-- Hostel 1: Premium hostel with many ratings
('Comfort PG', 
 'Premium hostel with all modern amenities including AC, WiFi, and gym. Located near metro station.',
 '123 MG Road', 'Pune', 'Shivajinagar', 'Near Phoenix Mall', 'https://maps.google.com/?q=18.5204,73.8567',
 2.5, 8000, 12000, 2,
 true, true, true, true,
 'Rajesh Kumar', '9876543210',
 2, 'APPROVED', DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 59 DAY)),

-- Hostel 2: Budget friendly with good ratings
('Student Haven',
 'Budget-friendly hostel perfect for students. Clean rooms with basic amenities.',
 '456 FC Road', 'Pune', 'Deccan', 'Near Fergusson College', 'https://maps.google.com/?q=18.5196,73.8354',
 3.0, 5000, 7000, 3,
 true, false, true, true,
 'Priya Sharma', '9876543211',
 3, 'APPROVED', DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 49 DAY)),

-- Hostel 3: New hostel with few ratings
('Modern Living PG',
 'Newly opened hostel with modern facilities and spacious rooms.',
 '789 Baner Road', 'Pune', 'Baner', 'Near Baner IT Park', 'https://maps.google.com/?q=18.5598,73.7811',
 5.0, 9000, 13000, 2,
 true, true, true, false,
 'Amit Patel', '9876543212',
 4, 'APPROVED', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY)),

-- Hostel 4: Girls only hostel
('Safe Stay Girls Hostel',
 'Secure hostel exclusively for girls with 24/7 security and CCTV.',
 '321 Karve Road', 'Pune', 'Kothrud', 'Near Karve Statue', 'https://maps.google.com/?q=18.5074,73.8077',
 4.0, 7000, 10000, 3,
 true, true, true, true,
 'Sunita Desai', '9876543213',
 5, 'APPROVED', DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 39 DAY)),

-- Hostel 5: Budget hostel
('Economy PG',
 'Very affordable hostel for students on a tight budget.',
 '654 Paud Road', 'Pune', 'Kothrud', 'Near Paud Phata', 'https://maps.google.com/?q=18.5018,73.8077',
 4.5, 4000, 6000, 5,
 true, false, true, false,
 'Ramesh Yadav', '9876543214',
 6, 'APPROVED', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 29 DAY)),

-- Hostel 6: Pending approval
('New Horizon PG',
 'Brand new hostel waiting for approval.',
 '987 Hinjewadi Road', 'Pune', 'Hinjewadi', 'Near Rajiv Gandhi Infotech Park', 'https://maps.google.com/?q=18.5912,73.7389',
 8.0, 10000, 15000, 2,
 true, true, true, true,
 'Vikram Singh', '9876543215',
 7, 'PENDING', DATE_SUB(NOW(), INTERVAL 2 DAY), NULL);

-- ============================================
-- 3. HOSTEL CATEGORIES MAPPING
-- ============================================
-- Map hostels to categories
INSERT INTO hostel_categories_mapping (hostel_id, category_id) VALUES
-- Comfort PG: Premium, AC Rooms, WiFi, Food Included, Near Metro
(1, 2), (1, 7), (1, 9), (1, 8), (1, 6),
-- Student Haven: Budget Friendly, WiFi, Food Included
(2, 1), (2, 9), (2, 8),
-- Modern Living PG: Premium, AC Rooms, WiFi, Near Metro
(3, 2), (3, 7), (3, 9), (3, 6),
-- Safe Stay: Girls Only, AC Rooms, WiFi, Food Included
(4, 3), (4, 7), (4, 9), (4, 8),
-- Economy PG: Budget Friendly, WiFi, Food Included
(5, 1), (5, 9), (5, 8);

-- ============================================
-- 4. HOSTEL RATINGS
-- ============================================
-- Multi-criteria ratings for hostels
-- Hostel 1 (Comfort PG): 20 ratings, high quality (avg ~4.5)
INSERT INTO hostel_ratings (
    hostel_id, user_id,
    cleanliness_rating, food_quality_rating, safety_rating, location_rating, affordability_rating,
    review_text, created_at, updated_at
) VALUES
(1, 10, 5, 5, 5, 5, 4, 'Excellent hostel! Very clean and safe.', DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 50 DAY)),
(1, 11, 5, 4, 5, 5, 4, 'Great place, highly recommended.', DATE_SUB(NOW(), INTERVAL 48 DAY), DATE_SUB(NOW(), INTERVAL 48 DAY)),
(1, 12, 4, 5, 5, 4, 4, 'Food is amazing, location is perfect.', DATE_SUB(NOW(), INTERVAL 45 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY)),
(1, 13, 5, 4, 4, 5, 3, 'Very good overall, bit expensive.', DATE_SUB(NOW(), INTERVAL 42 DAY), DATE_SUB(NOW(), INTERVAL 42 DAY)),
(1, 14, 4, 4, 5, 5, 4, 'Safe and comfortable stay.', DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 40 DAY)),
(1, 15, 5, 5, 5, 4, 4, 'Best hostel I have stayed in.', DATE_SUB(NOW(), INTERVAL 38 DAY), DATE_SUB(NOW(), INTERVAL 38 DAY)),
(1, 16, 4, 4, 4, 5, 4, 'Good location, near metro.', DATE_SUB(NOW(), INTERVAL 35 DAY), DATE_SUB(NOW(), INTERVAL 35 DAY)),
(1, 17, 5, 4, 5, 5, 3, 'Excellent facilities.', DATE_SUB(NOW(), INTERVAL 32 DAY), DATE_SUB(NOW(), INTERVAL 32 DAY)),
(1, 18, 4, 5, 4, 4, 4, 'Food quality is top-notch.', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
(1, 19, 5, 4, 5, 5, 4, 'Very clean and well-maintained.', DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 28 DAY)),
(1, 20, 4, 4, 4, 5, 4, 'Great value for money.', DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY)),
(1, 21, 5, 5, 5, 4, 4, 'Highly satisfied with everything.', DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY)),
(1, 22, 4, 4, 5, 5, 3, 'Safe environment, good location.', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY)),
(1, 23, 5, 4, 4, 5, 4, 'Comfortable and clean.', DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY)),
(1, 24, 4, 5, 5, 4, 4, 'Excellent food and service.', DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY)),
(1, 25, 5, 4, 5, 5, 4, 'Best hostel in the area.', DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY)),
(1, 26, 4, 4, 4, 5, 4, 'Good overall experience.', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY)),
(1, 27, 5, 5, 5, 5, 3, 'Perfect in every way except price.', DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY)),
(1, 28, 4, 4, 5, 4, 4, 'Very safe and secure.', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
(1, 29, 5, 4, 4, 5, 4, 'Would definitely recommend.', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),

-- Hostel 2 (Student Haven): 15 ratings, good quality (avg ~4.2)
(2, 30, 4, 4, 4, 4, 5, 'Great value for students.', DATE_SUB(NOW(), INTERVAL 45 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY)),
(2, 31, 4, 4, 4, 4, 5, 'Very affordable and clean.', DATE_SUB(NOW(), INTERVAL 42 DAY), DATE_SUB(NOW(), INTERVAL 42 DAY)),
(2, 32, 5, 4, 4, 4, 5, 'Perfect for budget-conscious students.', DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 40 DAY)),
(2, 33, 4, 3, 4, 4, 5, 'Good hostel, food could be better.', DATE_SUB(NOW(), INTERVAL 38 DAY), DATE_SUB(NOW(), INTERVAL 38 DAY)),
(2, 34, 4, 4, 5, 4, 5, 'Safe and affordable.', DATE_SUB(NOW(), INTERVAL 35 DAY), DATE_SUB(NOW(), INTERVAL 35 DAY)),
(2, 35, 5, 4, 4, 3, 5, 'Best budget option.', DATE_SUB(NOW(), INTERVAL 32 DAY), DATE_SUB(NOW(), INTERVAL 32 DAY)),
(2, 36, 4, 4, 4, 4, 4, 'Good overall experience.', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
(2, 37, 4, 3, 4, 4, 5, 'Decent place for the price.', DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 28 DAY)),
(2, 38, 5, 4, 5, 4, 5, 'Very clean and safe.', DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY)),
(2, 39, 4, 4, 4, 4, 5, 'Recommended for students.', DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY)),
(2, 40, 4, 4, 4, 3, 5, 'Good value, bit far from college.', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY)),
(2, 41, 5, 4, 4, 4, 5, 'Clean and affordable.', DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY)),
(2, 42, 4, 3, 4, 4, 5, 'Decent food, great price.', DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY)),
(2, 43, 4, 4, 5, 4, 5, 'Very safe environment.', DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY)),
(2, 44, 4, 4, 4, 4, 4, 'Good hostel overall.', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY)),

-- Hostel 3 (Modern Living): 2 ratings, excellent but new (avg ~5.0)
(3, 45, 5, 5, 5, 5, 4, 'Brand new and amazing!', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
(3, 46, 5, 5, 5, 5, 3, 'Perfect facilities, bit expensive.', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),

-- Hostel 4 (Safe Stay): 12 ratings, good (avg ~4.3)
(4, 47, 5, 4, 5, 4, 4, 'Very safe for girls.', DATE_SUB(NOW(), INTERVAL 35 DAY), DATE_SUB(NOW(), INTERVAL 35 DAY)),
(4, 48, 4, 4, 5, 4, 4, 'Excellent security.', DATE_SUB(NOW(), INTERVAL 32 DAY), DATE_SUB(NOW(), INTERVAL 32 DAY)),
(4, 49, 5, 4, 5, 4, 3, 'Safe but a bit expensive.', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
(4, 50, 4, 4, 5, 4, 4, 'Good for working women.', DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 28 DAY)),
(4, 51, 5, 4, 5, 3, 4, 'Very secure, bit far from metro.', DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY)),
(4, 52, 4, 4, 5, 4, 4, 'Parents approved!', DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY)),
(4, 53, 5, 4, 5, 4, 4, 'Best girls hostel.', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY)),
(4, 54, 4, 3, 5, 4, 4, 'Safe, food could improve.', DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY)),
(4, 55, 5, 4, 5, 4, 4, 'Highly recommended.', DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY)),
(4, 56, 4, 4, 4, 4, 4, 'Good overall.', DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY)),
(4, 57, 5, 4, 5, 4, 3, 'Very safe, worth the price.', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY)),
(4, 58, 4, 4, 5, 4, 4, 'Secure and clean.', DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY)),

-- Hostel 5 (Economy PG): 8 ratings, average (avg ~3.8)
(5, 59, 4, 3, 4, 3, 5, 'Very cheap, basic facilities.', DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY)),
(5, 60, 3, 3, 4, 3, 5, 'Good for tight budget.', DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY)),
(5, 61, 4, 4, 4, 3, 5, 'Affordable and clean.', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY)),
(5, 62, 4, 3, 3, 3, 5, 'Basic but cheap.', DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY)),
(5, 63, 3, 3, 4, 4, 5, 'You get what you pay for.', DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY)),
(5, 64, 4, 4, 4, 3, 5, 'Good value for money.', DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY)),
(5, 65, 4, 3, 4, 3, 5, 'Decent for the price.', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY)),
(5, 66, 3, 4, 4, 4, 5, 'Cheap and cheerful.', DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY));

-- ============================================
-- 5. REVIEW REPLIES
-- ============================================
-- Sample replies from hostel owners
INSERT INTO hostel_review_replies (rating_id, replied_by_user_id, reply_text, replied_at) VALUES
(1, 2, 'Thank you so much for your kind words! We are glad you enjoyed your stay.', DATE_SUB(NOW(), INTERVAL 49 DAY)),
(3, 2, 'We are happy to hear you loved our food! Thank you for the review.', DATE_SUB(NOW(), INTERVAL 44 DAY)),
(6, 2, 'Thank you! We strive to provide the best experience for our guests.', DATE_SUB(NOW(), INTERVAL 37 DAY)),
(21, 3, 'Thank you for choosing Student Haven! We appreciate your feedback.', DATE_SUB(NOW(), INTERVAL 41 DAY)),
(24, 3, 'We are working on improving our food menu. Thanks for the suggestion!', DATE_SUB(NOW(), INTERVAL 37 DAY)),
(31, 45, 'Thank you! We are glad you like our new facilities.', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(33, 5, 'Thank you for your trust! Safety is our top priority.', DATE_SUB(NOW(), INTERVAL 34 DAY)),
(38, 5, 'We appreciate your feedback and will work on improving our food quality.', DATE_SUB(NOW(), INTERVAL 17 DAY));

-- ============================================
-- SEED DATA SUMMARY
-- ============================================
-- Categories: 10 (9 approved, 1 pending)
-- Hostels: 6 (5 approved, 1 pending)
-- Ratings: 57 total
--   - Hostel 1: 20 ratings (avg ~4.5)
--   - Hostel 2: 15 ratings (avg ~4.2)
--   - Hostel 3: 2 ratings (avg ~5.0)
--   - Hostel 4: 12 ratings (avg ~4.3)
--   - Hostel 5: 8 ratings (avg ~3.8)
-- Replies: 8 total
-- ============================================
