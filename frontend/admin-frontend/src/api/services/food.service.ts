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

/**
 * Food service
 * Handles food places and categories
 */
export const foodService = {
  // ========== Food Places ==========

  /**
   * Get all approved food places (public)
   * Supports filtering, pagination, and search
   */
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

  /**
   * Get food place by ID
   */
  async getFoodPlaceById(id: number): Promise<FoodPlace> {
    const response = await client.get<FoodPlace>(`/api/food/places/${id}`);
    return response.data;
  },

  /**
   * Create new food place submission
   * Requires authentication
   */
  async createFoodPlace(data: CreateFoodPlaceRequest): Promise<FoodPlace> {
    const response = await client.post<FoodPlace>('/api/food/places', data);
    return response.data;
  },

  /**
   * Upload images for food place
   * Returns array of image URLs
   */
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

  // ========== Categories ==========

  /**
   * Get all approved food categories (public)
   */
  async getApprovedCategories(): Promise<FoodCategory[]> {
    const response = await client.get<FoodCategory[]>('/api/food/categories');
    return response.data;
  },

  /**
   * Get category by ID
   */
  async getCategoryById(id: number): Promise<FoodCategory> {
    const response = await client.get<FoodCategory>(`/api/food/categories/${id}`);
    return response.data;
  },

  /**
   * Create new category suggestion
   * Requires authentication
   * Category will be in PENDING status until admin approval
   */
  async createCategory(data: CreateFoodCategoryRequest): Promise<FoodCategory> {
    const response = await client.post<FoodCategory>('/api/food/categories', data);
    return response.data;
  },
};
