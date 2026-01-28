import { NavLink } from "react-router-dom";
import { FaTachometerAlt, FaUsers, FaUtensils, FaBuilding } from "react-icons/fa";

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
];

export default function Sidebar() {
  return (
    <aside className="w-64 bg-white border-r border-zinc-200 min-h-screen p-4 flex flex-col">
      <h2 className="text-xl font-bold mb-6 text-zinc-900">Admin Panel</h2>

      <nav className="flex flex-col space-y-2">
        {links.map((link) => (
          <NavLink
            key={link.path}
            to={link.path}
            className={({ isActive }) =>
              `flex items-center px-4 py-2 rounded-md text-zinc-700 hover:bg-indigo-50 hover:text-indigo-700 transition-colors ${
                isActive ? "bg-indigo-100 text-indigo-700 font-semibold" : ""
              }`
            }
          >
            <span className="mr-3 text-lg">{link.icon}</span>
            <span>{link.name}</span>
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
