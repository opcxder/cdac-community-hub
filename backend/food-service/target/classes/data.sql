-- Seed data for food categories (approved by default for testing)
INSERT INTO food_categories (category_name, status, created_at, approved_at) VALUES   
('Breakfast', 'APPROVED', NOW(), NOW()),
('Lunch', 'APPROVED', NOW(), NOW()),
('Dinner', 'APPROVED', NOW(), NOW()),
('Tea/Coffee', 'APPROVED', NOW(), NOW()),
('Snacks', 'APPROVED', NOW(), NOW()),
('Biryani', 'APPROVED', NOW(), NOW()),
('All-rounder', 'APPROVED', NOW(), NOW());
