import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "@/stores/authStore";
import { UtensilsCrossed, Building2, MessageSquare, PlusCircle } from "lucide-react";

export default function DashboardPage() {
    const navigate = useNavigate();
    const user = useAuthStore((state) => state.user);

    function getDisplayName(email: string) {
        if (!email) return "User";
        const namePart = email.split("@")[0];
        return namePart.charAt(0).toUpperCase() + namePart.slice(1);
    }

    const quickActions = [
        {
            title: "Browse Food Places",
            description: "Discover restaurants and eateries near CDAC",
            icon: UtensilsCrossed,
            color: "text-orange-600",
            bgColor: "bg-orange-100",
            path: "/food",
        },
        {
            title: "Browse Hostels",
            description: "Find accommodation options near CDAC",
            icon: Building2,
            color: "text-purple-600",
            bgColor: "bg-purple-100",
            path: "/hostels",
        },
        {
            title: "Submit Food Place",
            description: "Share a great restaurant you discovered",
            icon: PlusCircle,
            color: "text-green-600",
            bgColor: "bg-green-100",
            path: "/submit-food",
        },
        {
            title: "Submit Hostel",
            description: "Recommend a hostel for fellow students",
            icon: PlusCircle,
            color: "text-blue-600",
            bgColor: "bg-blue-100",
            path: "/submit-hostel",
        },
        {
            title: "Share Suggestions",
            description: "Help us improve the platform",
            icon: MessageSquare,
            color: "text-pink-600",
            bgColor: "bg-pink-100",
            path: "/submit-suggestion",
        },
    ];

    return (
        <div className="min-h-screen bg-gradient-to-b from-background to-muted/20 py-4 sm:py-6">
            <div className="container mx-auto px-3 sm:px-4">
                {/* Welcome Section */}
                <div className="mb-6 sm:mb-8">
                    <h1 className="text-2xl sm:text-3xl font-bold">
                        Welcome back, {user ? getDisplayName(user.email) : "User"}!
                    </h1>
                    <p className="text-sm text-muted-foreground mt-2">
                        Explore food places, hostels, and share your experiences with the CDAC community.
                    </p>
                </div>

                {/* Quick Actions */}
                <div className="grid gap-4 md:gap-6 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3">
                    {quickActions.map((action) => (
                        <Card
                            key={action.title}
                            className="overflow-hidden rounded-xl border border-border/50 shadow-md hover:shadow-2xl hover:scale-[1.02] transition-all duration-300 cursor-pointer"
                            onClick={() => navigate(action.path)}
                        >
                            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                <CardTitle className="text-sm font-medium">{action.title}</CardTitle>
                                <div className={`rounded-full p-2 ${action.bgColor}`}>
                                    <action.icon className={`h-4 w-4 ${action.color}`} />
                                </div>
                            </CardHeader>
                            <CardContent>
                                <CardDescription className="text-xs sm:text-sm">{action.description}</CardDescription>
                                <Button
                                    variant="link"
                                    className="mt-2 p-0 h-auto text-xs sm:text-sm"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        navigate(action.path);
                                    }}
                                >
                                    Go →
                                </Button>
                            </CardContent>
                        </Card>
                    ))}
                </div>

                {/* Info Section */}
                <div className="mt-6 sm:mt-8">
                    <Card className="overflow-hidden rounded-xl border border-border/50 shadow-md">
                        <CardHeader className="bg-muted/30 border-b">
                            <CardTitle className="text-lg sm:text-xl">About CDAC Community Hub</CardTitle>
                        </CardHeader>
                        <CardContent className="space-y-2 p-4 sm:p-6">
                            <p className="text-xs sm:text-sm text-muted-foreground">
                                This platform helps CDAC students and staff discover and share information about:
                            </p>
                            <ul className="list-disc list-inside space-y-1 text-xs sm:text-sm text-muted-foreground ml-4">
                                <li>Nearby restaurants and food places</li>
                                <li>Hostel and accommodation options</li>
                                <li>Community suggestions and feedback</li>
                            </ul>
                            <p className="text-xs sm:text-sm text-muted-foreground mt-4">
                                Your contributions help build a better community for everyone!
                            </p>
                        </CardContent>
                    </Card>
                </div>
            </div>
        </div>
    );
}
