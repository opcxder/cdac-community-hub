import { NavLink } from "react-router-dom";
import { FaTachometerAlt, FaUsers, FaUtensils, FaBuilding, FaComments } from "react-icons/fa";
import { LogOut, X } from "lucide-react";

interface SidebarLink {
  name: string;
  path: string;
  icon: React.ReactNode;
}

const links: SidebarLink[] = [
  { name: "Dashboard", path: "/admin", icon: <FaTachometerAlt /> },
  { name: "Users", path: "/admin/users", icon: <FaUsers /> },
  { name: "Food", path: "/admin/food", icon: <FaUtensils /> },
  { name: "Hostels", path: "/admin/hostels", icon: <FaBuilding /> },
  { name: "Suggestions", path: "/admin/suggestions", icon: <FaComments /> },
];

interface SidebarProps {
  onLogout: () => void;
  user: { email: string } | null;
  isOpen: boolean;
  onClose: () => void;
}

function getDisplayName(email: string) {
  if (!email) return "User";
  const namePart = email.split("@")[0];
  return namePart.charAt(0).toUpperCase() + namePart.slice(1);
}

export default function Sidebar({ onLogout, user, isOpen, onClose }: SidebarProps) {
  return (
    <>
      {/* Mobile Overlay */}
      {isOpen && (
        <div
          className="fixed inset-0 bg-black/50 z-40 lg:hidden"
          onClick={onClose}
        />
      )}

      {/* Sidebar */}
      <aside className={`
        fixed lg:static inset-y-0 left-0 z-50
        w-64 bg-white border-r border-zinc-200 min-h-screen flex flex-col
        transform transition-transform duration-300 ease-in-out
        ${isOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
      `}>
        <div className="p-4 border-b border-zinc-200 flex items-center justify-between">
          <div>
            <h2 className="text-xl font-bold text-zinc-900">Admin Panel</h2>
            {user && (
              <p className="text-sm text-zinc-500 mt-1">
                Logged in as {getDisplayName(user.email)}
              </p>
            )}
          </div>
          {/* Close button for mobile */}
          <button
            onClick={onClose}
            className="lg:hidden p-2 hover:bg-zinc-100 rounded-md transition-colors"
          >
            <X size={20} />
          </button>
        </div>

        <nav className="flex-1 flex flex-col space-y-2 p-4">
          {links.map((link) => (
            <NavLink
              key={link.path}
              to={link.path}
              onClick={onClose}
              className={({ isActive }) =>
                `flex items-center px-4 py-2 rounded-md text-zinc-700 hover:bg-indigo-50 hover:text-indigo-700 transition-colors ${isActive ? "bg-indigo-100 text-indigo-700 font-semibold" : ""
                }`
              }
            >
              <span className="mr-3 text-lg">{link.icon}</span>
              <span>{link.name}</span>
            </NavLink>
          ))}
        </nav>

        <div className="p-4 border-t border-zinc-200">
          <button
            onClick={onLogout}
            className="flex items-center w-full px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded transition-colors"
          >
            <LogOut className="mr-2" size={18} />
            Logout
          </button>
        </div>
      </aside>
    </>
  );
}
