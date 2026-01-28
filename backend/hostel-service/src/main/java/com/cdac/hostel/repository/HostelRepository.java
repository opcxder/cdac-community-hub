package com.cdac.hostel.repository;

 
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cdac.hostel.model.Hostel;
import com.cdac.hostel.model.HostelStatus;

public interface HostelRepository extends JpaRepository<Hostel, Long> {

    List<Hostel> findByStatus(HostelStatus status);
}

