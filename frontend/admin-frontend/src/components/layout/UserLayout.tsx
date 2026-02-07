import { useState } from "react";
import { Outlet, useNavigate, NavLink } from "react-router-dom";
import { Menu, LogOut, Home, UtensilsCrossed, Building2, MessageSquare, User, PlusCircle, X } from "lucide-react";
import { useAuthStore } from "@/stores/authStore";

export default function UserLayout() {
    const navigate = useNavigate();
    const logout = useAuthStore((state) => state.logout);
    const user = useAuthStore((state) => state.user);
    const [sidebarOpen, setSidebarOpen] = useState(false);

    const handleLogout = () => {
        logout();
        navigate("/login", { replace: true });
    };

    function getDisplayName(email: string) {
        if (!email) return "User";
        const namePart = email.split("@")[0];
        return namePart.charAt(0).toUpperCase() + namePart.slice(1);
    }

    const navLinks = [
        { name: "Dashboard", path: "/dashboard", icon: Home },
        { name: "Browse Food", path: "/food", icon: UtensilsCrossed },
        { name: "Browse Hostels", path: "/hostels", icon: Building2 },
        { name: "Browse Suggestions", path: "/suggestions", icon: MessageSquare },
        { name: "Submit Food", path: "/submit-food", icon: PlusCircle },
        { name: "Submit Hostel", path: "/submit-hostel", icon: PlusCircle },
        { name: "Submit Suggestion", path: "/submit-suggestion", icon: PlusCircle },
        { name: "Profile", path: "/profile", icon: User },
    ];

    return (
        <div className="flex h-screen bg-zinc-100">
            {/* Mobile Overlay */}
            {sidebarOpen && (
                <div
                    className="fixed inset-0 bg-black/50 z-40 md:hidden"
                    onClick={() => setSidebarOpen(false)}
                />
            )}

            {/* Sidebar */}
            <aside className={`
                fixed md:static inset-y-0 left-0 z-50
                w-64 bg-white border-r border-zinc-200 min-h-screen flex flex-col
                transform transition-transform duration-300 ease-in-out
                ${sidebarOpen ? "translate-x-0" : "-translate-x-full md:translate-x-0"}
            `}>
                <div className="p-4 border-b border-zinc-200 flex items-center justify-between">
                    <div>
                        <h2 className="text-xl font-bold text-zinc-900">CDAC Hub</h2>
                        {user && (
                            <p className="text-sm text-zinc-500 mt-1">
                                Welcome, {getDisplayName(user.email)}
                            </p>
                        )}
                    </div>
                    {/* Close button for mobile */}
                    <button
                        onClick={() => setSidebarOpen(false)}
                        className="md:hidden p-2 hover:bg-zinc-100 rounded-md text-zinc-500"
                    >
                        <X size={20} />
                    </button>
                </div>

                <nav className="flex-1 flex flex-col space-y-2 p-4">
                    {navLinks.map((link) => (
                        <NavLink
                            key={link.path}
                            to={link.path}
                            onClick={() => setSidebarOpen(false)}
                            className={({ isActive }) =>
                                `flex items-center px-4 py-2 rounded-md text-zinc-700 hover:bg-indigo-50 hover:text-indigo-700 transition-colors ${isActive ? "bg-indigo-100 text-indigo-700 font-semibold" : ""
                                }`
                            }
                        >
                            <link.icon className="mr-3 h-5 w-5" />
                            <span>{link.name}</span>
                        </NavLink>
                    ))}
                </nav>

                <div className="p-4 border-t border-zinc-200">
                    <button
                        onClick={handleLogout}
                        className="flex items-center w-full px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded transition-colors"
                    >
                        <LogOut className="mr-2" size={18} />
                        Logout
                    </button>
                </div>
            </aside>

            {/* Main content */}
            <div className="flex-1 flex flex-col min-w-0">
                {/* Mobile header */}
                <header className="md:hidden flex items-center justify-between p-4 bg-white border-b border-zinc-200 sticky top-0 z-30">
                    <button
                        onClick={() => setSidebarOpen(true)}
                        aria-label="Toggle sidebar"
                        className="p-2 -ml-2 hover:bg-zinc-100 rounded-md"
                    >
                        <Menu size={24} />
                    </button>
                    <span className="font-bold text-lg">CDAC Hub</span>
                    <button onClick={handleLogout} aria-label="Logout" className="p-2 -mr-2 text-red-600">
                        <LogOut size={20} />
                    </button>
                </header>

                {/* Page content */}
                <main className="flex-1 p-4 md:p-6 overflow-auto">
                    <Outlet />
                </main>
            </div>
        </div>
    );
}
