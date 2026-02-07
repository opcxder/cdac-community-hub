import client from '../client';
import type {
  User,
  FoodPlace,
  FoodCategory,
  Hostel,
  HostelCategory,
  Suggestion,
  DashboardStats,
  RejectRequest,
 
} from '@/types';


export const adminService = {
 
  async getDashboardStats(): Promise<DashboardStats> {
    const response = await client.get<DashboardStats>('/api/admin/dashboard/stats');
    return response.data;
  },

 
  async getPendingUsers(): Promise<User[]> {
    const response = await client.get<User[]>('/api/admin/users/pending');
    return response.data;
  },


  async approveUser(userId: number): Promise<void> {
    await client.post(`/api/admin/users/${userId}/approve`);
  },

 
  async rejectUser(userId: number, reason: string): Promise<void> {
    await client.post(`/api/admin/users/${userId}/reject`, { reason } as RejectRequest);
  },

 
  async getPendingFood(): Promise<FoodPlace[]> {
    const response = await client.get<FoodPlace[]>('/api/admin/foods/pending');
    return response.data;
  },

  
  async approveFood(placeId: number): Promise<void> {
    await client.post(`/api/admin/foods/${placeId}/approve`);
  },

  
  async rejectFood(placeId: number, reason: string): Promise<void> {
    await client.post(`/api/admin/foods/${placeId}/reject`, { reason } as RejectRequest);
  },

  async getPendingFoodCategories(): Promise<FoodCategory[]> {
    const response = await client.get<FoodCategory[]>('/api/admin/foods/categories/pending');
    return response.data;
  },

  async approveFoodCategory(categoryId: number): Promise<void> {
    await client.post(`/api/admin/foods/categories/${categoryId}/approve`);
  },

  async rejectFoodCategory(categoryId: number, reason: string): Promise<void> {
    await client.post(`/api/admin/foods/categories/${categoryId}/reject`, { reason } as RejectRequest);
  },

  async getPendingHostels(): Promise<Hostel[]> {
    const response = await client.get<Hostel[]>('/api/admin/hostels/pending');
    return response.data;
  },

  async approveHostel(hostelId: number): Promise<void> {
    await client.post(`/api/admin/hostels/${hostelId}/approve`);
  },

  
  async rejectHostel(hostelId: number, reason: string): Promise<void> {
    await client.post(`/api/admin/hostels/${hostelId}/reject`, { reason } as RejectRequest);
  },

  async getPendingHostelCategories(): Promise<HostelCategory[]> {
    const response = await client.get<HostelCategory[]>('/api/admin/hostels/categories/pending');
    return response.data;
  },

  async approveHostelCategory(categoryId: number): Promise<void> {
    await client.post(`/api/admin/hostels/categories/${categoryId}/approve`);
  },

  async rejectHostelCategory(categoryId: number, reason: string): Promise<void> {
    await client.post(`/api/admin/hostels/categories/${categoryId}/reject`, { reason } as RejectRequest);
  },

  async getAllSuggestions(): Promise<Suggestion[]> {
    const response = await client.get<Suggestion[]>('/api/admin/suggestions');
    return response.data;
  },

  async getSuggestionsByCategory(category: 'FOOD' | 'HOSTEL' | 'GENERAL'): Promise<Suggestion[]> {
    const response = await client.get<Suggestion[]>(`/api/admin/suggestions/category/${category}`);
    return response.data;
  },
};
