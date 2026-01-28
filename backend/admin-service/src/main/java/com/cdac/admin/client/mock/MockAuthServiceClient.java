package com.cdac.admin.client.mock;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

import org.springframework.stereotype.Service;

import com.cdac.admin.client.AuthServiceClient;
import com.cdac.admin.dto.PendingUserDto;

@Service
@Profile("dev")
public class MockAuthServiceClient  implements AuthServiceClient{

	private static final Logger log =  LoggerFactory.getLogger(MockAuthServiceClient.class);
	
	@Override
	public List<PendingUserDto> getPendingUser() {
		log.info("Mock:  Fecting the pending uses" );
		 List<PendingUserDto> users = new ArrayList<>();
		 users.add(  new PendingUserDto(1L, "test1" , "test1@gmail.com" , "PENDING") );
		 users.add(  new PendingUserDto(2L, "test2" , "test2@gmail.com" , "PENDING") );
		 users.add(  new PendingUserDto(3L, "test3" , "test3@gmail.com" , "PENDING") );
		 users.add(  new PendingUserDto(4L, "test4" , "test4@gmail.com" , "PENDING") );
		 users.add(  new PendingUserDto(5L, "test5" , "test5@gmail.com" , "PENDING") );
		 users.add(  new PendingUserDto(6L, "test6" , "test6@gmail.com" , "PENDING") );
		 
		  
		return users;
	}

	@Override
	public void approveUser(Long userId) {
		log.info("Approving user with id {}" , userId);
		
	}

	@Override
	public void rejectUser(Long userId, String message) {
		log.info("Rejecting user with id {}  for {}", userId , message);
		
	}
         
}
