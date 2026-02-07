import { useAuthStore } from "@/stores/authStore";
import { Navigate, Outlet } from "react-router-dom";




export default function AdminRoute() {
    const isAdmin = useAuthStore((state) => state.isAdmin());

    if (!isAdmin) {
        return <Navigate to="/dashboard" replace />;
    }

    return <Outlet />;
}