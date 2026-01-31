import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { MapPin, DollarSign, Wifi, Wind, UtensilsCrossed, Shirt } from 'lucide-react';
import { StatusBadge } from './StatusBadge';
import type { Hostel } from '@/types';

interface HostelCardProps {
    hostel: Hostel;
    onClick?: () => void;
    showStatus?: boolean;
}

/**
 * Hostel card component
 * Displays hostel information in a card format
 */
export function HostelCard({ hostel, onClick, showStatus = false }: HostelCardProps) {
    const amenities = [
        { key: 'hasWifi', icon: Wifi, label: 'WiFi' },
        { key: 'hasAc', icon: Wind, label: 'AC' },
        { key: 'hasMess', icon: UtensilsCrossed, label: 'Mess' },
        { key: 'hasLaundry', icon: Shirt, label: 'Laundry' },
    ];

    const activeAmenities = amenities.filter((amenity) => hostel[amenity.key as keyof Hostel]);

    return (
        <Card
            className={`overflow-hidden transition-shadow hover:shadow-lg ${onClick ? 'cursor-pointer' : ''}`}
            onClick={onClick}
        >
            <CardHeader className="pb-3">
                <div className="flex items-start justify-between gap-2">
                    <h3 className="line-clamp-1 text-lg font-semibold">{hostel.hostelName}</h3>
                    {showStatus && <StatusBadge status={hostel.status} />}
                </div>
                {hostel.description && (
                    <p className="line-clamp-2 text-sm text-muted-foreground">{hostel.description}</p>
                )}
            </CardHeader>

            <CardContent className="pb-3">
                {/* Location */}
                {hostel.locality && (
                    <div className="mb-2 flex items-center gap-1 text-sm text-muted-foreground">
                        <MapPin className="h-4 w-4" />
                        <span className="line-clamp-1">{hostel.locality}</span>
                    </div>
                )}

                {/* Distance from CDAC */}
                {hostel.distanceFromCdac && (
                    <div className="mb-2 text-sm">
                        <span className="font-medium">Distance:</span>{' '}
                        <span className="text-muted-foreground">{hostel.distanceFromCdac}</span>
                    </div>
                )}

                {/* Rent Range */}
                {(hostel.monthlyRentMin || hostel.monthlyRentMax) && (
                    <div className="mb-3 flex items-center gap-1 text-sm">
                        <DollarSign className="h-4 w-4 text-green-600" />
                        <span className="font-medium">
                            ₹{hostel.monthlyRentMin?.toLocaleString()}
                            {hostel.monthlyRentMax && ` - ₹${hostel.monthlyRentMax.toLocaleString()}`}
                        </span>
                        <span className="text-muted-foreground">/month</span>
                    </div>
                )}

                {/* Amenities */}
                {activeAmenities.length > 0 && (
                    <div className="mb-3 flex flex-wrap gap-2">
                        {activeAmenities.map((amenity) => (
                            <div
                                key={amenity.key}
                                className="flex items-center gap-1 rounded-md bg-muted px-2 py-1 text-xs"
                            >
                                <amenity.icon className="h-3 w-3" />
                                <span>{amenity.label}</span>
                            </div>
                        ))}
                    </div>
                )}

                {/* Room Capacity */}
                {hostel.roomCapacity && (
                    <div className="text-sm">
                        <span className="font-medium">Capacity:</span>{' '}
                        <span className="text-muted-foreground">{hostel.roomCapacity} persons</span>
                    </div>
                )}

                {/* Categories */}
                {hostel.categories && hostel.categories.length > 0 && (
                    <div className="mt-3 flex flex-wrap gap-1">
                        {hostel.categories.slice(0, 3).map((category) => (
                            <Badge key={category.categoryId} variant="outline" className="text-xs">
                                {category.categoryName}
                            </Badge>
                        ))}
                        {hostel.categories.length > 3 && (
                            <Badge variant="outline" className="text-xs">
                                +{hostel.categories.length - 3}
                            </Badge>
                        )}
                    </div>
                )}
            </CardContent>

            {hostel.rejectionReason && (
                <CardFooter className="border-t bg-red-50 pt-3">
                    <p className="text-sm text-red-600">
                        <span className="font-medium">Rejected:</span> {hostel.rejectionReason}
                    </p>
                </CardFooter>
            )}
        </Card>
    );
}
