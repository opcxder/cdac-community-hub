import client from '../client';
import type {
  LoginRequest,
  SignupRequest,
  AuthResponse,
  User,
} from '@/types';

export const authService = {
  
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response = await client.post<AuthResponse>('/api/auth/login', credentials);
    return response.data;
  },

  async signup(data: SignupRequest): Promise<void> {
    await client.post('/api/auth/register', data);
  },

  async logout(): Promise<void> {
    await client.post('/api/auth/logout');
  },

  async getCurrentUser(): Promise<User> {
    const response = await client.get<User>('/api/auth/me');
    return response.data;
  },

  async refreshToken(refreshToken: string): Promise<{ accessToken: string }> {
    const response = await client.post<{ accessToken: string }>('/api/auth/refresh-token', {
      refreshToken,
    });
    return response.data;
  },
};
