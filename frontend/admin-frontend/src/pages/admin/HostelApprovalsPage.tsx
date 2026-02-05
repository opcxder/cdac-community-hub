// src/pages/admin/HostelApprovalsPage.tsx
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

interface PendingHostel {
  hostelId: number;
  hostelName: string;
  description?: string;
  address: string;
  city: string;
  locality: string;
  landmark?: string;
  mapLocation?: string;
  distanceFromCdac?: string;
  monthlyRentMin: number;
  monthlyRentMax: number;
  hasWifi: boolean;
  hasAc: boolean;
  hasMess: boolean;
  hasLaundry: boolean;
  contactPersonName?: string;
  contactPersonPhone?: string;
  submittedByUserId: number;
  status: "PENDING";
  imageUrls: string[];
  roomCapacities?: number[];
  categories: string[];
  forGender: "BOYS" | "GIRLS" | "BOTH";
  overallRating?: number;
}

type ActionType = "approve" | "reject";

export default function HostelApprovalsPage() {
  const [hostels, setHostels] = useState<PendingHostel[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [selectedHostel, setSelectedHostel] = useState<PendingHostel | null>(null);
  const [actionType, setActionType] = useState<ActionType | null>(null);
  const [actionLoading, setActionLoading] = useState(false);
  const [actionError, setActionError] = useState<string | null>(null);

  // Fetch pending hostels
  useEffect(() => {
    async function fetchPendingHostels() {
      setLoading(true);
      setError(null);
      try {
        const response = await client.get<PendingHostel[]>("/api/admin/hostels/pending");
        setHostels(response.data);
      } catch (err: any) {
        setError(err?.response?.data?.message || "Failed to load pending hostels");
      } finally {
        setLoading(false);
      }
    }
    fetchPendingHostels();
  }, []);

  const openDialog = (hostel: PendingHostel, type: ActionType) => {
    setSelectedHostel(hostel);
    setActionType(type);
    setActionError(null);
  };

  const closeDialog = () => {
    setSelectedHostel(null);
    setActionType(null);
    setActionError(null);
  };

  const handleConfirmAction = async () => {
    if (!selectedHostel || !actionType) return;

    setActionLoading(true);
    setActionError(null);

    try {
      await client.post(`/api/admin/hostels/${selectedHostel.hostelId}/${actionType}`);
      // Remove hostel from table after success
      setHostels((prev) => prev.filter((h) => h.hostelId !== selectedHostel.hostelId));
      closeDialog();
    } catch (err: any) {
      const msg = err?.response?.data?.message || `Failed to ${actionType} hostel`;
      setActionError(msg);
    } finally {
      setActionLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-zinc-900">Pending Hostel Approvals</h1>

      {loading ? (
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Image</TableHead>
              <TableHead>Name</TableHead>
              <TableHead>Location</TableHead>
              <TableHead>Gender</TableHead>
              <TableHead>Submitted By (User)</TableHead>
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
                <TableCell>
                  <Skeleton className="h-6 w-full rounded" />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      ) : error ? (
        <div className="text-red-600 p-4">{error}</div>
      ) : hostels.length === 0 ? (
        <div className="p-4 text-zinc-500">No pending hostels</div>
      ) : (
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Image</TableHead>
              <TableHead>Name</TableHead>
              <TableHead>Location</TableHead>
              <TableHead>Gender</TableHead>
              <TableHead>Submitted By (User)</TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {hostels.map((hostel) => (
              <TableRow
                key={hostel.hostelId}
                className="hover:bg-zinc-50 transition-colors"
              >
                <TableCell>
                  {hostel.imageUrls && hostel.imageUrls.length > 0 ? (
                    <img
                      src={hostel.imageUrls[0]}
                      alt={hostel.hostelName}
                      className="h-12 w-12 rounded object-cover"
                    />
                  ) : (
                    <div className="h-12 w-12 bg-muted rounded flex items-center justify-center text-xs">No image</div>
                  )}
                </TableCell>
                <TableCell>{hostel.hostelName}</TableCell>
                <TableCell>
                  {hostel.categories && hostel.categories.length > 0 ? (
                    <div className="flex flex-wrap gap-1">
                      {hostel.categories.slice(0, 2).map((cat, idx) => (
                        <span key={idx} className="text-xs bg-muted px-2 py-1 rounded">{cat}</span>
                      ))}
                      {hostel.categories.length > 2 && (
                        <span className="text-xs text-muted-foreground">+{hostel.categories.length - 2}</span>
                      )}
                    </div>
                  ) : (
                    <span className="text-muted-foreground">-</span>
                  )}
                </TableCell>
                <TableCell>{hostel.locality}</TableCell>
                <TableCell>{hostel.city}</TableCell>
                <TableCell>{hostel.forGender}</TableCell>
                <TableCell>{hostel.submittedByUserId}</TableCell>
                <TableCell className="space-x-2">
                  <Button
                    variant="outline"
                    size="sm"
                    disabled={actionLoading}
                    className="disabled:opacity-50 disabled:cursor-not-allowed"
                    onClick={() => openDialog(hostel, "approve")}
                  >
                    Approve
                  </Button>
                  <Button
                    variant="destructive"
                    size="sm"
                    disabled={actionLoading}
                    className="disabled:opacity-50 disabled:cursor-not-allowed"
                    onClick={() => openDialog(hostel, "reject")}
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
        open={!!selectedHostel}
        onOpenChange={(open) => !open && closeDialog()}
      >
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>
              {actionType === "approve" ? "Approve Hostel?" : "Reject Hostel?"}
            </AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to {actionType}{" "}
              <span className="font-semibold">{selectedHostel?.hostelName}</span> (User ID: {selectedHostel?.submittedByUserId})?
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
