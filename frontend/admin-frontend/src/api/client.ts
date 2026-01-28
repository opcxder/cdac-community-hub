import axios, { AxiosError, AxiosHeaders } from "axios";
import type { AxiosInstance, InternalAxiosRequestConfig } from "axios";
import { useAuthStore } from "../stores/authStore";

interface RetryAxiosRequestConfig extends InternalAxiosRequestConfig {
  _retry?: boolean;
}

// Single-flight refresh promise
let refreshPromise: Promise<string> | null = null;

// Axios instance
const client: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_GATEWAY_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Request interceptor: attach access token
client.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const authStore = useAuthStore.getState();
    const token = authStore.accessToken;

    if (token) {
      config.headers = new AxiosHeaders(config.headers);
      config.headers.set("Authorization", `Bearer ${token}`);
    }

    return config;
  },
  (error) => Promise.reject(error),
);

// Response interceptor: handle 401 → refresh token
client.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as RetryAxiosRequestConfig | undefined;

    if (
      error.response?.status === 401 &&
      originalRequest &&
      !originalRequest._retry &&
      !originalRequest.url?.includes("/auth/refresh-token")
    ) {
      originalRequest._retry = true;

      const authStore = useAuthStore.getState();

      if (!authStore.refreshToken) {
        authStore.logout();
        return Promise.reject(error);
      }

      // Single-flight refresh
      if (!refreshPromise) {
        refreshPromise = axios
          .post<{ accessToken: string }>(
            `${import.meta.env.VITE_API_GATEWAY_URL}/api/auth/refresh-token`,
            { refreshToken: authStore.refreshToken },
            { timeout: 5000 },
          )
          .then((res) => {
            authStore.setAccessToken(res.data.accessToken);
            return res.data.accessToken;
          })
          .finally(() => {
            refreshPromise = null;
          });
      }

      try {
        const newToken = await refreshPromise;

        if (originalRequest.headers) {
          originalRequest.headers = new AxiosHeaders(originalRequest.headers);
          originalRequest.headers.set("Authorization", `Bearer ${newToken}`);
        }

        return client(originalRequest);
      } catch (refreshError) {
        authStore.logout();
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  },
);

export default client;
