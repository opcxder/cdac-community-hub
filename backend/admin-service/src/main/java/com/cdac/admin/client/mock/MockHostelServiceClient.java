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
public class MockHostelServiceClient  implements HostelServiceClient {

private static final Logger log = LoggerFactory.getLogger(MockHostelServiceClient.class);
	
	

	@Override
	public List<PendingHostelDto> getPendingHostel() {
		log.info("Mock: Fetching the pending hostel" );
		List<PendingHostelDto> hostel = new ArrayList<>();
		hostel.add(new PendingHostelDto(1L , "DhanaLaxmi PG" , "housing Board" , "PENDING"));
		hostel.add(new PendingHostelDto(2L , "Colnal Joshi PG" , "housing Board" ,"PENDING"));
		
		return hostel;
	}

	@Override
	public void approveHostel(Long hostelId) {
		log.info("Mock : Approving hostel id: {} ", hostelId );
		
	}

	@Override
	public void rejectHostel(Long hostelId, String reason) {
		log.info("Mock: rejecting the hostel with id {} for {}" , hostelId , reason);
		
	}

	@Override
	public List<PendingCategoryDto> getPendingCategories() {
		log.info("Mock: fetching pending hostel categories");
		
		List<PendingCategoryDto> pendingCategory =  new ArrayList<>();
		pendingCategory.add(new PendingCategoryDto(1L ,"Dhanlaxmi" , "PENDING"));
		pendingCategory.add(new PendingCategoryDto(2L ,"VEKATSH" , "PENDING"));
		
		return pendingCategory;
	}

	@Override
	public void approveCategory(Long categoryId) {
		log.info("Mock: approved category {}", categoryId);
		
	}
   
	
	 
	
}
