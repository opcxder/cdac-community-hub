package com.cdac.hostel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HostelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HostelServiceApplication.class, args);
		
		System.out.println("Done Server Strated hostel-service");
	}

}
