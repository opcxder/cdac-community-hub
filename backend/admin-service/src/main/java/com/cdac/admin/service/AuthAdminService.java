package com.cdac.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cdac.admin.client.AuthServiceClient;
import com.cdac.admin.dto.PendingUserDto;

@Service
public class AuthAdminService {
      
	//injecting the auth client dependency
	private AuthServiceClient authServiceClient;
	public AuthAdminService(AuthServiceClient authServiceClient) {
		this.authServiceClient = authServiceClient;
	}
	
	
	public  List<PendingUserDto> getPendingUsers() {
		List<PendingUserDto> users = authServiceClient.getPendingUser();
		System.out.println("📊 [ADMIN-SERVICE] Received " + users.size() + " pending users from auth-service");
		if (!users.isEmpty()) {
			PendingUserDto sample = users.get(0);
			System.out.println("📊 [ADMIN-SERVICE] Sample user: userId=" + sample.getUserId() + 
				", username=" + sample.getUsername() + 
				", email=" + sample.getEmail() + 
				", phone=" + sample.getPhone() + 
				", accountStatus=" + sample.getAccountStatus() + 
				", createdAt=" + sample.getCreatedAt());
		}
		return users;
	}
	
	
	public void approveUser(Long userId) {
		 authServiceClient.approveUser(userId);
	}
	
	public void rejectUser(Long userId , String message) {
		authServiceClient.rejectUser(userId, message);
	}
	
	
}
