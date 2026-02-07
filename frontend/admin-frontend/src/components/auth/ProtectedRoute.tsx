import { useAuthStore } from "@/stores/authStore";
import { Navigate, Outlet, useLocation } from "react-router-dom";





export default function ProtectedRoute() {
    const isAuthenticated = useAuthStore((state) => state.isAuthenticated());
    const location = useLocation();

    if (!isAuthenticated) {
        return (<Navigate to="/login" state={{ from: location }} replace />
        );
    }
    return <Outlet />;
}