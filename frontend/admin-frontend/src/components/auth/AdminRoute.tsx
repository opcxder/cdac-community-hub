import { useAuthStore } from "@/stores/authStore";
import { Navigate, Outlet } from "react-router-dom";



/**
 * AdminRoute
 *
 * Guards admin-only routes.
 * Assumes authentication is already verified by ProtectedRoute.
 *
 * Rule:
 * - Admin is identified by userId === -1
 * - Non-admin users are redirected safely
 */
export default function AdminRoute() {
    const isAdmin = useAuthStore((state) => state.isAdmin());

    if (!isAdmin) {
        return <Navigate to="/dashboard" replace />;
    }

    return <Outlet />;
}