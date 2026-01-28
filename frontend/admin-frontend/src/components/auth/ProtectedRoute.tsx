import { useAuthStore } from "@/stores/authStore";
import { Navigate, Outlet, useLocation } from "react-router-dom";


/**
 * ProtectedRoute
 *
 * Guards routes that require authentication.
 * - If user is not authenticated → redirect to login
 * - Otherwise → render child routes
 *
 * NOTE:
 * - Does NOT check admin role
 * - Admin-specific checks belong in AdminRoute
 */


export default function ProtectedRoute() {
    const isAuthenticated = useAuthStore((state) => state.isAuthenticated());
    const location = useLocation();

    if (!isAuthenticated) {
        return (<Navigate to="/login" state={{ from: location }} replace />
        );
    }
    return <Outlet />;
}