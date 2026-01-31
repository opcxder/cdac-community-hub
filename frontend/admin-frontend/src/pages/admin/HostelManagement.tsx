import { useState, useEffect } from 'react';
import { adminService } from '@/api/services';
import type { Hostel } from '@/types';
import { useToast } from '@/hooks/use-toast';
import {
    LoadingSpinner,
    EmptyState,
    HostelCard,
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
import { Home, Check, X, Eye, Wifi, Wind, UtensilsCrossed as Mess, Shirt } from 'lucide-react';

/**
 * Hostel Management Page
 * Admin interface for approving/rejecting hostel submissions
 */
export function HostelManagement() {
    const [hostels, setHostels] = useState<Hostel[]>([]);
    const [loading, setLoading] = useState(true);
    const [actionLoading, setActionLoading] = useState<number | null>(null);
    const [rejectDialogOpen, setRejectDialogOpen] = useState(false);
    const [detailsDialogOpen, setDetailsDialogOpen] = useState(false);
    const [selectedHostel, setSelectedHostel] = useState<Hostel | null>(null);
    const [rejectionReason, setRejectionReason] = useState('');
    const { toast } = useToast();

    const fetchHostels = async () => {
        try {
            setLoading(true);
            const data = await adminService.getPendingHostels();
            setHostels(data);
        } catch (error) {
            toast({
                title: 'Error',
                description: 'Failed to fetch hostels',
                variant: 'destructive',
            });
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchHostels();
    }, []);

    const handleApprove = async (hostelId: number) => {
        try {
            setActionLoading(hostelId);
            await adminService.approveHostel(hostelId);
            toast({
                title: 'Success',
                description: 'Hostel approved successfully',
            });
            await fetchHostels();
        } catch (error) {
            toast({
                title: 'Error',
                description: 'Failed to approve hostel',
                variant: 'destructive',
            });
        } finally {
            setActionLoading(null);
        }
    };

    const handleRejectClick = (hostel: Hostel) => {
        setSelectedHostel(hostel);
        setRejectionReason('');
        setRejectDialogOpen(true);
    };

    const handleRejectConfirm = async () => {
        if (!selectedHostel || !rejectionReason.trim()) {
            toast({
                title: 'Validation Error',
                description: 'Please provide a rejection reason',
                variant: 'destructive',
            });
            return;
        }

        try {
            setActionLoading(selectedHostel.hostelId);
            await adminService.rejectHostel(selectedHostel.hostelId, rejectionReason);
            toast({
                title: 'Success',
                description: 'Hostel rejected successfully',
            });
            setRejectDialogOpen(false);
            setSelectedHostel(null);
            setRejectionReason('');
            await fetchHostels();
        } catch (error) {
            toast({
                title: 'Error',
                description: 'Failed to reject hostel',
                variant: 'destructive',
            });
        } finally {
            setActionLoading(null);
        }
    };

    const handleViewDetails = (hostel: Hostel) => {
        setSelectedHostel(hostel);
        setDetailsDialogOpen(true);
    };

    if (loading) {
        return <LoadingSpinner fullPage text="Loading hostels..." />;
    }

    return (
        <div className="container mx-auto py-6">
            {/* Header */}
            <div className="mb-6">
                <h1 className="text-3xl font-bold">Hostel Management</h1>
                <p className="text-muted-foreground">Review and approve hostel submissions</p>
            </div>

            {hostels.length === 0 ? (
                <EmptyState
                    icon={<Home />}
                    title="No pending submissions"
                    description="All hostel submissions have been reviewed"
                />
            ) : (
                <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                    {hostels.map((hostel) => (
                        <div key={hostel.hostelId} className="relative">
                            <HostelCard hostel={hostel} />
                            <div className="mt-3 flex gap-2">
                                <Button
                                    size="sm"
                                    variant="outline"
                                    className="flex-1"
                                    onClick={() => handleViewDetails(hostel)}
                                >
                                    <Eye className="mr-1 h-4 w-4" />
                                    Details
                                </Button>
                                <Button
                                    size="sm"
                                    variant="outline"
                                    className="border-green-600 text-green-600 hover:bg-green-50"
                                    onClick={() => handleApprove(hostel.hostelId)}
                                    disabled={actionLoading === hostel.hostelId}
                                >
                                    <Check className="mr-1 h-4 w-4" />
                                    Approve
                                </Button>
                                <Button
                                    size="sm"
                                    variant="outline"
                                    className="border-red-600 text-red-600 hover:bg-red-50"
                                    onClick={() => handleRejectClick(hostel)}
                                    disabled={actionLoading === hostel.hostelId}
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
                        <DialogTitle>{selectedHostel?.hostelName}</DialogTitle>
                        <DialogDescription>Complete submission details</DialogDescription>
                    </DialogHeader>
                    {selectedHostel && (
                        <div className="space-y-4 py-4">
                            <div>
                                <h3 className="font-semibold">Description</h3>
                                <p className="text-sm text-muted-foreground">
                                    {selectedHostel.description || 'No description provided'}
                                </p>
                            </div>
                            <div>
                                <h3 className="font-semibold">Location</h3>
                                <p className="text-sm text-muted-foreground">{selectedHostel.address}</p>
                                {selectedHostel.city && (
                                    <p className="text-sm text-muted-foreground">City: {selectedHostel.city}</p>
                                )}
                                {selectedHostel.locality && (
                                    <p className="text-sm text-muted-foreground">
                                        Locality: {selectedHostel.locality}
                                    </p>
                                )}
                                {selectedHostel.landmark && (
                                    <p className="text-sm text-muted-foreground">
                                        Landmark: {selectedHostel.landmark}
                                    </p>
                                )}
                                {selectedHostel.distanceFromCdac && (
                                    <p className="text-sm text-muted-foreground">
                                        Distance from CDAC: {selectedHostel.distanceFromCdac}
                                    </p>
                                )}
                            </div>
                            <div>
                                <h3 className="font-semibold">Rent & Capacity</h3>
                                {selectedHostel.monthlyRentMin && selectedHostel.monthlyRentMax && (
                                    <p className="text-sm text-muted-foreground">
                                        Rent: ₹{selectedHostel.monthlyRentMin} - ₹{selectedHostel.monthlyRentMax}/month
                                    </p>
                                )}
                                {selectedHostel.roomCapacity && (
                                    <p className="text-sm text-muted-foreground">
                                        Capacity: {selectedHostel.roomCapacity} persons/room
                                    </p>
                                )}
                            </div>
                            {selectedHostel.contactPersonName && (
                                <div>
                                    <h3 className="font-semibold">Contact Person</h3>
                                    <p className="text-sm text-muted-foreground">{selectedHostel.contactPersonName}</p>
                                    {selectedHostel.contactPersonPhone && (
                                        <p className="text-sm text-muted-foreground">
                                            {selectedHostel.contactPersonPhone}
                                        </p>
                                    )}
                                </div>
                            )}
                            <div>
                                <h3 className="font-semibold mb-2">Amenities</h3>
                                <div className="grid grid-cols-2 gap-2">
                                    {selectedHostel.hasWifi && (
                                        <div className="flex items-center gap-2 text-sm">
                                            <Wifi className="h-4 w-4 text-primary" />
                                            <span>WiFi</span>
                                        </div>
                                    )}
                                    {selectedHostel.hasAc && (
                                        <div className="flex items-center gap-2 text-sm">
                                            <Wind className="h-4 w-4 text-primary" />
                                            <span>AC</span>
                                        </div>
                                    )}
                                    {selectedHostel.hasMess && (
                                        <div className="flex items-center gap-2 text-sm">
                                            <Mess className="h-4 w-4 text-primary" />
                                            <span>Mess</span>
                                        </div>
                                    )}
                                    {selectedHostel.hasLaundry && (
                                        <div className="flex items-center gap-2 text-sm">
                                            <Shirt className="h-4 w-4 text-primary" />
                                            <span>Laundry</span>
                                        </div>
                                    )}
                                </div>
                            </div>
                            {selectedHostel.categories && selectedHostel.categories.length > 0 && (
                                <div>
                                    <h3 className="font-semibold">Categories</h3>
                                    <div className="flex flex-wrap gap-2 mt-2">
                                        {selectedHostel.categories.map((cat, idx) => (
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
                        <DialogTitle>Reject Hostel</DialogTitle>
                        <DialogDescription>
                            Please provide a reason for rejecting "{selectedHostel?.hostelName}". This will be
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
