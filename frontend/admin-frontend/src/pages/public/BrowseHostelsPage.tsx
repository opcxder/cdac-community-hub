import { useEffect, useState } from "react";
import client from "@/api/client";
import HostelCard from "@/components/hostel/HostelCard";
import { Skeleton } from "@/components/ui/skeleton";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { Building2 } from "lucide-react";

interface Hostel {
    hostelId: number;
    name: string;
    overallRating: number;
    rentRange: string;
    primaryImageUrl: string;
    location: string;
    facilities: string[];
    forGender: string;
}

export default function BrowseHostelsPage() {
    const [hostels, setHostels] = useState<Hostel[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        async function fetchHostels() {
            setLoading(true);
            setError(null);
            try {
                console.log("🏠 [BROWSE-HOSTELS] Fetching hostels...");
                const response = await client.get<any>("/api/hostel/hostels/approved");
                console.log("🏠 [BROWSE-HOSTELS] Response:", response.data);

                // Map backend field names to frontend expected names
                const mapHostelData = (hostel: any) => ({
                    ...hostel,
                    name: hostel.hostelName || hostel.name || 'Unnamed Hostel',
                    location: hostel.address || hostel.location || hostel.city || 'Location not specified',
                    primaryImageUrl: hostel.primaryImageUrl || (hostel.images && hostel.images.length > 0 ? hostel.images[0] : null),
                    rentRange: hostel.monthlyRentMin && hostel.monthlyRentMax
                        ? `₹${hostel.monthlyRentMin} - ₹${hostel.monthlyRentMax}/month`
                        : 'Contact for pricing',
                    facilities: [
                        hostel.hasWifi && 'WiFi',
                        hostel.hasAc && 'AC',
                        hostel.hasMess && 'Mess',
                        hostel.hasLaundry && 'Laundry'
                    ].filter(Boolean)
                });

                // Handle paginated response (Spring Page object)
                if (response.data && response.data.content && Array.isArray(response.data.content)) {
                    const mappedHostels = response.data.content.map(mapHostelData);
                    setHostels(mappedHostels);
                    console.log("🏠 [BROWSE-HOSTELS] Loaded", mappedHostels.length, "hostels from paginated response");
                }
                // Handle direct array response (fallback)
                else if (Array.isArray(response.data)) {
                    const mappedHostels = response.data.map(mapHostelData);
                    setHostels(mappedHostels);
                    console.log("🏠 [BROWSE-HOSTELS] Loaded", mappedHostels.length, "hostels from array response");
                }
                // Invalid format
                else {
                    console.error("🏠 [BROWSE-HOSTELS] Invalid response format:", response.data);
                    setHostels([]);
                    setError("Invalid data format received from server");
                }
            } catch (err: any) {
                console.error("🏠 [BROWSE-HOSTELS] Error:", err);
                setError(err?.response?.data?.message || "Failed to load hostels");
            } finally {
                setLoading(false);
            }
        }

        fetchHostels();
    }, []);

    return (
        <div className="min-h-screen bg-gradient-to-b from-background to-muted/20">
            {/* Header */}
            <div className="border-b bg-white shadow-sm">
                <div className="container mx-auto px-3 sm:px-4 py-4 sm:py-6">
                    <div className="flex items-center gap-3">
                        <Building2 className="h-6 w-6 sm:h-8 sm:w-8 text-primary" />
                        <div>
                            <h1 className="text-2xl sm:text-3xl font-bold">Browse Hostels</h1>
                            <p className="text-sm text-muted-foreground">Find the perfect accommodation near CDAC</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* Content */}
            <div className="container mx-auto px-3 sm:px-4 py-6 sm:py-8">
                {loading ? (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6">
                        {[...Array(6)].map((_, i) => (
                            <div key={i} className="space-y-3">
                                <Skeleton className="aspect-video w-full rounded-xl" />
                                <Skeleton className="h-6 w-3/4" />
                                <Skeleton className="h-4 w-1/2" />
                                <Skeleton className="h-4 w-2/3" />
                            </div>
                        ))}
                    </div>
                ) : error ? (
                    <Alert variant="destructive" className="rounded-xl">
                        <AlertDescription>{error}</AlertDescription>
                    </Alert>
                ) : hostels.length === 0 ? (
                    <div className="text-center py-12">
                        <Building2 className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
                        <h3 className="text-lg font-semibold mb-2">No hostels yet</h3>
                        <p className="text-sm text-muted-foreground">
                            Be the first to add a hostel to the community!
                        </p>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6">
                        {hostels.map((hostel) => (
                            <HostelCard key={hostel.hostelId} {...hostel} />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}
