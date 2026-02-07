import client from '../client';
import type {
  FoodPlace,
  FoodCategory,
  CreateFoodPlaceRequest,
  CreateFoodCategoryRequest,
  PageResponse,
  PageRequest,
  FoodFilters,
} from '@/types';


export const foodService = {
 
  async getAllFoodPlaces(
    filters?: FoodFilters,
    pagination?: PageRequest
  ): Promise<PageResponse<FoodPlace>> {
    const params = {
      ...pagination,
      ...filters,
      categoryIds: filters?.categoryIds?.join(','),
      priceRange: filters?.priceRange?.join(','),
    };

    const response = await client.get<PageResponse<FoodPlace>>('/api/food/places', { params });
    return response.data;
  },

 
  async getFoodPlaceById(id: number): Promise<FoodPlace> {
    const response = await client.get<FoodPlace>(`/api/food/places/${id}`);
    return response.data;
  },

  async createFoodPlace(data: CreateFoodPlaceRequest): Promise<FoodPlace> {
    const response = await client.post<FoodPlace>('/api/food/places', data);
    return response.data;
  },

  async uploadImages(files: File[]): Promise<string[]> {
    const formData = new FormData();
    files.forEach((file) => formData.append('images', file));

    const response = await client.post<string[]>('/api/food/images/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  async getApprovedCategories(): Promise<FoodCategory[]> {
    const response = await client.get<FoodCategory[]>('/api/food/categories');
    return response.data;
  },

  async getCategoryById(id: number): Promise<FoodCategory> {
    const response = await client.get<FoodCategory>(`/api/food/categories/${id}`);
    return response.data;
  },

  async createCategory(data: CreateFoodCategoryRequest): Promise<FoodCategory> {
    const response = await client.post<FoodCategory>('/api/food/categories', data);
    return response.data;
  },
};
