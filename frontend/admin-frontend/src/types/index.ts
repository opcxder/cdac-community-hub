// ============================================
// COMMON TYPES
// ============================================

export type ApprovalStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

export type PriceRange = 'BUDGET' | 'MODERATE' | 'EXPENSIVE';

export type SuggestionCategory = 'FOOD' | 'HOSTEL' | 'GENERAL';

// ============================================
// USER TYPES
// ============================================

export interface User {
  userId: number;
  username: string;
  email: string;
  phone?: string;
  accountStatus: ApprovalStatus;
  rejectionReason?: string;
  createdAt: string;
  approvedAt?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  email: string;
  phone?: string;
  password: string;
}

export interface AuthResponse {
  user: User;
  accessToken: string;
  refreshToken: string;
}

// ============================================
// FOOD TYPES
// ============================================

export interface FoodCategory {
  categoryId: number;
  categoryName: string;
  createdByUserId: number;
  status: ApprovalStatus;
  rejectionReason?: string;
  createdAt: string;
  approvedAt?: string;
}

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
  priceRange?: PriceRange;
  submittedByUserId: number;
  status: ApprovalStatus;
  rejectionReason?: string;
  averageRating: number;
  categories?: FoodCategory[];
  categoryIds?: number[];
  bestForCategoryId?: number;
  imageUrls?: string[];
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
  priceRange?: PriceRange;
  categoryIds?: number[];
  bestForCategoryId?: number;
  suggestedCategoryName?: string; // New category suggestion
}

export interface CreateFoodCategoryRequest {
  categoryName: string;
}

// ============================================
// HOSTEL TYPES
// ============================================

export interface HostelCategory {
  categoryId: number;
  categoryName: string;
  createdByUserId: number;
  status: ApprovalStatus;
  rejectionReason?: string;
  createdAt: string;
  approvedAt?: string;
}

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
  status: ApprovalStatus;
  rejectionReason?: string;
  categories?: HostelCategory[];
  categoryIds?: number[];
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
  suggestedCategoryName?: string; // New category suggestion
}

export interface CreateHostelCategoryRequest {
  categoryName: string;
}

// ============================================
// SUGGESTION TYPES
// ============================================

export interface Suggestion {
  suggestionId: number;
  userId: number;
  category: SuggestionCategory;
  content: string;
  createdAt: string;
}

export interface CreateSuggestionRequest {
  category: SuggestionCategory;
  content: string;
}

// ============================================
// ADMIN DASHBOARD TYPES
// ============================================

export interface DashboardStats {
  pendingUsers: number;
  pendingFood: number;
  pendingHostels: number;
  totalSuggestions: number;
  pendingFoodCategories: number;
  pendingHostelCategories: number;
}

export interface RejectRequest {
  reason: string;
}

// ============================================
// PAGINATION TYPES
// ============================================

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

// ============================================
// FILTER TYPES
// ============================================

export interface FoodFilters {
  search?: string;
  priceRange?: PriceRange[];
  categoryIds?: number[];
  city?: string;
}

export interface HostelFilters {
  search?: string;
  rentMin?: number;
  rentMax?: number;
  hasWifi?: boolean;
  hasAc?: boolean;
  hasMess?: boolean;
  hasLaundry?: boolean;
  city?: string;
}

// ============================================
// API ERROR TYPES
// ============================================

export interface ApiError {
  message: string;
  status?: number;
  timestamp?: string;
  path?: string;
}
