package com.cdac.hostel.repository;

import com.cdac.hostel.model.HostelRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostelRoomTypeRepository extends JpaRepository<HostelRoomType, Long> {
    List<HostelRoomType> findByHostelId(Long hostelId);

    void deleteByHostelId(Long hostelId);
}
