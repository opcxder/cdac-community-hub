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
      <div className="min-h-screen bg-gradient-to-b from-background to-muted/20 py-4 sm:py-6">
        <div className="container mx-auto px-3 sm:px-4">
          <h1 className="text-2xl sm:text-3xl font-bold mb-4 sm:mb-6">Admin Dashboard</h1>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 md:gap-6">
            {[...Array(4)].map((_, i) => (
              <Skeleton key={i} className="h-24 w-full rounded-xl shadow" />
            ))}
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gradient-to-b from-background to-muted/20 py-4 sm:py-6">
        <div className="container mx-auto px-3 sm:px-4">
          <div className="text-center text-red-600 p-8 bg-red-50 rounded-xl border border-red-200">
            <p className="text-sm sm:text-base">{error}</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-b from-background to-muted/20 py-4 sm:py-6">
      <div className="container mx-auto px-3 sm:px-4">
        <h1 className="text-2xl sm:text-3xl font-bold mb-4 sm:mb-6">Admin Dashboard</h1>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 md:gap-6">
          {/* Pending Users */}
          <div
            className="p-4 sm:p-6 bg-white rounded-xl border border-border/50 shadow-md hover:shadow-2xl hover:scale-[1.02] transition-all duration-300 cursor-pointer"
            onClick={() => navigate("/admin/users")}
          >
            <p className="text-xs sm:text-sm font-medium text-zinc-500">Pending Users</p>
            <p className="mt-2 text-2xl sm:text-3xl font-bold">{stats?.pendingUsers}</p>
          </div>

          {/* Pending Food */}
          <div
            className="p-4 sm:p-6 bg-white rounded-xl border border-border/50 shadow-md hover:shadow-2xl hover:scale-[1.02] transition-all duration-300 cursor-pointer"
            onClick={() => navigate("/admin/food")}
          >
            <p className="text-xs sm:text-sm font-medium text-zinc-500">Pending Food</p>
            <p className="mt-2 text-2xl sm:text-3xl font-bold">{stats?.pendingFoodPlaces}</p>
          </div>

          {/* Pending Hostels */}
          <div
            className="p-4 sm:p-6 bg-white rounded-xl border border-border/50 shadow-md hover:shadow-2xl hover:scale-[1.02] transition-all duration-300 cursor-pointer"
            onClick={() => navigate("/admin/hostels")}
          >
            <p className="text-xs sm:text-sm font-medium text-zinc-500">Pending Hostels</p>
            <p className="mt-2 text-2xl sm:text-3xl font-bold">{stats?.pendingHostels}</p>
          </div>

          {/* Suggestions (total) */}
          <div
            className="p-4 sm:p-6 bg-white rounded-xl border border-border/50 shadow-md hover:shadow-2xl hover:scale-[1.02] transition-all duration-300 cursor-pointer"
            onClick={() => navigate("/admin/suggestions")}
          >
            <p className="text-xs sm:text-sm font-medium text-zinc-500">Suggestions</p>
            <p className="mt-2 text-2xl sm:text-3xl font-bold">{stats?.totalSuggestions}</p>
          </div>
        </div>
      </div>
    </div>
  );
}
