package com.cdac.admin.client;

import java.util.List;


import com.cdac.admin.dto.PendingUserDto;

public interface AuthServiceClient {
   
	List<PendingUserDto> getPendingUser();
	void approveUser (Long userId);
	void rejectUser(Long userId , String message);
}
