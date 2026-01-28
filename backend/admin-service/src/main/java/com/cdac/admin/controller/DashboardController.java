package com.cdac.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.admin.dto.DashboardStatsDto;
import com.cdac.admin.service.DashboardService;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	 @GetMapping
	    public ResponseEntity<DashboardStatsDto> getDashboard() {
	        DashboardStatsDto stats = dashboardService.getDashboardStats();
	        return ResponseEntity.ok(stats);
	    }
}