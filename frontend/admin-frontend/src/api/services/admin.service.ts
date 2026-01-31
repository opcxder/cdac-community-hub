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
  PageRequest,
  PageResponse,
} from '@/types';

/**
 * Admin service
 * Handles admin operations for approvals and moderation
 */
export const adminService = {
  // ========== Dashboard ==========

  /**
   * Get admin dashboard statistics
   */
  async getDashboardStats(): Promise<DashboardStats> {
    const response = await client.get<DashboardStats>('/api/admin/dashboard/stats');
    return response.data;
  },

  // ========== User Management ==========

  /**
   * Get all pending user registrations
   */
  async getPendingUsers(): Promise<User[]> {
    const response = await client.get<User[]>('/api/admin/users/pending');
    return response.data;
  },

  /**
   * Approve user registration
   */
  async approveUser(userId: number): Promise<void> {
    await client.post(`/api/admin/users/${userId}/approve`);
  },

  /**
   * Reject user registration with reason
   */
  async rejectUser(userId: number, reason: string): Promise<void> {
    await client.post(`/api/admin/users/${userId}/reject`, { reason } as RejectRequest);
  },

  // ========== Food Management ==========

  /**
   * Get all pending food place submissions
   */
  async getPendingFood(): Promise<FoodPlace[]> {
    const response = await client.get<FoodPlace[]>('/api/admin/foods/pending');
    return response.data;
  },

  /**
   * Approve food place submission
   */
  async approveFood(placeId: number): Promise<void> {
    await client.post(`/api/admin/foods/${placeId}/approve`);
  },

  /**
   * Reject food place submission with reason
   */
  async rejectFood(placeId: number, reason: string): Promise<void> {
    await client.post(`/api/admin/foods/${placeId}/reject`, { reason } as RejectRequest);
  },

  /**
   * Get all pending food categories
   */
  async getPendingFoodCategories(): Promise<FoodCategory[]> {
    const response = await client.get<FoodCategory[]>('/api/admin/foods/categories/pending');
    return response.data;
  },

  /**
   * Approve food category
   */
  async approveFoodCategory(categoryId: number): Promise<void> {
    await client.post(`/api/admin/foods/categories/${categoryId}/approve`);
  },

  /**
   * Reject food category with reason
   */
  async rejectFoodCategory(categoryId: number, reason: string): Promise<void> {
    await client.post(`/api/admin/foods/categories/${categoryId}/reject`, { reason } as RejectRequest);
  },

  // ========== Hostel Management ==========

  /**
   * Get all pending hostel submissions
   */
  async getPendingHostels(): Promise<Hostel[]> {
    const response = await client.get<Hostel[]>('/api/admin/hostels/pending');
    return response.data;
  },

  /**
   * Approve hostel submission
   */
  async approveHostel(hostelId: number): Promise<void> {
    await client.post(`/api/admin/hostels/${hostelId}/approve`);
  },

  /**
   * Reject hostel submission with reason
   */
  async rejectHostel(hostelId: number, reason: string): Promise<void> {
    await client.post(`/api/admin/hostels/${hostelId}/reject`, { reason } as RejectRequest);
  },

  /**
   * Get all pending hostel categories
   */
  async getPendingHostelCategories(): Promise<HostelCategory[]> {
    const response = await client.get<HostelCategory[]>('/api/admin/hostels/categories/pending');
    return response.data;
  },

  /**
   * Approve hostel category
   */
  async approveHostelCategory(categoryId: number): Promise<void> {
    await client.post(`/api/admin/hostels/categories/${categoryId}/approve`);
  },

  /**
   * Reject hostel category with reason
   */
  async rejectHostelCategory(categoryId: number, reason: string): Promise<void> {
    await client.post(`/api/admin/hostels/categories/${categoryId}/reject`, { reason } as RejectRequest);
  },

  // ========== Suggestions ==========

  /**
   * Get all suggestions (with pagination - only endpoint that supports it)
   */
  async getAllSuggestions(params?: PageRequest): Promise<PageResponse<Suggestion>> {
    const response = await client.get<PageResponse<Suggestion>>('/api/admin/suggestions', { params });
    return response.data;
  },

  /**
   * Get suggestions by category
   */
  async getSuggestionsByCategory(category: 'FOOD' | 'HOSTEL' | 'GENERAL'): Promise<Suggestion[]> {
    const response = await client.get<Suggestion[]>(`/api/admin/suggestions/category/${category}`);
    return response.data;
  },
};
