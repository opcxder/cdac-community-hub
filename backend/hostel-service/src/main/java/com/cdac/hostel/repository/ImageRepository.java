package com.cdac.hostel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.hostel.model.HostelImage;


@Repository
public interface ImageRepository extends JpaRepository<HostelImage, Long> {

    
    List<HostelImage> findByHostelIdOrderByDisplayOrderAsc(Long hostelId);

   
    long countByHostelId(Long hostelId);

   
    @Transactional
    void deleteByHostelId(Long hostelId);
}
