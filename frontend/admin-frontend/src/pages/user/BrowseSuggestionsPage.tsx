import { useState, useEffect } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { LoadingSpinner, EmptyState } from "@/components/shared";
import { useToast } from "@/hooks/use-toast";
import client from "@/api/client";
import { MessageSquare, Calendar, User } from "lucide-react";
import { format } from "date-fns";

interface Suggestion {
    suggestionId: number;
    suggestionText: string;
    category: string;
    userId: number;
    createdAt: string;
    username?: string;
}

export default function BrowseSuggestionsPage() {
    const [suggestions, setSuggestions] = useState<Suggestion[]>([]);
    const [loading, setLoading] = useState(true);
    const { toast } = useToast();

    useEffect(() => {
        const fetchSuggestions = async () => {
            try {
                setLoading(true);
                console.log("💡 [SUGGESTIONS-BROWSE] Fetching suggestions from /api/suggestions...");
                const response = await client.get<Suggestion[]>("/api/suggestions");
                console.log("💡 [SUGGESTIONS-BROWSE] Response:", {
                    status: response.status,
                    dataCount: response.data?.length,
                    data: response.data
                });
                setSuggestions(response.data);
            } catch (error: any) {
                console.error("❌ [SUGGESTIONS-BROWSE] Error:", {
                    message: error.message,
                    response: error.response?.data,
                    status: error.response?.status
                });
                toast({
                    title: "Error",
                    description: "Failed to fetch suggestions",
                    variant: "destructive",
                });
            } finally {
                setLoading(false);
            }
        };

        fetchSuggestions();
    }, []);

    if (loading) {
        return <LoadingSpinner fullPage text="Loading suggestions..." />;
    }

    return (
        <div className="min-h-screen bg-gradient-to-b from-background to-muted/20 py-4 sm:py-6">
            <div className="container mx-auto px-3 sm:px-4">
                <div className="mb-4 sm:mb-6">
                    <h1 className="text-2xl sm:text-3xl font-bold">Community Suggestions</h1>
                    <p className="text-sm text-muted-foreground">
                        See what others are suggesting to improve the platform
                    </p>
                </div>

                {suggestions.length === 0 ? (
                    <EmptyState
                        icon={<MessageSquare />}
                        title="No suggestions yet"
                        description="Be the first to share your ideas!"
                    />
                ) : (
                    <div className="grid gap-4 md:gap-6">
                        {suggestions.map((suggestion) => (
                            <Card key={suggestion.suggestionId} className="overflow-hidden rounded-xl border border-border/50 shadow-md hover:shadow-lg transition-all duration-300">
                                <CardHeader className="bg-muted/20">
                                    <div className="flex flex-col sm:flex-row sm:items-start sm:justify-between gap-3">
                                        <div className="flex items-center gap-2">
                                            <MessageSquare className="h-5 w-5 text-primary" />
                                            <CardTitle className="text-sm sm:text-base">
                                                Suggestion #{suggestion.suggestionId}
                                            </CardTitle>
                                        </div>
                                        <div className="flex flex-wrap items-center gap-3 sm:gap-4 text-xs sm:text-sm text-muted-foreground">
                                            {suggestion.username && (
                                                <div className="flex items-center gap-1">
                                                    <User className="h-3 w-3 sm:h-4 sm:w-4" />
                                                    <span>{suggestion.username}</span>
                                                </div>
                                            )}
                                            <div className="flex items-center gap-1">
                                                <Calendar className="h-3 w-3 sm:h-4 sm:w-4" />
                                                <span>
                                                    {suggestion.createdAt
                                                        ? format(new Date(suggestion.createdAt), "MMM dd, yyyy")
                                                        : "N/A"}
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </CardHeader>
                                <CardContent className="pt-4">
                                    <p className="text-xs sm:text-sm text-muted-foreground whitespace-pre-wrap">
                                        {suggestion.suggestionText}
                                    </p>
                                </CardContent>
                            </Card>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}
