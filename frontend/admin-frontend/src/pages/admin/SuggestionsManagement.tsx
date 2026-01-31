import { useState, useEffect } from 'react';
import { adminService } from '@/api/services';
import type { Suggestion, PageResponse } from '@/types';
import { useToast } from '@/hooks/use-toast';
import { LoadingSpinner, EmptyState, Pagination } from '@/components/shared';
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

/**
 * Suggestions Management Page
 * View and manage user suggestions and feedback
 */
export function SuggestionsManagement() {
    const [suggestions, setSuggestions] = useState<PageResponse<Suggestion> | null>(null);
    const [loading, setLoading] = useState(true);
    const [searchQuery, setSearchQuery] = useState('');
    const [categoryFilter, setCategoryFilter] = useState<string>('all');
    const [currentPage, setCurrentPage] = useState(0);
    const { toast } = useToast();

    const fetchSuggestions = async (page = 0) => {
        try {
            setLoading(true);
            const data = await adminService.getAllSuggestions({ page, size: 10 });
            setSuggestions(data);
            setCurrentPage(page);
        } catch (error) {
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

    const filteredSuggestions = suggestions?.content.filter((suggestion) => {
        const matchesSearch =
            !searchQuery ||
            suggestion.content.toLowerCase().includes(searchQuery.toLowerCase()) ||
            suggestion.userId.toString().includes(searchQuery);

        const matchesCategory =
            categoryFilter === 'all' || suggestion.category === categoryFilter;

        return matchesSearch && matchesCategory;
    });

    const getCategoryColor = (category: string) => {
        switch (category) {
            case 'FOOD':
                return 'bg-orange-100 text-orange-800';
            case 'HOSTEL':
                return 'bg-purple-100 text-purple-800';
            case 'GENERAL':
                return 'bg-blue-100 text-blue-800';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    };

    if (loading) {
        return <LoadingSpinner fullPage text="Loading suggestions..." />;
    }

    return (
        <div className="container mx-auto py-6">
            {/* Header */}
            <div className="mb-6">
                <h1 className="text-3xl font-bold">User Suggestions</h1>
                <p className="text-muted-foreground">Review user feedback and improvement ideas</p>
            </div>

            {/* Filters */}
            <div className="mb-4 flex flex-col gap-4 sm:flex-row">
                <div className="relative flex-1">
                    <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                    <Input
                        placeholder="Search suggestions..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        className="pl-10"
                    />
                </div>
                <Select value={categoryFilter} onValueChange={setCategoryFilter}>
                    <SelectTrigger className="w-full sm:w-[180px]">
                        <SelectValue placeholder="Category" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="all">All Categories</SelectItem>
                        <SelectItem value="FOOD">Food</SelectItem>
                        <SelectItem value="HOSTEL">Hostel</SelectItem>
                        <SelectItem value="GENERAL">General</SelectItem>
                    </SelectContent>
                </Select>
            </div>

            {/* Suggestions List */}
            {!filteredSuggestions || filteredSuggestions.length === 0 ? (
                <EmptyState
                    icon={MessageSquare}
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
                                                        <span>{format(new Date(suggestion.createdAt), 'MMM dd, yyyy')}</span>
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
                                    <p className="text-sm text-muted-foreground">{suggestion.content}</p>
                                </CardContent>
                            </Card>
                        ))}
                    </div>

                    {/* Pagination */}
                    {suggestions && suggestions.totalPages > 1 && (
                        <div className="mt-6">
                            <Pagination
                                currentPage={suggestions.number}
                                totalPages={suggestions.totalPages}
                                onPageChange={fetchSuggestions}
                            />
                        </div>
                    )}
                </>
            )}
        </div>
    );
}
