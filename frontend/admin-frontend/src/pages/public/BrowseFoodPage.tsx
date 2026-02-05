import { useEffect, useState } from "react";
import client from "@/api/client";
import FoodCard from "@/components/food/FoodCard";
import { Skeleton } from "@/components/ui/skeleton";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { UtensilsCrossed } from "lucide-react";

interface FoodPlace {
    placeId: number;
    placeName: string;
    averageRating: number;
    priceRange: string;
    imageUrls: string[];  // Changed from imageUrl: string
    locality: string;      // Changed from location: string
    categories: string[];  // Changed from Category[] to string[]
}

export default function BrowseFoodPage() {
    const [foodPlaces, setFoodPlaces] = useState<FoodPlace[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        async function fetchFoodPlaces() {
            setLoading(true);
            setError(null);
            try {
                console.log("🍽️ [BROWSE-FOOD] Fetching food places...");
                const response = await client.get<any>("/api/food/places");
                console.log("🍽️ [BROWSE-FOOD] Response:", response.data);

                // Handle paginated response (Spring Page object)
                if (response.data && response.data.content && Array.isArray(response.data.content)) {
                    setFoodPlaces(response.data.content);
                    console.log("🍽️ [BROWSE-FOOD] Loaded", response.data.content.length, "food places from paginated response");
                }
                // Handle direct array response (fallback)
                else if (Array.isArray(response.data)) {
                    setFoodPlaces(response.data);
                    console.log("🍽️ [BROWSE-FOOD] Loaded", response.data.length, "food places from array response");
                }
                // Invalid format
                else {
                    console.error("🍽️ [BROWSE-FOOD] Invalid response format:", response.data);
                    setFoodPlaces([]);
                    setError("Invalid data format received from server");
                }
            } catch (err: any) {
                console.error("🍽️ [BROWSE-FOOD] Error:", err);
                setError(err?.response?.data?.message || "Failed to load food places");
            } finally {
                setLoading(false);
            }
        }

        fetchFoodPlaces();
    }, []);

    return (
        <div className="min-h-screen bg-gradient-to-b from-background to-muted/20">
            {/* Header */}
            <div className="border-b bg-white shadow-sm">
                <div className="container mx-auto px-3 sm:px-4 py-4 sm:py-6">
                    <div className="flex items-center gap-3">
                        <UtensilsCrossed className="h-6 w-6 sm:h-8 sm:w-8 text-primary" />
                        <div>
                            <h1 className="text-2xl sm:text-3xl font-bold">Browse Food Places</h1>
                            <p className="text-sm text-muted-foreground">Discover the best food spots around CDAC</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* Content */}
            <div className="container mx-auto px-3 sm:px-4 py-6 sm:py-8">
                {loading ? (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6">
                        {[...Array(6)].map((_, i) => (
                            <div key={i} className="space-y-3">
                                <Skeleton className="aspect-video w-full rounded-xl" />
                                <Skeleton className="h-6 w-3/4" />
                                <Skeleton className="h-4 w-1/2" />
                                <Skeleton className="h-4 w-2/3" />
                            </div>
                        ))}
                    </div>
                ) : error ? (
                    <Alert variant="destructive" className="rounded-xl">
                        <AlertDescription>{error}</AlertDescription>
                    </Alert>
                ) : foodPlaces.length === 0 ? (
                    <div className="text-center py-12">
                        <UtensilsCrossed className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
                        <h3 className="text-lg font-semibold mb-2">No food places yet</h3>
                        <p className="text-sm text-muted-foreground">
                            Be the first to add a food place to the community!
                        </p>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6">
                        {foodPlaces.map((place) => (
                            <FoodCard key={place.placeId} {...place} />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}
