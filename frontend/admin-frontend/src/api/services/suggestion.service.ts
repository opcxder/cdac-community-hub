import client from '../client';


import type {
  Suggestion,
  CreateSuggestionRequest,
} from '@/types';

export const suggestionService = {
  
  async createSuggestion(data: CreateSuggestionRequest): Promise<Suggestion> {
    const response = await client.post<Suggestion>('/api/suggestion/suggestions', data);
    return response.data;
  },

  async getMySuggestions(): Promise<Suggestion[]> {
    const response = await client.get<Suggestion[]>('/api/suggestion/suggestions/my');
    return response.data;
  },
};
