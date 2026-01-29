import { useNavigate } from "react-router-dom";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Star, MapPin, Wifi, Wind, UtensilsCrossed, Shirt } from "lucide-react";

interface HostelCardProps {
    hostelId: number;
    name: string;
    overallRating: number;
    rentRange: string;
    primaryImageUrl: string;
    location: string;
    facilities: string[];
    forGender: string;
}

export default function HostelCard({
    hostelId,
    name,
    overallRating,
    rentRange,
    primaryImageUrl,
    location,
    facilities,
    forGender
}: HostelCardProps) {
    const navigate = useNavigate();

    const facilityIcons: Record<string, any> = {
        WiFi: Wifi,
        AC: Wind,
        Mess: UtensilsCrossed,
        Laundry: Shirt
    };

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
                    src={primaryImageUrl}
                    alt={name}
                    className="h-full w-full object-cover transition-transform duration-200 hover:scale-105"
                    onError={(e) => {
                        (e.target as HTMLImageElement).src = "https://via.placeholder.com/400x300?text=No+Image";
                    }}
                />
            </div>

            <CardContent className="p-4 space-y-2">
                <div className="flex items-start justify-between gap-2">
                    <h3 className="font-semibold text-lg line-clamp-1">{name}</h3>
                    <Badge className={getGenderBadgeColor(forGender)} variant="secondary">
                        {forGender}
                    </Badge>
                </div>

                <div className="flex items-center gap-1 text-sm">
                    <Star className="h-4 w-4 fill-yellow-400 text-yellow-400" />
                    <span className="font-medium">{overallRating.toFixed(1)}</span>
                    <span className="text-muted-foreground">rating</span>
                </div>

                <div className="flex items-center gap-1 text-sm text-muted-foreground">
                    <MapPin className="h-4 w-4" />
                    <span className="line-clamp-1">{location}</span>
                </div>

                <div className="flex items-center justify-between pt-1">
                    <span className="text-sm font-medium text-primary">{rentRange}</span>

                    <div className="flex gap-2">
                        {facilities.slice(0, 4).map((facility) => {
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
