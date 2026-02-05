import { useNavigate } from "react-router-dom";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Star, MapPin, Wifi, Wind, UtensilsCrossed, Shirt } from "lucide-react";

interface HostelCardProps {
    hostelId: number;
    hostelName: string;
    description?: string;
    locality: string;
    city: string;
    imageUrls: string[];
    categories?: string[];
    monthlyRentMin: number;
    monthlyRentMax: number;
    hasWifi: boolean;
    hasAc: boolean;
    hasMess: boolean;
    hasLaundry: boolean;
    roomCapacities?: number[];
    forGender: string;
    overallRating?: number;
}

export default function HostelCard({
    hostelId,
    hostelName,
    locality,
    city,
    imageUrls,
    categories,
    monthlyRentMin,
    monthlyRentMax,
    hasWifi,
    hasAc,
    hasMess,
    hasLaundry,
    forGender,
    overallRating
}: HostelCardProps) {
    const navigate = useNavigate();

    // Compute display values
    const primaryImage = imageUrls && imageUrls.length > 0
        ? imageUrls[0]
        : 'https://via.placeholder.com/400x300?text=No+Image';

    const rentRange = monthlyRentMin && monthlyRentMax
        ? `₹${monthlyRentMin} - ₹${monthlyRentMax}/month`
        : 'Contact for pricing';

    const location = `${locality}, ${city}`;

    const facilityIcons: Record<string, any> = {
        WiFi: Wifi,
        AC: Wind,
        Mess: UtensilsCrossed,
        Laundry: Shirt
    };

    // Build facilities array from boolean flags
    const facilities = [
        hasWifi && 'WiFi',
        hasAc && 'AC',
        hasMess && 'Mess',
        hasLaundry && 'Laundry'
    ].filter(Boolean) as string[];

    const getGenderBadgeColor = (gender: string) => {
        const colors = {
            BOYS: "bg-blue-100 text-blue-800",
            GIRLS: "bg-pink-100 text-pink-800",
            BOTH: "bg-purple-100 text-purple-800"
        };
        return colors[gender as keyof typeof colors] || "bg-gray-100 text-gray-800";
    };

    return (
        <Card
            className="overflow-hidden cursor-pointer hover:shadow-lg transition-shadow duration-200"
            onClick={() => navigate(`/hostels/${hostelId}`)}
        >
            <div className="aspect-video w-full overflow-hidden bg-muted">
                <img
                    src={primaryImage}
                    alt={hostelName}
                    className="h-full w-full object-cover transition-transform duration-200 hover:scale-105"
                    onError={(e) => {
                        (e.target as HTMLImageElement).src = "https://via.placeholder.com/400x300?text=No+Image";
                    }}
                />
            </div>

            <CardContent className="p-4 space-y-2">
                <div className="flex items-start justify-between gap-2">
                    <h3 className="font-semibold text-lg line-clamp-1">{hostelName}</h3>
                    <Badge className={getGenderBadgeColor(forGender)} variant="secondary">
                        {forGender}
                    </Badge>
                </div>

                <div className="flex items-center gap-1 text-sm">
                    <Star className="h-4 w-4 fill-yellow-400 text-yellow-400" />
                    <span className="font-medium">{overallRating != null ? overallRating.toFixed(1) : '0.0'}</span>
                    <span className="text-muted-foreground">rating</span>
                </div>

                <div className="flex items-center gap-1 text-sm text-muted-foreground">
                    <MapPin className="h-4 w-4" />
                    <span className="line-clamp-1">{location}</span>
                </div>

                {/* Categories */}
                {categories && categories.length > 0 && (
                    <div className="flex flex-wrap gap-1">
                        {categories.slice(0, 2).map((cat, idx) => (
                            <span key={idx} className="text-xs bg-muted px-2 py-0.5 rounded">
                                {cat}
                            </span>
                        ))}
                        {categories.length > 2 && (
                            <span className="text-xs text-muted-foreground">+{categories.length - 2}</span>
                        )}
                    </div>
                )}

                <div className="flex items-center justify-between pt-1">
                    <span className="text-sm font-medium text-primary">{rentRange}</span>

                    <div className="flex gap-2">
                        {facilities && facilities.slice(0, 4).map((facility) => {
                            const Icon = facilityIcons[facility];
                            return Icon ? (
                                <div key={facility} className="text-muted-foreground" title={facility}>
                                    <Icon className="h-4 w-4" />
                                </div>
                            ) : null;
                        })}
                    </div>
                </div>
            </CardContent>
        </Card>
    );
}
