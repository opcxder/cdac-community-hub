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
import SubmitSuggestionPage from "@/pages/user/SubmitSuggestionPage";
import BrowseSuggestionsPage from "@/pages/user/BrowseSuggestionsPage";
import ProfilePage from "@/pages/user/ProfilePage";
import FoodDetailsPage from "@/pages/user/FoodDetailsPage";
import HostelDetailsPage from "@/pages/user/HostelDetailsPage";

import { Toaster } from 'sonner';
import AdminRoute from "./components/auth/AdminRoute";
import AdminLayout from "./components/layout/AdminLayout";
import UserLayout from "./components/layout/UserLayout";
import AccountStatusGuard from "./components/auth/AccountStatusGuard";

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
      
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />

      
        <Route element={<ProtectedRoute />}>
          {/* User routes with layout */}
          <Route element={<UserLayout />}>
            {/* Dashboard - always accessible */}
            <Route path="/dashboard" element={<DashboardPage />} />

           
            <Route path="/food" element={<AccountStatusGuard><BrowseFoodPage /></AccountStatusGuard>} />
            <Route path="/food/:id" element={<AccountStatusGuard><FoodDetailsPage /></AccountStatusGuard>} />
            <Route path="/hostels" element={<AccountStatusGuard><BrowseHostelsPage /></AccountStatusGuard>} />
            <Route path="/hostels/:id" element={<AccountStatusGuard><HostelDetailsPage /></AccountStatusGuard>} />
            <Route path="/suggestions" element={<AccountStatusGuard><BrowseSuggestionsPage /></AccountStatusGuard>} />
            <Route path="/submit-food" element={<AccountStatusGuard><SubmitFoodPage /></AccountStatusGuard>} />
            <Route path="/submit-hostel" element={<AccountStatusGuard><SubmitHostelPage /></AccountStatusGuard>} />
            <Route path="/submit-suggestion" element={<AccountStatusGuard><SubmitSuggestionPage /></AccountStatusGuard>} />
            <Route path="/profile" element={<ProfilePage />} />
          </Route>

        
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

      
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
      <Toaster richColors />
    </ErrorBoundary>
    </BrowserRouter>
  );
}
