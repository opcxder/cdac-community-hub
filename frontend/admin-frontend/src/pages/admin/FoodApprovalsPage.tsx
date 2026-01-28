// src/pages/admin/FoodApprovalsPage.tsx
import { useEffect, useState } from "react";
import client from "@/api/client";
import { Button } from "@/components/ui/button";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import { Skeleton } from "@/components/ui/skeleton";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
} from "@/components/ui/alert-dialog";

interface PendingFood {
    foodId: number;
    name: string;
    category: { categoryId: number; name: string };
    location: string;
    imageUrl: string;
    submittedBy: { userId: number; username: string };
    approvalStatus: "PENDING";
}

type ActionType = "approve" | "reject";

export default function FoodApprovalsPage() {
    const [foods, setFoods] = useState<PendingFood[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [selectedFood, setSelectedFood] = useState<PendingFood | null>(null);
    const [actionType, setActionType] = useState<ActionType | null>(null);
    const [actionLoading, setActionLoading] = useState(false);
    const [actionError, setActionError] = useState<string | null>(null);

    // Fetch pending foods
    useEffect(() => {
        async function fetchPendingFoods() {
            setLoading(true);
            setError(null);
            try {
                const response = await client.get<PendingFood[]>("/api/admin/food/pending");
                setFoods(response.data);
            } catch (err: any) {
                setError(err?.response?.data?.message || "Failed to load pending foods");
            } finally {
                setLoading(false);
            }
        }
        fetchPendingFoods();
    }, []);

    const openDialog = (food: PendingFood, type: ActionType) => {
        setSelectedFood(food);
        setActionType(type);
        setActionError(null);
    };

    const closeDialog = () => {
        setSelectedFood(null);
        setActionType(null);
        setActionError(null);
    };

    const handleConfirmAction = async () => {
        if (!selectedFood || !actionType) return;

        setActionLoading(true);
        setActionError(null);

        try {
            await client.post(`/api/admin/food/${selectedFood.foodId}/${actionType}`);
            // Remove food from table after success
            setFoods((prev) => prev.filter((f) => f.foodId !== selectedFood.foodId));
            closeDialog();
        } catch (err: any) {
            const msg = err?.response?.data?.message || `Failed to ${actionType} food`;
            setActionError(msg);
        } finally {
            setActionLoading(false);
        }
    };

    return (
        <div className="space-y-6">
            <h1 className="text-2xl font-bold text-zinc-900">Pending Food Approvals</h1>

            {loading ? (
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Image</TableHead>
                            <TableHead>Name</TableHead>
                            <TableHead>Category</TableHead>
                            <TableHead>Submitted By (User)</TableHead>
                            <TableHead>Location</TableHead>
                            <TableHead>Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {[...Array(5)].map((_, i) => (
                            <TableRow key={i} className="animate-pulse">
                                <TableCell>
                                    <Skeleton className="h-12 w-12 rounded" />
                                </TableCell>
                                <TableCell>
                                    <Skeleton className="h-6 w-full rounded" />
                                </TableCell>
                                <TableCell>
                                    <Skeleton className="h-6 w-full rounded" />
                                </TableCell>
                                <TableCell>
                                    <Skeleton className="h-6 w-full rounded" />
                                </TableCell>
                                <TableCell>
                                    <Skeleton className="h-6 w-full rounded" />
                                </TableCell>
                                <TableCell>
                                    <Skeleton className="h-6 w-full rounded" />
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            ) : error ? (
                <div className="text-red-600 p-4">{error}</div>
            ) : foods.length === 0 ? (
                <div className="p-4 text-zinc-500">No pending food items</div>
            ) : (
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Image</TableHead>
                            <TableHead>Name</TableHead>
                            <TableHead>Category</TableHead>
                            <TableHead>Submitted By (User)</TableHead>
                            <TableHead>Location</TableHead>
                            <TableHead>Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {foods.map((food) => (
                            <TableRow
                                key={food.foodId}
                                className="hover:bg-zinc-50 transition-colors"
                            >
                                <TableCell>
                                    <img
                                        src={food.imageUrl}
                                        alt={food.name}
                                        className="h-12 w-12 rounded object-cover"
                                    />
                                </TableCell>
                                <TableCell>{food.name}</TableCell>
                                <TableCell>{food.category.name}</TableCell>
                                <TableCell>{food.submittedBy.username}</TableCell>
                                <TableCell>{food.location}</TableCell>
                                <TableCell className="space-x-2">
                                    <Button
                                        variant="outline"
                                        size="sm"
                                        disabled={actionLoading}
                                        className="disabled:opacity-50 disabled:cursor-not-allowed"
                                        onClick={() => openDialog(food, "approve")}
                                    >
                                        Approve
                                    </Button>
                                    <Button
                                        variant="destructive"
                                        size="sm"
                                        disabled={actionLoading}
                                        className="disabled:opacity-50 disabled:cursor-not-allowed"
                                        onClick={() => openDialog(food, "reject")}
                                    >
                                        Reject
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            )}

            {/* Shared Confirmation Dialog */}
            <AlertDialog
                open={!!selectedFood}
                onOpenChange={(open) => !open && closeDialog()}
            >
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>
                            {actionType === "approve" ? "Approve Food?" : "Reject Food?"}
                        </AlertDialogTitle>
                        <AlertDialogDescription>
                            Are you sure you want to {actionType}{" "}
                            <span className="font-semibold">{selectedFood?.name}</span> submitted by{" "}
                            <span className="font-semibold">{selectedFood?.submittedBy.username}</span>?
                        </AlertDialogDescription>
                    </AlertDialogHeader>

                    {actionError && <div className="text-red-600 mt-2">{actionError}</div>}

                    <AlertDialogFooter className="space-x-2">
                        <AlertDialogCancel onClick={closeDialog}>Cancel</AlertDialogCancel>
                        <AlertDialogAction
                            onClick={handleConfirmAction}
                            disabled={actionLoading}
                        >
                            {actionLoading
                                ? "Processing..."
                                : actionType === "approve"
                                    ? "Approve"
                                    : "Reject"}
                        </AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </div>
    );
}
