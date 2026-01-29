import { useState, useEffect } from "react";
import client from "@/api/client";
import { Card, CardContent, CardDescription, CardHeader  } from "@/components/ui/card";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { MessageSquare, Loader2 } from "lucide-react";
import type { Suggestion, PageResponse } from "@/types/api";

export default function SuggestionsPage() {
    const [suggestions, setSuggestions] = useState<Suggestion[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [activeCategory, setActiveCategory] = useState<'ALL' | 'FOOD' | 'HOSTEL' | 'GENERAL'>('ALL');

    const fetchSuggestions = async () => {
        setLoading(true);
        setError(null);

        try {
            const response = await client.get<PageResponse<Suggestion>>('/api/admin/suggestions', {
                params: {
                    page: 0,
                    size: 100
                }
            });
            setSuggestions(response.data.content);
        } catch (err: unknown) {
            const message = (err as { response?: { data?: { message?: string } } })?.response?.data?.message ||
                "Failed to load suggestions";
            setError(message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchSuggestions();
    }, []);

    const filteredSuggestions = activeCategory === 'ALL'
        ? suggestions
        : suggestions.filter(s => s.category === activeCategory);

    const getCategoryBadgeColor = (category: string) => {
        switch (category) {
            case 'FOOD': return 'bg-orange-100 text-orange-800';
            case 'HOSTEL': return 'bg-blue-100 text-blue-800';
            case 'GENERAL': return 'bg-gray-100 text-gray-800';
            default: return 'bg-gray-100 text-gray-800';
        }
    };

    const formatDate = (dateString: string) => {
        return new Date(dateString).toLocaleDateString('en-IN', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    return (
        <div className="container mx-auto p-6">
            <div className="mb-6">
                <h1 className="text-3xl font-bold">User Suggestions</h1>
                <p className="text-muted-foreground mt-2">
                    View feedback and suggestions from the community
                </p>
            </div>

            {error && (
                <Alert variant="destructive" className="mb-4">
                    <AlertDescription>{error}</AlertDescription>
                </Alert>
            )}

            <Tabs value={activeCategory} onValueChange={(value:any) => setActiveCategory(value as typeof activeCategory)}>
                <TabsList className="mb-4">
                    <TabsTrigger value="ALL">All ({suggestions.length})</TabsTrigger>
                    <TabsTrigger value="GENERAL">
                        General ({suggestions.filter(s => s.category === 'GENERAL').length})
                    </TabsTrigger>
                    <TabsTrigger value="FOOD">
                        Food ({suggestions.filter(s => s.category === 'FOOD').length})
                    </TabsTrigger>
                    <TabsTrigger value="HOSTEL">
                        Hostel ({suggestions.filter(s => s.category === 'HOSTEL').length})
                    </TabsTrigger>
                </TabsList>

                <TabsContent value={activeCategory}>
                    {loading ? (
                        <div className="flex items-center justify-center py-12">
                            <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
                        </div>
                    ) : filteredSuggestions.length === 0 ? (
                        <Card>
                            <CardContent className="flex flex-col items-center justify-center py-12">
                                <MessageSquare className="h-12 w-12 text-muted-foreground mb-4" />
                                <p className="text-muted-foreground">No suggestions found</p>
                            </CardContent>
                        </Card>
                    ) : (
                        <div className="grid gap-4">
                            {filteredSuggestions.map((suggestion) => (
                                <Card key={suggestion.suggestionId}>
                                    <CardHeader>
                                        <div className="flex items-start justify-between">
                                            <div className="flex-1">
                                                <div className="flex items-center gap-2 mb-2">
                                                    <Badge className={getCategoryBadgeColor(suggestion.category)}>
                                                        {suggestion.category}
                                                    </Badge>
                                                    <span className="text-sm text-muted-foreground">
                                                        User ID: {suggestion.userId}
                                                    </span>
                                                </div>
                                                <CardDescription>
                                                    Submitted on {formatDate(suggestion.createdAt)}
                                                </CardDescription>
                                            </div>
                                        </div>
                                    </CardHeader>
                                    <CardContent>
                                        <p className="text-sm whitespace-pre-wrap">{suggestion.content}</p>
                                    </CardContent>
                                </Card>
                            ))}
                        </div>
                    )}
                </TabsContent>
            </Tabs>
        </div>
    );
}
