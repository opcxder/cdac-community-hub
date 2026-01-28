import client from "@/api/client";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Skeleton } from "@/components/ui/skeleton";

interface DashboardStats {
  pendingUsers: number;
  pendingFoodPlaces: number;
  pendingHostels: number;
  totalSuggestions: number;
}

export default function AdminDashboardPage() {
  const navigate = useNavigate();
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchStats() {
      setLoading(true);
      setError(null);

      try {
        const response = await client.get<DashboardStats>("/api/admin/dashboard");
        setStats(response.data);
      } catch (err: unknown) {
        const message =
          (err as any)?.response?.data?.message || "Failed to load dashboard stats";
        setError(message);
      } finally {
        setLoading(false);
      }
    }

    fetchStats();
  }, []);

  if (loading) {
    return (
      <div className="space-y-6 p-6">
        <h1 className="text-2xl font-bold">Admin Dashboard</h1>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {[...Array(4)].map((_, i) => (
            <Skeleton key={i} className="h-24 w-full rounded shadow" />
          ))}
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center text-red-600 p-8">
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div className="space-y-6 p-6">
      <h1 className="text-2xl font-bold">Admin Dashboard</h1>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {/* Pending Users */}
        <div
          className="p-4 bg-white rounded shadow hover:shadow-lg transition-shadow cursor-pointer"
          onClick={() => navigate("/admin/users")}
        >
          <p className="text-sm font-medium text-zinc-500">Pending Users</p>
          <p className="mt-2 text-2xl font-bold">{stats?.pendingUsers}</p>
        </div>

        {/* Pending Food */}
        <div
          className="p-4 bg-white rounded shadow hover:shadow-lg transition-shadow cursor-pointer"
          onClick={() => navigate("/admin/food")}
        >
          <p className="text-sm font-medium text-zinc-500">Pending Food</p>
          <p className="mt-2 text-2xl font-bold">{stats?.pendingFoodPlaces}</p>
        </div>

        {/* Pending Hostels */}
        <div
          className="p-4 bg-white rounded shadow hover:shadow-lg transition-shadow cursor-pointer"
          onClick={() => navigate("/admin/hostels")}
        >
          <p className="text-sm font-medium text-zinc-500">Pending Hostels</p>
          <p className="mt-2 text-2xl font-bold">{stats?.pendingHostels}</p>
        </div>

        {/* Suggestions (total) */}
        <div
          className="p-4 bg-white rounded shadow hover:shadow-lg transition-shadow cursor-pointer"
          onClick={() => navigate("/admin/suggestions")}
        >
          <p className="text-sm font-medium text-zinc-500">Suggestions</p>
          <p className="mt-2 text-2xl font-bold">{stats?.totalSuggestions}</p>
        </div>
      </div>
    </div>
  );
}
