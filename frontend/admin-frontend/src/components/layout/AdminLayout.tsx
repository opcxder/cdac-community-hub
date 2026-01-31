import { useState } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import { Menu, LogOut } from "lucide-react";
import { useAuthStore } from "@/stores/authStore";
import Sidebar from "./Sidebar";



export default function AdminLayout() {
    const navigate = useNavigate();
    const logout = useAuthStore((state) => state.logout);
    const user = useAuthStore((state) => state.user);

    const [sidebarOpen, setSidebarOpen] = useState(true);

    const handleLogout = () => {
        logout();
        navigate("/login", { replace: true });
    };

    function getDisplayName(email: string) {
        if (!email) return "User";
        const namePart = email.split("@")[0];
        return namePart.charAt(0).toUpperCase() + namePart.slice(1);
    }

    return (
        <div className="flex h-screen bg-zinc-100">
            {/* Sidebar */}
            <Sidebar onLogout={handleLogout} user={user} />

            {/* Main content */}
            <div className="flex-1 flex flex-col">
                {/* Mobile header */}
                <header className="md:hidden flex items-center justify-between p-4 bg-zinc-900 text-white">
                    <button
                        onClick={() => setSidebarOpen(!sidebarOpen)}
                        aria-label="Toggle sidebar"
                    >
                        <Menu size={20} />
                    </button>
                    <span className="font-medium">Admin Panel</span>
                    <button onClick={handleLogout} aria-label="Logout">
                        <LogOut size={20} />
                    </button>
                </header>

                {/* Page content */}
                <main className="flex-1 p-6 overflow-auto">
                    <Outlet />
                </main>
            </div>
        </div>
    );
}
