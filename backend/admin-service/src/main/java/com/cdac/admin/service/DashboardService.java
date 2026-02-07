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

        
        try {
            long pendingUsers = authServiceClient.getPendingUser().size();

            long pendingFoods = foodServiceClient.getPendingFood().size();

            long pendingHostels = hostelServiceClient.getPendingHostel().size();

            
            long pendingFoodCategories = 0;

           
            long pendingHostelCategories = 0;


            long totalSuggestions = suggestionServiceClient
                    .getSuggestions(0, Integer.MAX_VALUE)
                    .size();


            DashboardStatsDto stats = new DashboardStatsDto(
                    pendingUsers,
                    pendingFoods,
                    pendingHostels,
                    pendingFoodCategories,
                    pendingHostelCategories,
                    totalSuggestions
            );
            

            return stats;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch dashboard stats", e);
        }
    }

}
