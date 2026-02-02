import { useState } from 'react';
import { Send } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Textarea } from '@/components/ui/textarea';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';

interface ReviewFormProps {
    onSubmit: (reviewText: string) => Promise<void>;
    isSubmitting?: boolean;
}

/**
 * Review Form Component
 * Form for submitting new reviews/comments
 */
export function ReviewForm({ onSubmit, isSubmitting = false }: ReviewFormProps) {
    const [reviewText, setReviewText] = useState('');
    const maxLength = 500;

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!reviewText.trim()) {
            return;
        }

        await onSubmit(reviewText.trim());
        setReviewText(''); // Clear form after successful submission
    };

    const remainingChars = maxLength - reviewText.length;
    const isValid = reviewText.trim().length > 0 && reviewText.length <= maxLength;

    return (
        <Card>
            <CardHeader>
                <CardTitle className="text-lg">Write a Review</CardTitle>
            </CardHeader>
            <CardContent>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div className="space-y-2">
                        <Textarea
                            placeholder="Share your experience..."
                            value={reviewText}
                            onChange={(e) => setReviewText(e.target.value)}
                            maxLength={maxLength}
                            rows={4}
                            className="resize-none"
                            disabled={isSubmitting}
                        />
                        <div className="flex items-center justify-between text-sm">
                            <span className={`text-muted-foreground ${remainingChars < 50 ? 'text-orange-500' : ''} ${remainingChars === 0 ? 'text-red-500' : ''}`}>
                                {remainingChars} characters remaining
                            </span>
                        </div>
                    </div>

                    <Button
                        type="submit"
                        disabled={!isValid || isSubmitting}
                        className="w-full sm:w-auto gap-2"
                    >
                        {isSubmitting ? (
                            <>Submitting...</>
                        ) : (
                            <>
                                <Send className="h-4 w-4" />
                                Submit Review
                            </>
                        )}
                    </Button>
                </form>
            </CardContent>
        </Card>
    );
}
