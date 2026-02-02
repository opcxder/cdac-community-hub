import { Star } from 'lucide-react';
import { Progress } from '@/components/ui/progress';

interface RatingBreakdownProps {
    ratings: {
        5: number;
        4: number;
        3: number;
        2: number;
        1: number;
    };
    totalRatings: number;
}

/**
 * Rating Breakdown Component
 * Shows distribution of ratings with progress bars
 */
export function RatingBreakdown({ ratings, totalRatings }: RatingBreakdownProps) {
    if (totalRatings === 0) {
        return (
            <div className="text-center py-4 text-muted-foreground">
                No ratings yet
            </div>
        );
    }

    const getPercentage = (count: number) => {
        return totalRatings > 0 ? (count / totalRatings) * 100 : 0;
    };

    return (
        <div className="space-y-2">
            {[5, 4, 3, 2, 1].map((star) => {
                const count = ratings[star as keyof typeof ratings] || 0;
                const percentage = getPercentage(count);

                return (
                    <div key={star} className="flex items-center gap-3">
                        <div className="flex items-center gap-1 w-12">
                            <span className="text-sm font-medium">{star}</span>
                            <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                        </div>
                        <Progress value={percentage} className="flex-1 h-2" />
                        <span className="text-sm text-muted-foreground w-12 text-right">
                            {count}
                        </span>
                    </div>
                );
            })}
        </div>
    );
}
