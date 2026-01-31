package com.cdac.hostel.model;

import jakarta.persistence.*;

/**
 * Entity representing room types available in a hostel.
 * A hostel can offer multiple room capacities (single, double, triple, etc.)
 */
@Entity
@Table(name = "hostel_room_types")
public class HostelRoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hostelId;

    @Column(nullable = false)
    private Integer capacity; // 1 for single, 2 for double, 3 for triple, etc.

    public HostelRoomType() {
    }

    public HostelRoomType(Long hostelId, Integer capacity) {
        this.hostelId = hostelId;
        this.capacity = capacity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHostelId() {
        return hostelId;
    }

    public void setHostelId(Long hostelId) {
        this.hostelId = hostelId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
