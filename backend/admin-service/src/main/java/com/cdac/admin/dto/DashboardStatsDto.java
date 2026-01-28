package com.cdac.admin.dto;

public class DashboardStatsDto {

	
	 private long pendingUsers;
	    private long pendingFoods;
	    private long pendingHostels;
	    private long pendingFoodCategories;
	    private long pendingHostelCategories;
	    private long totalSuggestions;
		
	    public DashboardStatsDto() {
			super();
		}

		public DashboardStatsDto(long pendingUsers, long pendingFoods, long pendingHostels, long pendingFoodCategories,
				long pendingHostelCategories, long totalSuggestions) {
			super();
			this.pendingUsers = pendingUsers;
			this.pendingFoods = pendingFoods;
			this.pendingHostels = pendingHostels;
			this.pendingFoodCategories = pendingFoodCategories;
			this.pendingHostelCategories = pendingHostelCategories;
			this.totalSuggestions = totalSuggestions;
		}

		public long getPendingUsers() {
			return pendingUsers;
		}

		public void setPendingUsers(long pendingUsers) {
			this.pendingUsers = pendingUsers;
		}

		public long getPendingFoods() {
			return pendingFoods;
		}

		public void setPendingFoods(long pendingFoods) {
			this.pendingFoods = pendingFoods;
		}

		public long getPendingHostels() {
			return pendingHostels;
		}

		public void setPendingHostels(long pendingHostels) {
			this.pendingHostels = pendingHostels;
		}

		public long getPendingFoodCategories() {
			return pendingFoodCategories;
		}

		public void setPendingFoodCategories(long pendingFoodCategories) {
			this.pendingFoodCategories = pendingFoodCategories;
		}

		public long getPendingHostelCategories() {
			return pendingHostelCategories;
		}

		public void setPendingHostelCategories(long pendingHostelCategories) {
			this.pendingHostelCategories = pendingHostelCategories;
		}

		public long getTotalSuggestions() {
			return totalSuggestions;
		}

		public void setTotalSuggestions(long totalSuggestions) {
			this.totalSuggestions = totalSuggestions;
		}
	    
	    
		
		
	    
	    
}
	
	

