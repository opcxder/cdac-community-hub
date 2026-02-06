import { useState, useEffect } from 'react';
import { adminService } from '@/api/services';
import type { Suggestion } from '@/types';
import { useToast } from '@/hooks/use-toast';
import { LoadingSpinner, EmptyState } from '@/components/shared';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { MessageSquare, Search, Calendar, User } from 'lucide-react';
import { format } from 'date-fns';
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select';

export function SuggestionsManagement() {
    const [suggestions, setSuggestions] = useState<Suggestion[]>([]);
    const [loading, setLoading] = useState(true);
    const [searchQuery, setSearchQuery] = useState('');
    const [categoryFilter, setCategoryFilter] = useState<string>('all');
    const { toast } = useToast();

    const fetchSuggestions = async () => {
        try {
            setLoading(true);
            const data = await adminService.getAllSuggestions();
            console.log('📊 [SUGGESTIONS-MGMT] Received:', data);
            setSuggestions(Array.isArray(data) ? data : []);
        } catch (error) {
            console.error('❌ [SUGGESTIONS-MGMT] Error:', error);
            toast({
                title: 'Error',
                description: 'Failed to fetch suggestions',
                variant: 'destructive',
            });
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchSuggestions();
    }, []);

    const filteredSuggestions = suggestions.filter((suggestion) => {
        const matchesSearch =
            !searchQuery ||
            suggestion.suggestionText.toLowerCase().includes(searchQuery.toLowerCase()) ||
            suggestion.userId.toString().includes(searchQuery);

        const matchesCategory =
            categoryFilter === 'all' || suggestion.category === categoryFilter;

        return matchesSearch && matchesCategory;
    });

    const getCategoryColor = (category: string) => {
        switch (category) {
            case 'CANTEEN':
                return 'bg-orange-100 text-orange-800';
            case 'CLASSROOM':
                return 'bg-purple-100 text-purple-800';
            case 'FACILITIES':
                return 'bg-green-100 text-green-800';
            case 'OTHER':
                return 'bg-blue-100 text-blue-800';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    };

    if (loading) {
        return <LoadingSpinner fullPage text="Loading suggestions..." />;
    }

    return (
        <div className="min-h-screen bg-gradient-to-b from-background to-muted/20 py-4 sm:py-6">
            <div className="container mx-auto px-3 sm:px-4">
                {/* Header */}
                <div className="mb-4 sm:mb-6">
                    <h1 className="text-2xl sm:text-3xl font-bold">User Suggestions</h1>
                    <p className="text-sm text-muted-foreground">
                        Review user feedback and improvement ideas
                    </p>
                </div>

                {/* Filters */}
                <div className="mb-4 flex flex-col gap-4 sm:flex-row">
                    <div className="relative flex-1">
                        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                        <Input
                            placeholder="Search suggestions..."
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            className="pl-10 h-12"
                        />
                    </div>
                    <Select value={categoryFilter} onValueChange={setCategoryFilter}>
                        <SelectTrigger className="w-full sm:w-[180px] h-12">
                            <SelectValue placeholder="Category" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="all">All Categories</SelectItem>
                            <SelectItem value="OTHER">General / Other</SelectItem>
                            <SelectItem value="FACILITIES">Facilities</SelectItem>
                            <SelectItem value="CANTEEN">Canteen</SelectItem>
                            <SelectItem value="CLASSROOM">Classroom</SelectItem>
                        </SelectContent>
                    </Select>
                </div>

                {/* Suggestions List */}
                {!filteredSuggestions || filteredSuggestions.length === 0 ? (
                    <EmptyState
                        icon={<MessageSquare />}
                        title="No suggestions found"
                        description={
                            searchQuery || categoryFilter !== 'all'
                                ? 'Try adjusting your filters'
                                : 'No user suggestions have been submitted yet'
                        }
                    />
                ) : (
                    <>
                        <div className="space-y-4">
                            {filteredSuggestions.map((suggestion) => (
                                <Card key={suggestion.suggestionId}>
                                    <CardHeader>
                                        <div className="flex items-start justify-between">
                                            <div className="flex items-center gap-3">
                                                <div className="flex h-10 w-10 items-center justify-center rounded-full bg-primary/10">
                                                    <MessageSquare className="h-5 w-5 text-primary" />
                                                </div>
                                                <div>
                                                    <CardTitle className="text-base">
                                                        Suggestion #{suggestion.suggestionId}
                                                    </CardTitle>
                                                    <div className="flex items-center gap-2 mt-1">
                                                        <div className="flex items-center gap-1 text-sm text-muted-foreground">
                                                            <User className="h-3 w-3" />
                                                            <span>User ID: {suggestion.userId}</span>
                                                        </div>
                                                        <span className="text-muted-foreground">•</span>
                                                        <div className="flex items-center gap-1 text-sm text-muted-foreground">
                                                            <Calendar className="h-3 w-3" />
                                                            <span>
                                                                {format(
                                                                    new Date(suggestion.createdAt),
                                                                    'MMM dd, yyyy'
                                                                )}
                                                            </span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <Badge className={getCategoryColor(suggestion.category)}>
                                                {suggestion.category}
                                            </Badge>
                                        </div>
                                    </CardHeader>
                                    <CardContent>
                                        <p className="text-sm text-muted-foreground">
                                            {suggestion.suggestionText}
                                        </p>
                                    </CardContent>
                                </Card>
                            ))}
                        </div>


                    </>
                )}
            </div>
        </div>
    );
}
