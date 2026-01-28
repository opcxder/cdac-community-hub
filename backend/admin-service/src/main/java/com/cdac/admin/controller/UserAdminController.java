package com.cdac.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.admin.dto.PendingUserDto;
import com.cdac.admin.dto.RejectUserDto;
import com.cdac.admin.service.AuthAdminService;

@RestController
@RequestMapping("/api/admin/users")
public class UserAdminController {
    
	
	 private AuthAdminService authAdminService;
	
	 public UserAdminController(AuthAdminService authAdminService) {
		  this.authAdminService = authAdminService;
	 }
	  
	 @GetMapping("/pending")
	 public  ResponseEntity<List<PendingUserDto>> getPendingUsers(){
		List<PendingUserDto> pendingUsers =  authAdminService.getPendingUsers();
		return ResponseEntity.ok(pendingUsers);
	 }
	 
	 @PostMapping("/{id}/approve")
	 public ResponseEntity<Void> approveUser(@PathVariable("id") Long id) {
		  authAdminService.approveUser(id);
	   return ResponseEntity.ok().build();
	 }
	 
	 
	 @PostMapping("/{id}/reject")
	 public ResponseEntity<Void> rejectUser(@PathVariable("id") Long id , @RequestBody RejectUserDto rejectUserRequest ){
		 authAdminService.rejectUser(id,  rejectUserRequest.getReason());
		 return ResponseEntity.ok().build();
	 }
}
