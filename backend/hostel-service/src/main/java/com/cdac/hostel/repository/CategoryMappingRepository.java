package com.cdac.hostel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.hostel.model.HostelCategoriesMapping;


@Repository
public interface CategoryMappingRepository extends JpaRepository<HostelCategoriesMapping, Long> {

   
    List<HostelCategoriesMapping> findByHostelId(Long hostelId);

   
    @Transactional
    void deleteByHostelIdAndCategoryId(Long hostelId, Long categoryId);
}
