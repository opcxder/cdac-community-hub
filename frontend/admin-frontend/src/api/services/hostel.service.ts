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

export const hostelService = {
 
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

  async getHostelById(id: number): Promise<Hostel> {
    const response = await client.get<Hostel>(`/api/hostel/hostels/${id}`);
    return response.data;
  },

  
  async createHostel(data: CreateHostelRequest): Promise<Hostel> {
    const response = await client.post<Hostel>('/api/hostel/hostels', data);
    return response.data;
  },

 
  async getApprovedCategories(): Promise<HostelCategory[]> {
    const response = await client.get<HostelCategory[]>('/api/hostel/categories');
    return response.data;
  },

  async getCategoryById(id: number): Promise<HostelCategory> {
    const response = await client.get<HostelCategory>(`/api/hostel/categories/${id}`);
    return response.data;
  },

  async createCategory(data: CreateHostelCategoryRequest): Promise<HostelCategory> {
    const response = await client.post<HostelCategory>('/api/hostel/categories', data);
    return response.data;
  },
};
