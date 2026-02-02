import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { MapPin, DollarSign } from 'lucide-react';
import type { FoodPlace } from '@/types';

interface FoodDetailsDialogProps {
    foodPlace: FoodPlace | null;
    open: boolean;
    onOpenChange: (open: boolean) => void;
}

export function FoodDetailsDialog({ foodPlace, open, onOpenChange }: FoodDetailsDialogProps) {
    if (!foodPlace) return null;

    const getPriceRangeDisplay = (range?: string) => {
        switch (range) {
            case 'BUDGET':
                return { text: 'Budget Friendly', color: 'bg-green-50 dark:bg-green-950/20 border-green-200 dark:border-green-900 text-green-700 dark:text-green-400' };
            case 'MEDIUM':
                return { text: 'Medium Range', color: 'bg-yellow-50 dark:bg-yellow-950/20 border-yellow-200 dark:border-yellow-900 text-yellow-700 dark:text-yellow-400' };
            case 'EXPENSIVE':
                return { text: 'Premium', color: 'bg-orange-50 dark:bg-orange-950/20 border-orange-200 dark:border-orange-900 text-orange-700 dark:text-orange-400' };
            default:
                return { text: 'Not specified', color: 'bg-muted/30 border-border text-muted-foreground' };
        }
    };

    const priceRange = getPriceRangeDisplay(foodPlace.priceRange);

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="max-w-4xl max-h-[90vh] overflow-hidden p-0">
                <DialogHeader className="px-4 sm:px-6 pt-4 sm:pt-6 pb-3 sm:pb-4 border-b sticky top-0 bg-background z-10">
                    <DialogTitle className="text-lg sm:text-xl pr-8">{foodPlace.placeName}</DialogTitle>
                    <DialogDescription className="text-xs sm:text-sm">
                        Complete food place submission details for review
                    </DialogDescription>
                </DialogHeader>

                <div className="overflow-y-auto max-h-[calc(90vh-180px)] px-4 sm:px-6 py-4">
                    <div className="space-y-4 sm:space-y-6">
                        {/* Image Gallery */}
                        {foodPlace.imageUrls && foodPlace.imageUrls.length > 0 && (
                            <div className="space-y-2 sm:space-y-3">
                                <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                    <span className="h-1 w-1 rounded-full bg-primary"></span>
                                    Images ({foodPlace.imageUrls.length})
                                </h3>
                                <div className="grid grid-cols-2 sm:grid-cols-3 gap-2 sm:gap-3">
                                    {foodPlace.imageUrls.map((url, idx) => (
                                        <div
                                            key={idx}
                                            className="relative aspect-video rounded-lg overflow-hidden border border-border/50 shadow-sm hover:shadow-md transition-all group"
                                        >
                                            <img
                                                src={url}
                                                alt={`${foodPlace.placeName} ${idx + 1}`}
                                                className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300"
                                            />
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}

                        {/* Description */}
                        {foodPlace.description && (
                            <div className="space-y-2">
                                <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                    <span className="h-1 w-1 rounded-full bg-primary"></span>
                                    Description
                                </h3>
                                <p className="text-xs sm:text-sm leading-relaxed bg-muted/30 rounded-lg p-3 sm:p-4">
                                    {foodPlace.description}
                                </p>
                            </div>
                        )}

                        {/* Location Details */}
                        <div className="space-y-2">
                            <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                <MapPin className="h-3 w-3 sm:h-4 sm:w-4" />
                                Location Details
                            </h3>
                            <div className="bg-muted/30 rounded-lg p-3 sm:p-4 space-y-2 sm:space-y-3">
                                <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 sm:gap-3">
                                    <div>
                                        <p className="text-xs text-muted-foreground mb-1">Address</p>
                                        <p className="text-xs sm:text-sm font-medium">{foodPlace.address}</p>
                                    </div>
                                    {foodPlace.city && (
                                        <div>
                                            <p className="text-xs text-muted-foreground mb-1">City</p>
                                            <p className="text-xs sm:text-sm font-medium">{foodPlace.city}</p>
                                        </div>
                                    )}
                                    {foodPlace.locality && (
                                        <div>
                                            <p className="text-xs text-muted-foreground mb-1">Locality</p>
                                            <p className="text-xs sm:text-sm font-medium">{foodPlace.locality}</p>
                                        </div>
                                    )}
                                    {foodPlace.landmark && (
                                        <div>
                                            <p className="text-xs text-muted-foreground mb-1">Landmark</p>
                                            <p className="text-xs sm:text-sm font-medium">{foodPlace.landmark}</p>
                                        </div>
                                    )}
                                    {foodPlace.mapLocation && (
                                        <div className="sm:col-span-2">
                                            <p className="text-xs text-muted-foreground mb-1">Map Location</p>
                                            <p className="text-xs sm:text-sm font-medium text-primary">
                                                {foodPlace.mapLocation}
                                            </p>
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>

                        {/* Price Range & Contact */}
                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4">
                            {foodPlace.priceRange && (
                                <div className="space-y-2">
                                    <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                        <DollarSign className="h-3 w-3 sm:h-4 sm:w-4" />
                                        Price Range
                                    </h3>
                                    <div className={`rounded-lg p-3 sm:p-4 border ${priceRange.color}`}>
                                        <p className="text-base sm:text-lg font-bold">{priceRange.text}</p>
                                        <p className="text-xs text-muted-foreground mt-1">
                                            {foodPlace.priceRange}
                                        </p>
                                    </div>
                                </div>
                            )}
                            {foodPlace.contactInfo && (
                                <div className="space-y-2">
                                    <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                        <span className="h-1 w-1 rounded-full bg-primary"></span>
                                        Contact Information
                                    </h3>
                                    <div className="bg-muted/30 rounded-lg p-3 sm:p-4">
                                        <p className="text-xs sm:text-sm font-medium">{foodPlace.contactInfo}</p>
                                    </div>
                                </div>
                            )}
                        </div>

                        {/* Categories */}
                        {foodPlace.categories && foodPlace.categories.length > 0 && (
                            <div className="space-y-2">
                                <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                    <span className="h-1 w-1 rounded-full bg-primary"></span>
                                    Categories
                                </h3>
                                <div className="flex flex-wrap gap-2">
                                    {foodPlace.categories.map((cat, idx) => (
                                        <span
                                            key={idx}
                                            className="inline-flex items-center rounded-full bg-primary/10 px-3 sm:px-4 py-1.5 sm:py-2 text-xs sm:text-sm font-medium text-primary border border-primary/20"
                                        >
                                            {typeof cat === 'string' ? cat : cat.categoryName}
                                        </span>
                                    ))}
                                </div>
                            </div>
                        )}
                    </div>
                </div>

                <DialogFooter className="px-4 sm:px-6 py-3 sm:py-4 border-t bg-muted/20 sticky bottom-0">
                    <Button variant="outline" onClick={() => onOpenChange(false)} className="w-full sm:w-auto">
                        Close
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}
