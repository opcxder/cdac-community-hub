import { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from '@/components/ui/dialog';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Check, X, Clock } from 'lucide-react';
import { LoadingSpinner, EmptyState } from '@/components/shared';
import { adminService } from '@/api/services';
import type { FoodCategory } from '@/types';
import { useToast } from '@/hooks/use-toast';

/**
 * Food Category Management Page
 * Admin page for approving/rejecting food category suggestions
 */
export function FoodCategoryManagement() {
    const [categories, setCategories] = useState<FoodCategory[]>([]);
    const [loading, setLoading] = useState(true);
    const [selectedCategory, setSelectedCategory] = useState<FoodCategory | null>(null);
    const [rejectDialogOpen, setRejectDialogOpen] = useState(false);
    const [rejectionReason, setRejectionReason] = useState('');
    const [actionLoading, setActionLoading] = useState(false);
    const { toast } = useToast();

    const fetchCategories = async () => {
        try {
            setLoading(true);
            const data = await adminService.getPendingFoodCategories();
            setCategories(data);
        } catch (error) {
            toast({
                title: 'Error',
                description: 'Failed to fetch pending categories',
                variant: 'destructive',
            });
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    const handleApprove = async (categoryId: number) => {
        try {
            setActionLoading(true);
            await adminService.approveFoodCategory(categoryId);
            toast({
                title: 'Success',
                description: 'Category approved successfully',
            });
            fetchCategories(); // Refresh list
        } catch (error) {
            toast({
                title: 'Error',
                description: 'Failed to approve category',
                variant: 'destructive',
            });
        } finally {
            setActionLoading(false);
        }
    };

    const handleRejectClick = (category: FoodCategory) => {
        setSelectedCategory(category);
        setRejectionReason('');
        setRejectDialogOpen(true);
    };

    const handleRejectConfirm = async () => {
        if (!selectedCategory || !rejectionReason.trim()) {
            toast({
                title: 'Error',
                description: 'Please provide a rejection reason',
                variant: 'destructive',
            });
            return;
        }

        try {
            setActionLoading(true);
            await adminService.rejectFoodCategory(selectedCategory.categoryId, rejectionReason);
            toast({
                title: 'Success',
                description: 'Category rejected successfully',
            });
            setRejectDialogOpen(false);
            setSelectedCategory(null);
            setRejectionReason('');
            fetchCategories(); // Refresh list
        } catch (error) {
            toast({
                title: 'Error',
                description: 'Failed to reject category',
                variant: 'destructive',
            });
        } finally {
            setActionLoading(false);
        }
    };

    if (loading) {
        return <LoadingSpinner fullPage text="Loading pending categories..." />;
    }

    return (
        <div className="container mx-auto py-6">
            <div className="mb-6">
                <h1 className="text-3xl font-bold">Food Category Management</h1>
                <p className="text-muted-foreground">
                    Review and approve/reject food category suggestions from users
                </p>
            </div>

            {categories.length === 0 ? (
                <EmptyState
                    icon={<Check className="h-12 w-12 text-green-500" />}
                    title="No pending categories"
                    description="All food category suggestions have been reviewed"
                />
            ) : (
                <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
                    {categories.map((category) => (
                        <Card key={category.categoryId}>
                            <CardHeader>
                                <CardTitle className="flex items-center justify-between">
                                    <span className="line-clamp-1">{category.categoryName}</span>
                                    <Badge variant="secondary" className="bg-yellow-100 text-yellow-800">
                                        <Clock className="mr-1 h-3 w-3" />
                                        PENDING
                                    </Badge>
                                </CardTitle>
                            </CardHeader>
                            <CardContent className="space-y-4">
                                <div className="text-sm text-muted-foreground">
                                    <p>
                                        <span className="font-medium">Suggested by:</span> User #{category.createdByUserId}
                                    </p>
                                    <p>
                                        <span className="font-medium">Created:</span>{' '}
                                        {new Date(category.createdAt).toLocaleDateString()}
                                    </p>
                                </div>

                                <div className="flex gap-2">
                                    <Button
                                        size="sm"
                                        className="flex-1"
                                        onClick={() => handleApprove(category.categoryId)}
                                        disabled={actionLoading}
                                    >
                                        <Check className="mr-1 h-4 w-4" />
                                        Approve
                                    </Button>
                                    <Button
                                        size="sm"
                                        variant="destructive"
                                        className="flex-1"
                                        onClick={() => handleRejectClick(category)}
                                        disabled={actionLoading}
                                    >
                                        <X className="mr-1 h-4 w-4" />
                                        Reject
                                    </Button>
                                </div>
                            </CardContent>
                        </Card>
                    ))}
                </div>
            )}

            {/* Rejection Dialog */}
            <Dialog open={rejectDialogOpen} onOpenChange={setRejectDialogOpen}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Reject Category</DialogTitle>
                        <DialogDescription>
                            Please provide a reason for rejecting "{selectedCategory?.categoryName}". This will be
                            visible to the user who suggested it.
                        </DialogDescription>
                    </DialogHeader>

                    <div className="space-y-2">
                        <Label htmlFor="rejection-reason">Rejection Reason *</Label>
                        <Textarea
                            id="rejection-reason"
                            placeholder="e.g., Duplicate category, inappropriate name, too specific..."
                            value={rejectionReason}
                            onChange={(e) => setRejectionReason(e.target.value)}
                            rows={4}
                            className="resize-none"
                        />
                        <p className="text-xs text-muted-foreground">
                            {rejectionReason.length}/500 characters
                        </p>
                    </div>

                    <DialogFooter>
                        <Button variant="outline" onClick={() => setRejectDialogOpen(false)}>
                            Cancel
                        </Button>
                        <Button
                            variant="destructive"
                            onClick={handleRejectConfirm}
                            disabled={!rejectionReason.trim() || actionLoading}
                        >
                            {actionLoading ? 'Rejecting...' : 'Reject Category'}
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    );
}
