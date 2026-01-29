import { useNavigate } from "react-router-dom";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Star, MapPin } from "lucide-react";

interface Category {
    categoryId: number;
    categoryName: string;
}

interface FoodCardProps {
    placeId: number;
    placeName: string;
    averageRating: number;
    priceRange: string;
    imageUrl: string;
    location: string;
    categories: Category[];
}

export default function FoodCard({
    placeId,
    placeName,
    averageRating,
    priceRange,
    imageUrl,
    location,
    categories
}: FoodCardProps) {
    const navigate = useNavigate();

    const getPriceRangeBadge = (range: string) => {
        const colors = {
            BUDGET: "bg-green-100 text-green-800",
            MEDIUM: "bg-yellow-100 text-yellow-800",
            EXPENSIVE: "bg-red-100 text-red-800"
        };
        return colors[range as keyof typeof colors] || "bg-gray-100 text-gray-800";
    };

    return (
        <Card
            className="overflow-hidden cursor-pointer hover:shadow-lg transition-shadow duration-200"
            onClick={() => navigate(`/food/${placeId}`)}
        >
            <div className="aspect-video w-full overflow-hidden bg-muted">
                <img
                    src={imageUrl}
                    alt={placeName}
                    className="h-full w-full object-cover transition-transform duration-200 hover:scale-105"
                    onError={(e) => {
                        (e.target as HTMLImageElement).src = "https://via.placeholder.com/400x300?text=No+Image";
                    }}
                />
            </div>

            <CardContent className="p-4 space-y-2">
                <div className="flex items-start justify-between gap-2">
                    <h3 className="font-semibold text-lg line-clamp-1">{placeName}</h3>
                    <Badge className={getPriceRangeBadge(priceRange)} variant="secondary">
                        {priceRange}
                    </Badge>
                </div>

                <div className="flex items-center gap-1 text-sm">
                    <Star className="h-4 w-4 fill-yellow-400 text-yellow-400" />
                    <span className="font-medium">{averageRating.toFixed(1)}</span>
                    <span className="text-muted-foreground">rating</span>
                </div>

                <div className="flex items-center gap-1 text-sm text-muted-foreground">
                    <MapPin className="h-4 w-4" />
                    <span className="line-clamp-1">{location}</span>
                </div>

                <div className="flex flex-wrap gap-1 pt-1">
                    {categories.slice(0, 3).map((category) => (
                        <Badge key={category.categoryId} variant="outline" className="text-xs">
                            {category.categoryName}
                        </Badge>
                    ))}
                    {categories.length > 3 && (
                        <Badge variant="outline" className="text-xs">
                            +{categories.length - 3}
                        </Badge>
                    )}
                </div>
            </CardContent>
        </Card>
    );
}
