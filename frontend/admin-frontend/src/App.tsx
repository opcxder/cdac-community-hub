import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import LandingPage from "@/pages/public/LandingPage";
import LoginPage from "@/pages/public/LoginPage";
import SignupPage from "@/pages/public/SignupPage";
import BrowseFoodPage from "@/pages/public/BrowseFoodPage";
import BrowseHostelsPage from "@/pages/public/BrowseHostelsPage";
import ProtectedRoute from "@/components/auth/ProtectedRoute";
import ErrorBoundary from "@/components/ErrorBoundary";

import DashboardPage from "@/pages/user/DashboardPage";
import SubmitFoodPage from "@/pages/user/SubmitFoodPage";
import SubmitHostelPage from "@/pages/user/SubmitHostelPage";

import { Toaster } from 'sonner';
import AdminRoute from "./components/auth/AdminRoute";
import AdminLayout from "./components/layout/AdminLayout";

// Import all new admin pages
import {
  AdminDashboard,
  UserManagement,
  FoodPlaceManagement,
  FoodCategoryManagement,
  HostelManagement,
  HostelCategoryManagement,
  SuggestionsManagement,
} from "@/pages/admin";

export default function App() {
  return (
    <BrowserRouter><ErrorBoundary>
      <Routes>
        {/* Public routes */}
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/food" element={<BrowseFoodPage />} />
        <Route path="/hostels" element={<BrowseHostelsPage />} />

        {/* Protected routes */}
        <Route element={<ProtectedRoute />}>
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/submit-food" element={<SubmitFoodPage />} />
          <Route path="/submit-hostel" element={<SubmitHostelPage />} />

          {/* Admin routes (all nested inside AdminRouteLayout) */}
          <Route element={<AdminRoute />}>
            <Route element={<AdminLayout />}>
              <Route path="/admin" element={<AdminDashboard />} />
              <Route path="/admin/users" element={<UserManagement />} />
              <Route path="/admin/food" element={<FoodPlaceManagement />} />
              <Route path="/admin/food-categories" element={<FoodCategoryManagement />} />
              <Route path="/admin/hostels" element={<HostelManagement />} />
              <Route path="/admin/hostel-categories" element={<HostelCategoryManagement />} />
              <Route path="/admin/suggestions" element={<SuggestionsManagement />} />
            </Route>
          </Route>
        </Route>

        {/* Catch-all → redirect to landing page */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
      <Toaster richColors />
    </ErrorBoundary>
    </BrowserRouter>
  );
}
