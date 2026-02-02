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
import { Home, Check, X, Eye } from 'lucide-react';
import { HostelDetailsDialog } from '@/components/admin';

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
        } catch {
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
            toast({ title: 'Success', description: 'Hostel approved successfully' });
            await fetchHostels();
        } catch {
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
            await adminService.rejectHostel(
                selectedHostel.hostelId,
                rejectionReason
            );
            toast({ title: 'Success', description: 'Hostel rejected successfully' });
            setRejectDialogOpen(false);
            setSelectedHostel(null);
            setRejectionReason('');
            await fetchHostels();
        } catch {
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
                <p className="text-muted-foreground">
                    Review and approve hostel submissions
                </p>
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
                        <div key={hostel.hostelId}>
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
            <HostelDetailsDialog
                hostel={selectedHostel}
                open={detailsDialogOpen}
                onOpenChange={setDetailsDialogOpen}
            />

            {/* Rejection Dialog */}
            <Dialog open={rejectDialogOpen} onOpenChange={setRejectDialogOpen}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Reject Hostel</DialogTitle>
                        <DialogDescription>
                            Please provide a reason for rejecting "
                            {selectedHostel?.hostelName}".
                        </DialogDescription>
                    </DialogHeader>

                    <div className="space-y-4 py-4">
                        <Label htmlFor="reason">Rejection Reason *</Label>
                        <Textarea
                            id="reason"
                            value={rejectionReason}
                            onChange={(e) => setRejectionReason(e.target.value)}
                            rows={4}
                        />
                    </div>

                    <DialogFooter>
                        <Button
                            variant="outline"
                            onClick={() => setRejectDialogOpen(false)}
                        >
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
