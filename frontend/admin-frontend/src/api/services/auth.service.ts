import client from '../client';
import type {
  LoginRequest,
  SignupRequest,
  AuthResponse,
  User,
} from '@/types';

/**
 * Authentication service
 * Handles login, signup, and user session management
 */
export const authService = {
  /**
   * Login user with email and password
   */
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response = await client.post<AuthResponse>('/api/auth/login', credentials);
    return response.data;
  },

  /**
   * Register new user account
   * Account will be in PENDING status until admin approval
   */
  async signup(data: SignupRequest): Promise<void> {
    await client.post('/api/auth/register', data);
  },

  /**
   * Logout current user
   */
  async logout(): Promise<void> {
    await client.post('/api/auth/logout');
  },

  /**
   * Get current authenticated user details
   */
  async getCurrentUser(): Promise<User> {
    const response = await client.get<User>('/api/auth/me');
    return response.data;
  },

  /**
   * Refresh access token using refresh token
   */
  async refreshToken(refreshToken: string): Promise<{ accessToken: string }> {
    const response = await client.post<{ accessToken: string }>('/api/auth/refresh-token', {
      refreshToken,
    });
    return response.data;
  },
};
