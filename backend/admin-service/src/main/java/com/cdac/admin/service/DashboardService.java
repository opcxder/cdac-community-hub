package com.cdac.admin.service;

import org.springframework.stereotype.Service;


import com.cdac.admin.client.AuthServiceClient;
import com.cdac.admin.client.FoodServiceClient;
import com.cdac.admin.client.HostelServiceClient;
import com.cdac.admin.client.SuggestionServiceClient;
import com.cdac.admin.dto.DashboardStatsDto;


@Service
public class DashboardService {
	 private final AuthServiceClient authServiceClient;
	    private final FoodServiceClient foodServiceClient;
	    private final HostelServiceClient hostelServiceClient;
	    private final SuggestionServiceClient suggestionServiceClient;

	    public DashboardService(
	            AuthServiceClient authServiceClient,
	            FoodServiceClient foodServiceClient,
	            HostelServiceClient hostelServiceClient,
	            SuggestionServiceClient suggestionServiceClient) {

	        this.authServiceClient = authServiceClient;
	        this.foodServiceClient = foodServiceClient;
	        this.hostelServiceClient = hostelServiceClient;
	        this.suggestionServiceClient = suggestionServiceClient;
	    }

	    public DashboardStatsDto getDashboardStats() {
        System.out.println("📊 [DASHBOARD] Starting getDashboardStats...");
        
        try {
            System.out.println("📊 [DASHBOARD] Fetching pending users...");
            long pendingUsers = authServiceClient.getPendingUser().size();
            System.out.println("📊 [DASHBOARD] Pending users: " + pendingUsers);

            System.out.println("📊 [DASHBOARD] Fetching pending foods...");
            long pendingFoods = foodServiceClient.getPendingFood().size();
            System.out.println("📊 [DASHBOARD] Pending foods: " + pendingFoods);

            System.out.println("📊 [DASHBOARD] Fetching pending hostels...");
            long pendingHostels = hostelServiceClient.getPendingHostel().size();
            System.out.println("📊 [DASHBOARD] Pending hostels: " + pendingHostels);

            // TODO: Uncomment when category management UI is implemented
            // System.out.println("📊 [DASHBOARD] Fetching pending food categories...");
            // long pendingFoodCategories = foodServiceClient.getPendingCategories().size();
            // System.out.println("📊 [DASHBOARD] Pending food categories: " + pendingFoodCategories);
            long pendingFoodCategories = 0;

            // TODO: Uncomment when category management UI is implemented
            // System.out.println("📊 [DASHBOARD] Fetching pending hostel categories...");
            // long pendingHostelCategories = hostelServiceClient.getPendingCategories().size();
            // System.out.println("📊 [DASHBOARD] Pending hostel categories: " + pendingHostelCategories);
            long pendingHostelCategories = 0;

            System.out.println("📊 [DASHBOARD] Fetching total suggestions...");
            long totalSuggestions = suggestionServiceClient
                    .getSuggestions(0, Integer.MAX_VALUE)
                    .size();
            System.out.println("📊 [DASHBOARD] Total suggestions: " + totalSuggestions);

            DashboardStatsDto stats = new DashboardStatsDto(
                    pendingUsers,
                    pendingFoods,
                    pendingHostels,
                    pendingFoodCategories,
                    pendingHostelCategories,
                    totalSuggestions
            );
            
            System.out.println("📊 [DASHBOARD] Successfully created stats: " + stats);
            return stats;
            
        } catch (Exception e) {
            System.err.println("❌ [DASHBOARD] Error in getDashboardStats: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch dashboard stats", e);
        }
    }

}
