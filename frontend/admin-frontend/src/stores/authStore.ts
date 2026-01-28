import { create } from "zustand";
import { persist } from "zustand/middleware";

export interface User {
  userId: number;
  username: string;
  email: string;
  accountStatus: "APPROVED" | "PENDING" | "REJECTED";
}

export interface AuthStoreType {
  user: User | null;
  accessToken: string | null;
  refreshToken: string | null;
  setAuth: (user: User, accessToken: string, refreshToken: string) => void;
  setAccessToken: (accessToken: string) => void;
  logout: () => void;
  isAuthenticated: () => boolean;
  isAdmin: () => boolean;
}

export const useAuthStore = create<AuthStoreType>()(
  persist(
    (set, get) => ({
      user: null,
      accessToken: null,
      refreshToken: null,

      setAuth: (user, accessToken, refreshToken) =>
        set({ user, accessToken, refreshToken }),

      setAccessToken: (accessToken) => set({ accessToken }),

      logout: () => set({ user: null, accessToken: null, refreshToken: null }),

      isAuthenticated: () => !!get().accessToken,

      isAdmin: () => get().user?.userId === -1,
    }),
    {
      name: "auth-storage",
      version: 1,
    },
  ),
);
