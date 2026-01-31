import client from '../client';
import type {
  Hostel,
  HostelCategory,
  CreateHostelRequest,
  CreateHostelCategoryRequest,
  PageResponse,
  PageRequest,
  HostelFilters,
} from '@/types';

/**
 * Hostel service
 * Handles hostels and hostel categories
 */
export const hostelService = {
  // ========== Hostels ==========

  /**
   * Get all approved hostels (public)
   * Supports filtering, pagination, and search
   */
  async getAllHostels(
    filters?: HostelFilters,
    pagination?: PageRequest
  ): Promise<PageResponse<Hostel>> {
    const params = {
      ...pagination,
      ...filters,
    };

    const response = await client.get<PageResponse<Hostel>>('/api/hostel/hostels', { params });
    return response.data;
  },

  /**
   * Get hostel by ID
   */
  async getHostelById(id: number): Promise<Hostel> {
    const response = await client.get<Hostel>(`/api/hostel/hostels/${id}`);
    return response.data;
  },

  /**
   * Create new hostel submission
   * Requires authentication
   */
  async createHostel(data: CreateHostelRequest): Promise<Hostel> {
    const response = await client.post<Hostel>('/api/hostel/hostels', data);
    return response.data;
  },

  // ========== Categories ==========

  /**
   * Get all approved hostel categories (public)
   */
  async getApprovedCategories(): Promise<HostelCategory[]> {
    const response = await client.get<HostelCategory[]>('/api/hostel/categories');
    return response.data;
  },

  /**
   * Get category by ID
   */
  async getCategoryById(id: number): Promise<HostelCategory> {
    const response = await client.get<HostelCategory>(`/api/hostel/categories/${id}`);
    return response.data;
  },

  /**
   * Create new category suggestion
   * Requires authentication
   * Category will be in PENDING status until admin approval
   */
  async createCategory(data: CreateHostelCategoryRequest): Promise<HostelCategory> {
    const response = await client.post<HostelCategory>('/api/hostel/categories', data);
    return response.data;
  },
};
