import { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Users, UtensilsCrossed, Home, MessageSquare, FolderTree } from 'lucide-react';
import { LoadingSpinner } from '@/components/shared';
import { adminService } from '@/api/services';
import type { DashboardStats } from '@/types';
import { useToast } from '@/hooks/use-toast';
import { Link } from 'react-router-dom';

/**
 * Admin Dashboard Page
 * Overview of pending approvals and system statistics
 */
export function AdminDashboard() {
    const [stats, setStats] = useState<DashboardStats | null>(null);
    const [loading, setLoading] = useState(true);
    const { toast } = useToast();

    useEffect(() => {
        const fetchStats = async () => {
            try {
                setLoading(true);
                const data = await adminService.getDashboardStats();
                setStats(data);
            } catch (error) {
                toast({
                    title: 'Error',
                    description: 'Failed to fetch dashboard statistics',
                    variant: 'destructive',
                });
            } finally {
                setLoading(false);
            }
        };

        fetchStats();
    }, []);

    if (loading) {
        return <LoadingSpinner fullPage text="Loading dashboard..." />;
    }

    if (!stats) {
        return null;
    }

    const statCards = [
        {
            title: 'Pending Users',
            value: stats.pendingUsers,
            icon: Users,
            color: 'text-blue-600',
            bgColor: 'bg-blue-100',
            link: '/admin/users',
        },
        {
            title: 'Pending Food Places',
            value: stats.pendingFood,
            icon: UtensilsCrossed,
            color: 'text-orange-600',
            bgColor: 'bg-orange-100',
            link: '/admin/food',
        },
        {
            title: 'Pending Hostels',
            value: stats.pendingHostels,
            icon: Home,
            color: 'text-purple-600',
            bgColor: 'bg-purple-100',
            link: '/admin/hostels',
        },
        {
            title: 'Food Categories',
            value: stats.pendingFoodCategories,
            icon: FolderTree,
            color: 'text-green-600',
            bgColor: 'bg-green-100',
            link: '/admin/food-categories',
        },
        {
            title: 'Hostel Categories',
            value: stats.pendingHostelCategories,
            icon: FolderTree,
            color: 'text-teal-600',
            bgColor: 'bg-teal-100',
            link: '/admin/hostel-categories',
        },
        {
            title: 'Total Suggestions',
            value: stats.totalSuggestions,
            icon: MessageSquare,
            color: 'text-pink-600',
            bgColor: 'bg-pink-100',
            link: '/admin/suggestions',
        },
    ];

    return (
        <div className="container mx-auto py-6">
            <div className="mb-6">
                <h1 className="text-3xl font-bold">Admin Dashboard</h1>
                <p className="text-muted-foreground">
                    Overview of pending approvals and system statistics
                </p>
            </div>

            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
                {statCards.map((stat) => (
                    <Link key={stat.title} to={stat.link}>
                        <Card className="transition-shadow hover:shadow-lg">
                            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                <CardTitle className="text-sm font-medium">{stat.title}</CardTitle>
                                <div className={`rounded-full p-2 ${stat.bgColor}`}>
                                    <stat.icon className={`h-4 w-4 ${stat.color}`} />
                                </div>
                            </CardHeader>
                            <CardContent>
                                <div className="text-2xl font-bold">{stat.value}</div>
                                <p className="text-xs text-muted-foreground">
                                    {stat.value === 0 ? 'No items pending' : 'Click to review'}
                                </p>
                            </CardContent>
                        </Card>
                    </Link>
                ))}
            </div>

            {/* Quick Actions */}
            <div className="mt-8">
                <h2 className="mb-4 text-xl font-semibold">Quick Actions</h2>
                <div className="grid gap-4 md:grid-cols-2">
                    <Card>
                        <CardHeader>
                            <CardTitle>Pending Approvals</CardTitle>
                        </CardHeader>
                        <CardContent className="space-y-2">
                            {stats.pendingUsers > 0 && (
                                <Link
                                    to="/admin/users"
                                    className="block rounded-md bg-blue-50 p-3 transition-colors hover:bg-blue-100"
                                >
                                    <p className="font-medium">Review {stats.pendingUsers} user registrations</p>
                                </Link>
                            )}
                            {stats.pendingFood > 0 && (
                                <Link
                                    to="/admin/food"
                                    className="block rounded-md bg-orange-50 p-3 transition-colors hover:bg-orange-100"
                                >
                                    <p className="font-medium">Review {stats.pendingFood} food submissions</p>
                                </Link>
                            )}
                            {stats.pendingHostels > 0 && (
                                <Link
                                    to="/admin/hostels"
                                    className="block rounded-md bg-purple-50 p-3 transition-colors hover:bg-purple-100"
                                >
                                    <p className="font-medium">Review {stats.pendingHostels} hostel submissions</p>
                                </Link>
                            )}
                            {stats.pendingFoodCategories > 0 && (
                                <Link
                                    to="/admin/food-categories"
                                    className="block rounded-md bg-green-50 p-3 transition-colors hover:bg-green-100"
                                >
                                    <p className="font-medium">Review {stats.pendingFoodCategories} food categories</p>
                                </Link>
                            )}
                            {stats.pendingHostelCategories > 0 && (
                                <Link
                                    to="/admin/hostel-categories"
                                    className="block rounded-md bg-teal-50 p-3 transition-colors hover:bg-teal-100"
                                >
                                    <p className="font-medium">Review {stats.pendingHostelCategories} hostel categories</p>
                                </Link>
                            )}
                            {stats.pendingUsers === 0 &&
                                stats.pendingFood === 0 &&
                                stats.pendingHostels === 0 &&
                                stats.pendingFoodCategories === 0 &&
                                stats.pendingHostelCategories === 0 && (
                                    <p className="text-center text-sm text-muted-foreground">
                                        All caught up! No pending approvals.
                                    </p>
                                )}
                        </CardContent>
                    </Card>

                    <Card>
                        <CardHeader>
                            <CardTitle>User Feedback</CardTitle>
                        </CardHeader>
                        <CardContent>
                            <Link
                                to="/admin/suggestions"
                                className="block rounded-md bg-pink-50 p-3 transition-colors hover:bg-pink-100"
                            >
                                <p className="font-medium">View {stats.totalSuggestions} user suggestions</p>
                                <p className="text-sm text-muted-foreground">
                                    Review feedback and improvement ideas
                                </p>
                            </Link>
                        </CardContent>
                    </Card>
                </div>
            </div>
        </div>
    );
}
