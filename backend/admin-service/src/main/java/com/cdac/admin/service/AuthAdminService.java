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
		return authServiceClient.getPendingUser();
	}
	
	
	public void approveUser(Long userId) {
		 authServiceClient.approveUser(userId);
	}
	
	public void rejectUser(Long userId , String message) {
		authServiceClient.rejectUser(userId, message);
	}
	
	
}
