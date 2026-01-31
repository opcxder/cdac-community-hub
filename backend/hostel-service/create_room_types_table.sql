-- Create hostel_room_types table
CREATE TABLE IF NOT EXISTS hostel_room_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hostel_id BIGINT NOT NULL,
    capacity INT NOT NULL,
    FOREIGN KEY (hostel_id) REFERENCES hostels(hostel_id) ON DELETE CASCADE,
    INDEX idx_hostel_id (hostel_id)
);
