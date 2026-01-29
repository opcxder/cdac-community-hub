// User types
export interface User {
  userId: number;
  username: string;
  email: string;
  phone?: string;
  accountStatus: 'PENDING' | 'APPROVED' | 'REJECTED';
  rejectionReason?: string;
  createdAt: string;
  approvedAt?: string;
}

// Auth types
export interface LoginRequest {
  username: string; // email
  password: string;
}

export interface RegisterRequest {
  email: string;
  phone?: string;
  password: string;
}

export interface AuthResponse {
  token: string;
}

export interface UsernameResponse {
  username: string;
}

// Food types
export interface FoodPlace {
  placeId: number;
  placeName: string;
  description?: string;
  address: string;
  city?: string;
  locality?: string;
  landmark?: string;
  mapLocation?: string;
  contactInfo?: string;
  priceRange?: 'BUDGET' | 'MODERATE' | 'EXPENSIVE';
  bestForCategoryId?: number;
  submittedByUserId: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  rejectionReason?: string;
  averageRating: number;
  categories?: string[];
  imageUrls?: string[];
  createdAt?: string;
  approvedAt?: string;
}

export interface FoodCategory {
  categoryId: number;
  categoryName: string;
  createdByUserId: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  createdAt: string;
  approvedAt?: string;
}

export interface CreateFoodPlaceRequest {
  placeName: string;
  description?: string;
  address: string;
  city?: string;
  locality?: string;
  landmark?: string;
  mapLocation?: string;
  contactInfo?: string;
  priceRange?: 'BUDGET' | 'MODERATE' | 'EXPENSIVE';
  categoryIds?: number[];
  bestForCategoryId?: number;
  submittedByUserId: number;
}

// Hostel types
export interface Hostel {
  hostelId: number;
  hostelName: string;
  description?: string;
  address: string;
  city?: string;
  locality?: string;
  landmark?: string;
  mapLocation?: string;
  distanceFromCdac?: string;
  monthlyRentMin?: number;
  monthlyRentMax?: number;
  hasWifi?: boolean;
  hasAc?: boolean;
  hasMess?: boolean;
  hasLaundry?: boolean;
  roomCapacity?: number;
  contactPersonName?: string;
  contactPersonPhone?: string;
  submittedByUserId: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  rejectionReason?: string;
  createdAt: string;
  approvedAt?: string;
}

export interface CreateHostelRequest {
  hostelName: string;
  description?: string;
  address: string;
  city?: string;
  locality?: string;
  landmark?: string;
  mapLocation?: string;
  distanceFromCdac?: string;
  monthlyRentMin?: number;
  monthlyRentMax?: number;
  hasWifi?: boolean;
  hasAc?: boolean;
  hasMess?: boolean;
  hasLaundry?: boolean;
  roomCapacity?: number;
  contactPersonName?: string;
  contactPersonPhone?: string;
}

// Suggestion types
export interface Suggestion {
  suggestionId: number;
  userId: number;
  category: 'FOOD' | 'HOSTEL' | 'GENERAL';
  content: string;
  createdAt: string;
}

export interface CreateSuggestionRequest {
  userId: number;
  category: 'FOOD' | 'HOSTEL' | 'GENERAL';
  content: string;
}

// Admin Dashboard types
export interface DashboardStats {
  pendingUsers: number;
  pendingFood: number;
  pendingHostels: number;
  totalSuggestions: number;
}

// Pagination types
export interface PageRequest {
  page?: number;
  size?: number;
  sort?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

// API Error types
export interface ApiError {
  message: string;
  status?: number;
  timestamp?: string;
}
