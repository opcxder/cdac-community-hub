import { useState, useEffect } from 'react';
import { adminService } from '@/api/services';
import type { FoodPlace } from '@/types';
import { useToast } from '@/hooks/use-toast';
import {
    LoadingSpinner,
    EmptyState,
    FoodCard,
} from '@/components/shared';
import { Button } from '@/components/ui/button';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from '@/components/ui/dialog';
import { Textarea } from '@/components/ui/textarea';
import { Label } from '@/components/ui/label';
import { UtensilsCrossed, Check, X, Eye } from 'lucide-react';

/**
 * Food Place Management Page
 * Admin interface for approving/rejecting food place submissions
 */
export function FoodPlaceManagement() {
    const [foodPlaces, setFoodPlaces] = useState<FoodPlace[]>([]);
    const [loading, setLoading] = useState(true);
    const [actionLoading, setActionLoading] = useState<number | null>(null);
    const [rejectDialogOpen, setRejectDialogOpen] = useState(false);
    const [detailsDialogOpen, setDetailsDialogOpen] = useState(false);
    const [selectedPlace, setSelectedPlace] = useState<FoodPlace | null>(null);
    const [rejectionReason, setRejectionReason] = useState('');
    const { toast } = useToast();

    const fetchFoodPlaces = async () => {
        try {
            setLoading(true);
            const data = await adminService.getPendingFood();
            setFoodPlaces(data);
        } catch (error) {
            toast({
                title: 'Error',
                description: 'Failed to fetch food places',
                variant: 'destructive',
            });
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchFoodPlaces();
    }, []);

    const handleApprove = async (placeId: number) => {
        try {
            setActionLoading(placeId);
            await adminService.approveFood(placeId);
            toast({
                title: 'Success',
                description: 'Food place approved successfully',
            });
            await fetchFoodPlaces();
        } catch (error) {
            toast({
                title: 'Error',
                description: 'Failed to approve food place',
                variant: 'destructive',
            });
        } finally {
            setActionLoading(null);
        }
    };

    const handleRejectClick = (place: FoodPlace) => {
        setSelectedPlace(place);
        setRejectionReason('');
        setRejectDialogOpen(true);
    };

    const handleRejectConfirm = async () => {
        if (!selectedPlace || !rejectionReason.trim()) {
            toast({
                title: 'Validation Error',
                description: 'Please provide a rejection reason',
                variant: 'destructive',
            });
            return;
        }

        try {
            setActionLoading(selectedPlace.placeId);
            await adminService.rejectFood(selectedPlace.placeId, rejectionReason);
            toast({
                title: 'Success',
                description: 'Food place rejected successfully',
            });
            setRejectDialogOpen(false);
            setSelectedPlace(null);
            setRejectionReason('');
            await fetchFoodPlaces();
        } catch (error) {
            toast({
                title: 'Error',
                description: 'Failed to reject food place',
                variant: 'destructive',
            });
        } finally {
            setActionLoading(null);
        }
    };

    const handleViewDetails = (place: FoodPlace) => {
        setSelectedPlace(place);
        setDetailsDialogOpen(true);
    };

    if (loading) {
        return <LoadingSpinner fullPage text="Loading food places..." />;
    }

    return (
        <div className="container mx-auto py-6">
            {/* Header */}
            <div className="mb-6">
                <h1 className="text-3xl font-bold">Food Place Management</h1>
                <p className="text-muted-foreground">Review and approve food place submissions</p>
            </div>

            {foodPlaces.length === 0 ? (
                <EmptyState
                    icon={<UtensilsCrossed />}
                    title="No pending submissions"
                    description="All food place submissions have been reviewed"
                />
            ) : (
                <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                    {foodPlaces.map((place) => (
                        <div key={place.placeId} className="relative">
                            <FoodCard foodPlace={place} />
                            <div className="mt-3 flex gap-2">
                                <Button
                                    size="sm"
                                    variant="outline"
                                    className="flex-1"
                                    onClick={() => handleViewDetails(place)}
                                >
                                    <Eye className="mr-1 h-4 w-4" />
                                    Details
                                </Button>
                                <Button
                                    size="sm"
                                    variant="outline"
                                    className="border-green-600 text-green-600 hover:bg-green-50"
                                    onClick={() => handleApprove(place.placeId)}
                                    disabled={actionLoading === place.placeId}
                                >
                                    <Check className="mr-1 h-4 w-4" />
                                    Approve
                                </Button>
                                <Button
                                    size="sm"
                                    variant="outline"
                                    className="border-red-600 text-red-600 hover:bg-red-50"
                                    onClick={() => handleRejectClick(place)}
                                    disabled={actionLoading === place.placeId}
                                >
                                    <X className="mr-1 h-4 w-4" />
                                    Reject
                                </Button>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {/* Details Dialog */}
            <Dialog open={detailsDialogOpen} onOpenChange={setDetailsDialogOpen}>
                <DialogContent className="max-w-2xl max-h-[80vh] overflow-y-auto">
                    <DialogHeader>
                        <DialogTitle>{selectedPlace?.placeName}</DialogTitle>
                        <DialogDescription>Complete submission details</DialogDescription>
                    </DialogHeader>
                    {selectedPlace && (
                        <div className="space-y-4 py-4">
                            <div>
                                <h3 className="font-semibold">Description</h3>
                                <p className="text-sm text-muted-foreground">
                                    {selectedPlace.description || 'No description provided'}
                                </p>
                            </div>
                            <div>
                                <h3 className="font-semibold">Location</h3>
                                <p className="text-sm text-muted-foreground">{selectedPlace.address}</p>
                                {selectedPlace.city && (
                                    <p className="text-sm text-muted-foreground">City: {selectedPlace.city}</p>
                                )}
                                {selectedPlace.locality && (
                                    <p className="text-sm text-muted-foreground">
                                        Locality: {selectedPlace.locality}
                                    </p>
                                )}
                                {selectedPlace.landmark && (
                                    <p className="text-sm text-muted-foreground">
                                        Landmark: {selectedPlace.landmark}
                                    </p>
                                )}
                            </div>
                            {selectedPlace.contactInfo && (
                                <div>
                                    <h3 className="font-semibold">Contact</h3>
                                    <p className="text-sm text-muted-foreground">{selectedPlace.contactInfo}</p>
                                </div>
                            )}
                            {selectedPlace.categories && selectedPlace.categories.length > 0 && (
                                <div>
                                    <h3 className="font-semibold">Categories</h3>
                                    <div className="flex flex-wrap gap-2 mt-2">
                                        {selectedPlace.categories.map((cat, idx) => (
                                            <span
                                                key={idx}
                                                className="inline-flex items-center rounded-full bg-primary/10 px-3 py-1 text-xs font-medium text-primary"
                                            >
                                                {typeof cat === 'string' ? cat : cat.categoryName}
                                            </span>
                                        ))}
                                    </div>
                                </div>
                            )}
                            {selectedPlace.imageUrls && selectedPlace.imageUrls.length > 0 && (
                                <div>
                                    <h3 className="font-semibold mb-2">Images</h3>
                                    <div className="grid grid-cols-2 gap-2">
                                        {selectedPlace.imageUrls.map((url, idx) => (
                                            <img
                                                key={idx}
                                                src={url}
                                                alt={`${selectedPlace.placeName} ${idx + 1}`}
                                                className="rounded-md object-cover aspect-video"
                                            />
                                        ))}
                                    </div>
                                </div>
                            )}
                        </div>
                    )}
                    <DialogFooter>
                        <Button variant="outline" onClick={() => setDetailsDialogOpen(false)}>
                            Close
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>

            {/* Rejection Dialog */}
            <Dialog open={rejectDialogOpen} onOpenChange={setRejectDialogOpen}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Reject Food Place</DialogTitle>
                        <DialogDescription>
                            Please provide a reason for rejecting "{selectedPlace?.placeName}". This will be
                            visible to the submitter.
                        </DialogDescription>
                    </DialogHeader>
                    <div className="space-y-4 py-4">
                        <div className="space-y-2">
                            <Label htmlFor="reason">Rejection Reason *</Label>
                            <Textarea
                                id="reason"
                                placeholder="e.g., Incomplete information, duplicate entry, inappropriate content, etc."
                                value={rejectionReason}
                                onChange={(e) => setRejectionReason(e.target.value)}
                                rows={4}
                            />
                        </div>
                    </div>
                    <DialogFooter>
                        <Button variant="outline" onClick={() => setRejectDialogOpen(false)}>
                            Cancel
                        </Button>
                        <Button
                            variant="destructive"
                            onClick={handleRejectConfirm}
                            disabled={!rejectionReason.trim()}
                        >
                            Reject Submission
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    );
}
