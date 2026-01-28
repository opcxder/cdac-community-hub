package com.cdac.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.admin.dto.PendingCategoryDto;
import com.cdac.admin.dto.PendingHostelDto;
import com.cdac.admin.dto.RejectHostelDto;
import com.cdac.admin.service.HostelAdminService;

@RestController
@RequestMapping("/api/admin/hostels")
public class HostelAdminController {
  
	 private final HostelAdminService hostelAdminService;
	 
	 public HostelAdminController ( HostelAdminService hostelAdminService) {
		 this.hostelAdminService = hostelAdminService;
	 }
	 
	 @GetMapping("/pending")
	 public ResponseEntity< List<PendingHostelDto>> getPendingHostels(){
		 List<PendingHostelDto> pendingHostels =   hostelAdminService.getPendingHostels();
		 return ResponseEntity.ok( pendingHostels);
	 }
	 
	 
	 @PostMapping("/{id}/approve")
	 public ResponseEntity<Void> approveHostel( @PathVariable("id") Long hostelId ) {
		 hostelAdminService.approveHostel(hostelId);
	  return ResponseEntity.ok().build();
	 }
	 
	 @PostMapping("/{id}/reject")
	 public ResponseEntity<Void>  rejectHostel(@PathVariable("id") Long id , @RequestBody RejectHostelDto rejectHostel  )  {
		 hostelAdminService.rejectHostel(id, rejectHostel.getRejectReason());
		  return ResponseEntity.ok().build();
	 }
	 
	 @GetMapping("/categories/pending")
	 public ResponseEntity<List<PendingCategoryDto>> getPendingCategories() {
		 return ResponseEntity.ok(hostelAdminService.getPendingCategories());
		 
	 }
	 
	 
	 @PostMapping("/categories/{id}/approve")
	 public ResponseEntity<Void> approveCategory(@PathVariable("id") Long categoryId) {
		 hostelAdminService.approveCategory(categoryId);
		 return  ResponseEntity.ok().build();
	 }
	 
	 
	 
	
}
