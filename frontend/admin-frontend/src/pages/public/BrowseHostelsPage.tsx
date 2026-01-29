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
                const response = await client.get<Hostel[]>("/api/hostel/hostels");
                setHostels(response.data);
            } catch (err: any) {
                setError(err?.response?.data?.message || "Failed to load hostels");
            } finally {
                setLoading(false);
            }
        }

        fetchHostels();
    }, []);

    return (
        <div className="min-h-screen bg-background">
            {/* Header */}
            <div className="border-b bg-white">
                <div className="container mx-auto px-4 py-6">
                    <div className="flex items-center gap-3">
                        <Building2 className="h-8 w-8 text-primary" />
                        <div>
                            <h1 className="text-3xl font-bold">Browse Hostels</h1>
                            <p className="text-muted-foreground">Find the perfect accommodation near CDAC</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* Content */}
            <div className="container mx-auto px-4 py-8">
                {loading ? (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {[...Array(6)].map((_, i) => (
                            <div key={i} className="space-y-3">
                                <Skeleton className="aspect-video w-full rounded-lg" />
                                <Skeleton className="h-6 w-3/4" />
                                <Skeleton className="h-4 w-1/2" />
                                <Skeleton className="h-4 w-2/3" />
                            </div>
                        ))}
                    </div>
                ) : error ? (
                    <Alert variant="destructive">
                        <AlertDescription>{error}</AlertDescription>
                    </Alert>
                ) : hostels.length === 0 ? (
                    <div className="text-center py-12">
                        <Building2 className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
                        <h3 className="text-lg font-semibold mb-2">No hostels yet</h3>
                        <p className="text-muted-foreground">
                            Be the first to add a hostel to the community!
                        </p>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {hostels.map((hostel) => (
                            <HostelCard key={hostel.hostelId} {...hostel} />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}
