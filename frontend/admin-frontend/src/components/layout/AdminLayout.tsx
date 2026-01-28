import { useState } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { Menu, LogOut, Home, Users } from "lucide-react";
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
            <Sidebar/>
            <aside
                className={`bg-zinc-900 text-white w-64 transition-all duration-300 ${sidebarOpen ? "translate-x-0" : "-translate-x-64"
                    }`}
            >
                <div className="flex items-center justify-between p-4 border-b border-zinc-700">
                    <h2 className="text-lg font-bold">Admin Panel</h2>
                    <button
                        className="md:hidden text-white"
                        onClick={() => setSidebarOpen(!sidebarOpen)}
                        aria-label="Toggle sidebar"
                    >
                        <Menu size={20} />
                    </button>
                </div>

                {user && (
                    <div className="px-4 py-2 text-sm text-zinc-300">
                        Logged in as {getDisplayName(user.email)}
                    </div>
                )}

                <nav className="mt-6 flex flex-col space-y-2">
                    <NavLink
                        to="/admin"
                        className={({ isActive }) =>
                            `flex items-center px-4 py-2 rounded hover:bg-zinc-800 transition-colors ${isActive ? "bg-indigo-600" : ""
                            }`
                        }
                    >
                        <Home className="mr-3" size={18} />
                        Dashboard
                    </NavLink>

                    <NavLink
                        to="/admin/users"
                        className={({ isActive }) =>
                            `flex items-center px-4 py-2 rounded hover:bg-zinc-800 transition-colors ${isActive ? "bg-indigo-600" : ""
                            }`
                        }
                    >
                        <Users className="mr-3" size={18} />
                        Users
                    </NavLink>
                </nav>

                <div className="absolute bottom-0 w-full p-4 border-t border-zinc-700">
                    <button
                        onClick={handleLogout}
                        className="flex items-center w-full px-4 py-2 bg-red-600 hover:bg-red-700 rounded transition-colors disabled:opacity-60 disabled:cursor-not-allowed"
                    >
                        <LogOut className="mr-2" size={18} />
                        Logout
                    </button>
                </div>
            </aside>

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
