package com.cdac.admin.client.mock;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.cdac.admin.client.HostelServiceClient;
import com.cdac.admin.dto.PendingCategoryDto;
import com.cdac.admin.dto.PendingHostelDto;

@Service
@Profile("dev")
public class MockHostelServiceClient implements HostelServiceClient {

	private static final Logger log = LoggerFactory.getLogger(MockHostelServiceClient.class);

	@Override
	public List<PendingHostelDto> getPendingHostel() {
		log.info("Mock: Fetching the pending hostel");
		List<PendingHostelDto> hostel = new ArrayList<>();
		
		// Hostel 1: DhanaLaxmi PG
		PendingHostelDto hostel1 = new PendingHostelDto();
		hostel1.setHostelId(1L);
		hostel1.setHostelName("DhanaLaxmi PG");
		hostel1.setDescription("Comfortable PG for students with all amenities");
		hostel1.setAddress("Housing Board Colony, Aundh");
		hostel1.setCity("Pune");
		hostel1.setLocality("Aundh");
		hostel1.setLandmark("Near CDAC Campus");
		hostel1.setDistanceFromCdac("1.5 km");
		hostel1.setMonthlyRentMin(new java.math.BigDecimal("5000"));
		hostel1.setMonthlyRentMax(new java.math.BigDecimal("7000"));
		hostel1.setHasWifi(true);
		hostel1.setHasAc(false);
		hostel1.setHasMess(true);
		hostel1.setHasLaundry(true);
		hostel1.setContactPersonName("Ramesh Kumar");
		hostel1.setContactPersonPhone("9876543210");
		hostel1.setStatus("PENDING");
		hostel1.setSubmittedByUserId(201L);
		hostel1.setImageUrls(List.of("https://via.placeholder.com/400x300?text=DhanaLaxmi+PG"));
		hostel.add(hostel1);
		
		// Hostel 2: Colonel Joshi PG
		PendingHostelDto hostel2 = new PendingHostelDto();
		hostel2.setHostelId(2L);
		hostel2.setHostelName("Colonel Joshi PG");
		hostel2.setDescription("Premium PG with AC rooms and modern facilities");
		hostel2.setAddress("Housing Board Colony, Baner");
		hostel2.setCity("Pune");
		hostel2.setLocality("Baner");
		hostel2.setLandmark("Behind Cummins College");
		hostel2.setDistanceFromCdac("3 km");
		hostel2.setMonthlyRentMin(new java.math.BigDecimal("8000"));
		hostel2.setMonthlyRentMax(new java.math.BigDecimal("10000"));
		hostel2.setHasWifi(true);
		hostel2.setHasAc(true);
		hostel2.setHasMess(true);
		hostel2.setHasLaundry(false);
		hostel2.setContactPersonName("Col. Joshi");
		hostel2.setContactPersonPhone("9123456789");
		hostel2.setStatus("PENDING");
		hostel2.setSubmittedByUserId(202L);
		hostel2.setImageUrls(List.of("https://via.placeholder.com/400x300?text=Colonel+Joshi+PG"));
		hostel.add(hostel2);

		return hostel;
	}

	@Override
	public void approveHostel(Long hostelId) {
		log.info("Mock : Approving hostel id: {} ", hostelId);

	}

	@Override
	public void rejectHostel(Long hostelId, String reason) {
		log.info("Mock: rejecting the hostel with id {} for {}", hostelId, reason);

	}

	@Override
	public List<PendingCategoryDto> getPendingCategories() {
		log.info("Mock: fetching pending hostel categories");

		List<PendingCategoryDto> pendingCategory = new ArrayList<>();
		pendingCategory.add(new PendingCategoryDto(1L, "Dhanlaxmi", "PENDING"));
		pendingCategory.add(new PendingCategoryDto(2L, "VEKATSH", "PENDING"));

		return pendingCategory;
	}

	@Override
	public void approveCategory(Long categoryId) {
		log.info("Mock: approved category {}", categoryId);

	}

	@Override
	public void rejectCategory(Long categoryId, String reason) {
		log.info("Mock: rejected category {} with reason: {}", categoryId, reason);
	}

}
