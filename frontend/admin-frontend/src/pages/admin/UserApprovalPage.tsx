import { useEffect, useState } from "react";
import client from "@/api/client";
import { Button } from "@/components/ui/button";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
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

interface PendingUser {
    userId: number;
    username: string;
    email: string;
    accountStatus: "PENDING";
}

type ActionType = "approve" | "reject";

export default function UserApprovalsPage() {
    const [users, setUsers] = useState<PendingUser[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [selectedUser, setSelectedUser] = useState<PendingUser | null>(null);
    const [actionType, setActionType] = useState<ActionType | null>(null);
    const [actionLoading, setActionLoading] = useState(false);
    const [actionError, setActionError] = useState<string | null>(null);

    // Fetch pending users
    useEffect(() => {
        async function fetchPendingUsers() {
            setLoading(true);
            setError(null);
            try {
                const response = await client.get<PendingUser[]>("/api/admin/users/pending");
                setUsers(response.data);
            } catch (err: any) {
                const message = err?.response?.data?.message || "Failed to load pending users";
                setError(message);
            } finally {
                setLoading(false);
            }
        }

        fetchPendingUsers();
    }, []);

    // Confirm approve/reject action
    const handleConfirmAction = async () => {
        if (!selectedUser || !actionType) return;

        setActionLoading(true);
        setActionError(null);

        try {
            await client.post(`/api/admin/users/${selectedUser.userId}/${actionType}`);
            // Remove user from table on success
            setUsers((prev) => prev.filter((u) => u.userId !== selectedUser.userId));
            setSelectedUser(null);
            setActionType(null);
        } catch (err: any) {
            const message = err?.response?.data?.message || `Failed to ${actionType} user`;
            setActionError(message);
        } finally {
            setActionLoading(false);
        }
    };

    // Open dialog
    const openDialog = (user: PendingUser, type: ActionType) => {
        setSelectedUser(user);
        setActionType(type);
        setActionError(null);
    };

    // Close dialog
    const closeDialog = () => {
        setSelectedUser(null);
        setActionType(null);
        setActionError(null);
    };

    return (
        <div className="space-y-6">
            <h1 className="text-2xl font-bold">Pending User Approvals</h1>

            {loading ? (
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Username</TableHead>
                            <TableHead>Email</TableHead>
                            <TableHead>Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {[...Array(5)].map((_, i) => (
                            <TableRow key={i} className="animate-pulse">
                                <TableCell><Skeleton className="h-4 w-24 rounded" /></TableCell>
                                <TableCell><Skeleton className="h-4 w-32 rounded" /></TableCell>
                                <TableCell>
                                    <Skeleton className="h-8 w-20 rounded inline-block mr-2" />
                                    <Skeleton className="h-8 w-20 rounded inline-block" />
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            ) : error ? (
                <div className="text-red-600 p-4">{error}</div>
            ) : users.length === 0 ? (
                <div className="p-4 text-zinc-500">No pending users</div>
            ) : (
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Username</TableHead>
                            <TableHead>Email</TableHead>
                            <TableHead>Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {users.map((user) => (
                            <TableRow key={user.userId} className="hover:bg-zinc-50 transition-colors">
                                <TableCell>{user.username}</TableCell>
                                <TableCell>{user.email}</TableCell>
                                <TableCell className="space-x-2">
                                    <Button
                                        variant="outline"
                                        size="sm"
                                        onClick={() => openDialog(user, "approve")}
                                        disabled={actionLoading}
                                        className="disabled:opacity-60 disabled:cursor-not-allowed"
                                    >
                                        Approve
                                    </Button>
                                    <Button
                                        variant="destructive"
                                        size="sm"
                                        onClick={() => openDialog(user, "reject")}
                                        disabled={actionLoading}
                                        className="disabled:opacity-60 disabled:cursor-not-allowed"
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
            <AlertDialog open={!!selectedUser} onOpenChange={(open) => !open && closeDialog()}>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>
                            {actionType === "approve" ? "Approve User?" : "Reject User?"}
                        </AlertDialogTitle>
                        <AlertDialogDescription>
                            Are you sure you want to {actionType}{" "}
                            <span className="font-semibold">{selectedUser?.username}</span>?
                        </AlertDialogDescription>
                    </AlertDialogHeader>

                    {actionError && <div className="text-red-600 mt-2">{actionError}</div>}

                    <AlertDialogFooter className="space-x-2">
                        <AlertDialogCancel onClick={closeDialog}>Cancel</AlertDialogCancel>
                        <AlertDialogAction
                            onClick={handleConfirmAction}
                            disabled={actionLoading}
                        >
                            {actionLoading ? "Processing..." : actionType === "approve" ? "Approve" : "Reject"}
                        </AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </div>
    );
}
