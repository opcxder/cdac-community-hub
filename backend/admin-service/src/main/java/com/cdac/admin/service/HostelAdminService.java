package com.cdac.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cdac.admin.client.HostelServiceClient;
import com.cdac.admin.dto.PendingCategoryDto;
import com.cdac.admin.dto.PendingHostelDto;

@Service
public class HostelAdminService {

	private final HostelServiceClient hostelServiceClient;

	public HostelAdminService(HostelServiceClient hostelServiceClient) {
		this.hostelServiceClient = hostelServiceClient;
	}

	public List<PendingHostelDto> getPendingHostels() {
		return hostelServiceClient.getPendingHostel();
	}

	public void approveHostel(Long hostelId) {
		hostelServiceClient.approveHostel(hostelId);
	}

	public void rejectHostel(Long hostelId, String message) {
		hostelServiceClient.rejectHostel(hostelId, message);
	}

	public List<PendingCategoryDto> getPendingCategories() {
		return hostelServiceClient.getPendingCategories();
	}

	public void approveCategory(Long categoryId) {
		if (categoryId == null || categoryId <= 0) {
			throw new IllegalArgumentException("Invalid category id");
		}
		hostelServiceClient.approveCategory(categoryId);
	}

	public void rejectCategory(Long categoryId, String reason) {
		if (categoryId == null || categoryId <= 0) {
			throw new IllegalArgumentException("Invalid category id");
		}
		if (reason == null || reason.trim().isEmpty()) {
			throw new IllegalArgumentException("Rejection reason is required");
		}
		hostelServiceClient.rejectCategory(categoryId, reason);
	}
}
