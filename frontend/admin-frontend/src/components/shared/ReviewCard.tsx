import { User, Calendar } from 'lucide-react';
import { Card, CardContent } from '@/components/ui/card';
import { StarRating } from './StarRating';

interface ReviewCardProps {
    userName: string;
    rating?: number;
    reviewText: string;
    createdAt: string;
}

/**
 * Review Card Component
 * Displays a single review with user info, rating, and timestamp
 */
export function ReviewCard({ userName, rating, reviewText, createdAt }: ReviewCardProps) {
    const formatDate = (dateString: string) => {
        try {
            const date = new Date(dateString);
            return date.toLocaleDateString('en-US', {
                year: 'numeric',
                month: 'short',
                day: 'numeric'
            });
        } catch {
            return dateString;
        }
    };

    return (
        <Card>
            <CardContent className="pt-6">
                <div className="space-y-3">
                    {/* User Info and Rating */}
                    <div className="flex items-start justify-between gap-4">
                        <div className="flex items-center gap-2">
                            <div className="h-10 w-10 rounded-full bg-primary/10 flex items-center justify-center">
                                <User className="h-5 w-5 text-primary" />
                            </div>
                            <div>
                                <p className="font-semibold">{userName}</p>
                                {rating && (
                                    <div className="mt-1">
                                        <StarRating value={rating} readonly size="sm" />
                                    </div>
                                )}
                            </div>
                        </div>

                        {/* Timestamp */}
                        <div className="flex items-center gap-1 text-sm text-muted-foreground">
                            <Calendar className="h-3 w-3" />
                            <span>{formatDate(createdAt)}</span>
                        </div>
                    </div>

                    {/* Review Text */}
                    <p className="text-muted-foreground leading-relaxed pl-12">
                        {reviewText}
                    </p>
                </div>
            </CardContent>
        </Card>
    );
}
