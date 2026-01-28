package com.cdac.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminHealthController {
   
	@GetMapping("/health")
	public String getHealth() {
		return "Admin service is started";
	}
}
