import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { MapPin, DollarSign, Phone, Home, Wifi, Wind, UtensilsCrossed as Mess, Shirt } from 'lucide-react';
import type { Hostel } from '@/types';

interface HostelDetailsDialogProps {
    hostel: Hostel | null;
    open: boolean;
    onOpenChange: (open: boolean) => void;
}

export function HostelDetailsDialog({ hostel, open, onOpenChange }: HostelDetailsDialogProps) {
    if (!hostel) return null;

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="max-w-4xl max-h-[90vh] overflow-hidden p-0">
                <DialogHeader className="px-4 sm:px-6 pt-4 sm:pt-6 pb-3 sm:pb-4 border-b sticky top-0 bg-background z-10">
                    <DialogTitle className="text-lg sm:text-xl pr-8">{hostel.hostelName}</DialogTitle>
                    <DialogDescription className="text-xs sm:text-sm">
                        Complete hostel submission details for review
                    </DialogDescription>
                </DialogHeader>

                <div className="overflow-y-auto max-h-[calc(90vh-180px)] px-4 sm:px-6 py-4">
                    <div className="space-y-4 sm:space-y-6">
                        {/* Image Gallery */}
                        {hostel.imageUrls && hostel.imageUrls.length > 0 && (
                            <div className="space-y-2 sm:space-y-3">
                                <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                    <span className="h-1 w-1 rounded-full bg-primary"></span>
                                    Images ({hostel.imageUrls.length})
                                </h3>
                                <div className="grid grid-cols-2 sm:grid-cols-3 gap-2 sm:gap-3">
                                    {hostel.imageUrls.map((url, idx) => (
                                        <div
                                            key={idx}
                                            className="relative aspect-video rounded-lg overflow-hidden border border-border/50 shadow-sm hover:shadow-md transition-all group"
                                        >
                                            <img
                                                src={url}
                                                alt={`${hostel.hostelName} ${idx + 1}`}
                                                className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300"
                                            />
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}

                        {/* Description */}
                        {hostel.description && (
                            <div className="space-y-2">
                                <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                    <span className="h-1 w-1 rounded-full bg-primary"></span>
                                    Description
                                </h3>
                                <p className="text-xs sm:text-sm leading-relaxed bg-muted/30 rounded-lg p-3 sm:p-4">
                                    {hostel.description}
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
                                        <p className="text-xs sm:text-sm font-medium">{hostel.address}</p>
                                    </div>
                                    {hostel.city && (
                                        <div>
                                            <p className="text-xs text-muted-foreground mb-1">City</p>
                                            <p className="text-xs sm:text-sm font-medium">{hostel.city}</p>
                                        </div>
                                    )}
                                    {hostel.locality && (
                                        <div>
                                            <p className="text-xs text-muted-foreground mb-1">Locality</p>
                                            <p className="text-xs sm:text-sm font-medium">{hostel.locality}</p>
                                        </div>
                                    )}
                                    {hostel.landmark && (
                                        <div>
                                            <p className="text-xs text-muted-foreground mb-1">Landmark</p>
                                            <p className="text-xs sm:text-sm font-medium">{hostel.landmark}</p>
                                        </div>
                                    )}
                                    {hostel.distanceFromCdac && (
                                        <div className="sm:col-span-2">
                                            <p className="text-xs text-muted-foreground mb-1">Distance from CDAC</p>
                                            <p className="text-xs sm:text-sm font-medium text-primary">
                                                {hostel.distanceFromCdac}
                                            </p>
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>

                        {/* Rent & Capacity */}
                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4">
                            {(hostel.monthlyRentMin || hostel.monthlyRentMax) && (
                                <div className="space-y-2">
                                    <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                        <DollarSign className="h-3 w-3 sm:h-4 sm:w-4" />
                                        Monthly Rent
                                    </h3>
                                    <div className="bg-green-50 dark:bg-green-950/20 rounded-lg p-3 sm:p-4 border border-green-200 dark:border-green-900">
                                        <p className="text-base sm:text-lg font-bold text-green-700 dark:text-green-400">
                                            ₹{hostel.monthlyRentMin?.toLocaleString()} - ₹
                                            {hostel.monthlyRentMax?.toLocaleString()}
                                        </p>
                                        <p className="text-xs text-muted-foreground mt-1">per month</p>
                                    </div>
                                </div>
                            )}
                            {hostel.roomCapacity && (
                                <div className="space-y-2">
                                    <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                        <Home className="h-3 w-3 sm:h-4 sm:w-4" />
                                        Room Capacity
                                    </h3>
                                    <div className="bg-blue-50 dark:bg-blue-950/20 rounded-lg p-3 sm:p-4 border border-blue-200 dark:border-blue-900">
                                        <p className="text-base sm:text-lg font-bold text-blue-700 dark:text-blue-400">
                                            {hostel.roomCapacity} persons
                                        </p>
                                        <p className="text-xs text-muted-foreground mt-1">per room</p>
                                    </div>
                                </div>
                            )}
                        </div>

                        {/* Amenities */}
                        <div className="space-y-2">
                            <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                <span className="h-1 w-1 rounded-full bg-primary"></span>
                                Amenities
                            </h3>
                            <div className="grid grid-cols-2 sm:grid-cols-4 gap-2 sm:gap-3">
                                <div
                                    className={`flex items-center gap-2 p-2 sm:p-3 rounded-lg border ${hostel.hasWifi
                                        ? 'bg-primary/10 border-primary/20'
                                        : 'bg-muted/30 border-border/50 opacity-50'
                                        }`}
                                >
                                    <Wifi
                                        className={`h-4 w-4 sm:h-5 sm:w-5 ${hostel.hasWifi ? 'text-primary' : 'text-muted-foreground'
                                            }`}
                                    />
                                    <span className="text-xs sm:text-sm font-medium">WiFi</span>
                                </div>
                                <div
                                    className={`flex items-center gap-2 p-2 sm:p-3 rounded-lg border ${hostel.hasAc
                                        ? 'bg-primary/10 border-primary/20'
                                        : 'bg-muted/30 border-border/50 opacity-50'
                                        }`}
                                >
                                    <Wind
                                        className={`h-4 w-4 sm:h-5 sm:w-5 ${hostel.hasAc ? 'text-primary' : 'text-muted-foreground'
                                            }`}
                                    />
                                    <span className="text-xs sm:text-sm font-medium">AC</span>
                                </div>
                                <div
                                    className={`flex items-center gap-2 p-2 sm:p-3 rounded-lg border ${hostel.hasMess
                                        ? 'bg-primary/10 border-primary/20'
                                        : 'bg-muted/30 border-border/50 opacity-50'
                                        }`}
                                >
                                    <Mess
                                        className={`h-4 w-4 sm:h-5 sm:w-5 ${hostel.hasMess ? 'text-primary' : 'text-muted-foreground'
                                            }`}
                                    />
                                    <span className="text-xs sm:text-sm font-medium">Mess</span>
                                </div>
                                <div
                                    className={`flex items-center gap-2 p-2 sm:p-3 rounded-lg border ${hostel.hasLaundry
                                        ? 'bg-primary/10 border-primary/20'
                                        : 'bg-muted/30 border-border/50 opacity-50'
                                        }`}
                                >
                                    <Shirt
                                        className={`h-4 w-4 sm:h-5 sm:w-5 ${hostel.hasLaundry ? 'text-primary' : 'text-muted-foreground'
                                            }`}
                                    />
                                    <span className="text-xs sm:text-sm font-medium">Laundry</span>
                                </div>
                            </div>
                        </div>

                        {/* Contact Information */}
                        {(hostel.contactPersonName || hostel.contactPersonPhone) && (
                            <div className="space-y-2">
                                <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                    <Phone className="h-3 w-3 sm:h-4 sm:w-4" />
                                    Contact Information
                                </h3>
                                <div className="bg-muted/30 rounded-lg p-3 sm:p-4 space-y-2">
                                    {hostel.contactPersonName && (
                                        <div>
                                            <p className="text-xs text-muted-foreground mb-1">Contact Person</p>
                                            <p className="text-xs sm:text-sm font-medium">{hostel.contactPersonName}</p>
                                        </div>
                                    )}
                                    {hostel.contactPersonPhone && (
                                        <div>
                                            <p className="text-xs text-muted-foreground mb-1">Phone Number</p>
                                            <p className="text-xs sm:text-sm font-medium">{hostel.contactPersonPhone}</p>
                                        </div>
                                    )}
                                </div>
                            </div>
                        )}

                        {/* Categories */}
                        {hostel.categories && hostel.categories.length > 0 && (
                            <div className="space-y-2">
                                <h3 className="text-xs sm:text-sm font-semibold text-muted-foreground uppercase tracking-wide flex items-center gap-2">
                                    <span className="h-1 w-1 rounded-full bg-primary"></span>
                                    Categories
                                </h3>
                                <div className="flex flex-wrap gap-2">
                                    {hostel.categories.map((cat, idx) => (
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
