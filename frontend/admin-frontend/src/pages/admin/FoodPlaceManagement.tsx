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
import { FoodDetailsDialog } from '@/components/admin';

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
            console.log("🍔 [FOOD-ADMIN] Fetching pending food places...");
            const data = await adminService.getPendingFood();
            console.log("🍔 [FOOD-ADMIN] Received data:", {
                count: data.length,
                firstItem: data[0],
                allData: data
            });
            setFoodPlaces(data);
        } catch (error) {
            console.error("❌ [FOOD-ADMIN] Error fetching food places:", error);
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
        <div className="min-h-screen bg-gradient-to-b from-background to-muted/20 py-4 sm:py-6">
            <div className="container mx-auto px-3 sm:px-4">
                {/* Header */}
                <div className="mb-4 sm:mb-6">
                    <h1 className="text-2xl sm:text-3xl font-bold">Food Place Management</h1>
                    <p className="text-sm text-muted-foreground">Review and approve food place submissions</p>
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
                                <FoodCard foodPlace={place} showStatus={true} />
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
                <FoodDetailsDialog
                    foodPlace={selectedPlace}
                    open={detailsDialogOpen}
                    onOpenChange={setDetailsDialogOpen}
                />

                {/* Rejection Dialog */}
                < Dialog open={rejectDialogOpen} onOpenChange={setRejectDialogOpen} >
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
                </Dialog >
            </div >
        </div >
    );
}

