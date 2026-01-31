import { NavLink } from "react-router-dom";
import { FaTachometerAlt, FaUsers, FaUtensils, FaBuilding, FaComments } from "react-icons/fa";
import { LogOut } from "lucide-react";

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
}

function getDisplayName(email: string) {
  if (!email) return "User";
  const namePart = email.split("@")[0];
  return namePart.charAt(0).toUpperCase() + namePart.slice(1);
}

export default function Sidebar({ onLogout, user }: SidebarProps) {
  return (
    <aside className="w-64 bg-white border-r border-zinc-200 min-h-screen flex flex-col">
      <div className="p-4 border-b border-zinc-200">
        <h2 className="text-xl font-bold text-zinc-900">Admin Panel</h2>
        {user && (
          <p className="text-sm text-zinc-500 mt-1">
            Logged in as {getDisplayName(user.email)}
          </p>
        )}
      </div>

      <nav className="flex-1 flex flex-col space-y-2 p-4">
        {links.map((link) => (
          <NavLink
            key={link.path}
            to={link.path}
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
  );
}
