import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import LoginPage from "@/pages/public/LoginPage";
import ProtectedRoute from "@/components/auth/ProtectedRoute";
import ErrorBoundary from "@/components/ErrorBoundary";

import DashboardPage from "@/pages/user/DashboardPage";
import AdminDashboardPage from "@/pages/admin/AdminDashboardPage";



import { Toaster } from 'sonner';
import AdminRoute from "./components/auth/AdminRoute";
import AdminLayout from "./components/layout/AdminLayout";
import UserApprovalsPage from "./pages/admin/UserApprovalPage";
import FoodApprovalsPage from "./pages/admin/FoodApprovalsPage";
import HostelApprovalsPage from "./pages/admin/HostelApprovalsPage";
export default function App() {
  return (
    <BrowserRouter><ErrorBoundary>
      <Routes>
        {/* Public routes */}
        <Route path="/login" element={<LoginPage />} />

        {/* Protected routes */}
        <Route element={<ProtectedRoute />}>
          <Route path="/dashboard" element={<DashboardPage />} />

          {/* Admin routes (all nested inside AdminRouteLayout) */}
          <Route element={<AdminRoute />}>
            <Route element={<AdminLayout />}>
              <Route path="/admin" element={<AdminDashboardPage />} />
              <Route path="/admin/users" element={<UserApprovalsPage />} />
              <Route path="/admin/food" element={<FoodApprovalsPage />} />
              <Route path="/admin/hostels" element={<HostelApprovalsPage />} />
              {/*   <Route path="/admin/suggestions" element={<AdminSuggestionsPage />} /> */}
            </Route>
          </Route>
        </Route>

        {/* Catch-all → redirect to login */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
      <Toaster richColors />
      </ErrorBoundary>
    </BrowserRouter>
  );
}
