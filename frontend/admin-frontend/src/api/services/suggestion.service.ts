import client from '../client';
import type {
  Suggestion,
  CreateSuggestionRequest,
} from '@/types';

/**
 * Suggestion service
 * Handles user suggestions for improvements
 */
export const suggestionService = {
  /**
   * Create new suggestion
   * Requires authentication
   */
  async createSuggestion(data: CreateSuggestionRequest): Promise<Suggestion> {
    const response = await client.post<Suggestion>('/api/suggestion/suggestions', data);
    return response.data;
  },

  /**
   * Get all suggestions by current user
   * Requires authentication
   */
  async getMySuggestions(): Promise<Suggestion[]> {
    const response = await client.get<Suggestion[]>('/api/suggestion/suggestions/my');
    return response.data;
  },
};
