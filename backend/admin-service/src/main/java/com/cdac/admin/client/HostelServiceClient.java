package com.cdac.admin.client;

import java.util.List;

import com.cdac.admin.dto.PendingCategoryDto;
import com.cdac.admin.dto.PendingHostelDto;

public interface HostelServiceClient {

	
	 List<PendingHostelDto> getPendingHostel();
	 void approveHostel(Long hostelId);
	 void rejectHostel(Long hostelId, String reason);
	 
	 
	 List<PendingCategoryDto> getPendingCategories();
	    void approveCategory(Long categoryId);
	
}
