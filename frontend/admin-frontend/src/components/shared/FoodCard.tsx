import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { MapPin, DollarSign, Star } from 'lucide-react';
import { StatusBadge } from './StatusBadge';
import type { FoodPlace, PriceRange } from '@/types';

interface FoodCardProps {
    foodPlace: FoodPlace;
    onClick?: () => void;
    showStatus?: boolean;
}

/**
 * Food place card component
 * Displays food place information in a card format
 */
export function FoodCard({ foodPlace, onClick, showStatus = false }: FoodCardProps) {
    const priceRangeIcons: Record<PriceRange, string> = {
        BUDGET: '$',
        MODERATE: '$$',
        EXPENSIVE: '$$$',
    };

    return (
        <Card
            className={`overflow-hidden transition-shadow hover:shadow-lg ${onClick ? 'cursor-pointer' : ''}`}
            onClick={onClick}
        >
            {/* Image */}
            {foodPlace.imageUrls && foodPlace.imageUrls.length > 0 ? (
                <div className="relative h-48 w-full overflow-hidden bg-muted">
                    <img
                        src={foodPlace.imageUrls[0]}
                        alt={foodPlace.placeName}
                        className="h-full w-full object-cover transition-transform hover:scale-105"
                    />
                </div>
            ) : (
                <div className="flex h-48 items-center justify-center bg-muted">
                    <p className="text-sm text-muted-foreground">No image</p>
                </div>
            )}

            <CardHeader className="pb-3">
                <div className="flex items-start justify-between gap-2">
                    <h3 className="line-clamp-1 text-lg font-semibold">{foodPlace.placeName}</h3>
                    {showStatus && <StatusBadge status={foodPlace.status} />}
                </div>
                {foodPlace.description && (
                    <p className="line-clamp-2 text-sm text-muted-foreground">{foodPlace.description}</p>
                )}
            </CardHeader>

            <CardContent className="pb-3">
                {/* Location */}
                {foodPlace.locality && (
                    <div className="mb-2 flex items-center gap-1 text-sm text-muted-foreground">
                        <MapPin className="h-4 w-4" />
                        <span className="line-clamp-1">{foodPlace.locality}</span>
                    </div>
                )}

                {/* Price Range */}
                {foodPlace.priceRange && (
                    <div className="mb-2 flex items-center gap-1 text-sm">
                        <DollarSign className="h-4 w-4 text-green-600" />
                        <span className="font-medium">{priceRangeIcons[foodPlace.priceRange]}</span>
                        <span className="text-muted-foreground">{foodPlace.priceRange}</span>
                    </div>
                )}

                {/* Rating */}
                <div className="mb-3 flex items-center gap-1 text-sm">
                    <Star className="h-4 w-4 fill-yellow-400 text-yellow-400" />
                    <span className="font-medium">{foodPlace.averageRating.toFixed(1)}</span>
                    <span className="text-muted-foreground">/ 5.0</span>
                </div>

                {/* Categories */}
                {foodPlace.categories && foodPlace.categories.length > 0 && (
                    <div className="flex flex-wrap gap-1">
                        {foodPlace.categories.slice(0, 3).map((category) => (
                            <Badge key={category.categoryId} variant="outline" className="text-xs">
                                {category.categoryName}
                            </Badge>
                        ))}
                        {foodPlace.categories.length > 3 && (
                            <Badge variant="outline" className="text-xs">
                                +{foodPlace.categories.length - 3}
                            </Badge>
                        )}
                    </div>
                )}
            </CardContent>

            {foodPlace.rejectionReason && (
                <CardFooter className="border-t bg-red-50 pt-3">
                    <p className="text-sm text-red-600">
                        <span className="font-medium">Rejected:</span> {foodPlace.rejectionReason}
                    </p>
                </CardFooter>
            )}
        </Card>
    );
}
