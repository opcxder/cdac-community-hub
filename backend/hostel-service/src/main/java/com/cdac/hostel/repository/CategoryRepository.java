package com.cdac.hostel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cdac.hostel.model.CategoryStatus;
import com.cdac.hostel.model.HostelCategory;


@Repository
public interface CategoryRepository extends JpaRepository<HostelCategory, Long> {

  
    List<HostelCategory> findByStatus(CategoryStatus status);

  
    Optional<HostelCategory> findByCategoryName(String categoryName);
}
