import { User, Calendar, MessageSquare, Send, X } from 'lucide-react';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { StarRating } from './StarRating';
import { useState } from 'react';
import { useAuthStore } from '@/stores/authStore';

interface Reply {
    replyId?: number;
    userId?: number;
    username?: string;
    replyText: string;
    createdAt?: string;
    repliedAt?: string;
    repliedByUsername?: string;
}

interface Review {
    ratingId: number;
    userId?: number;
    userName?: string;
    username?: string;
    reviewText?: string;
    createdAt: string;
    overallRating?: number;
    rating?: number;
    reply?: Reply;
}

interface ReviewCardProps {
    review: Review;
    onReplySubmit?: (ratingId: number, replyText: string) => Promise<void>;
    canReply?: boolean; // User must have rated to reply
}

/**
 * Review Card Component
 * Displays a single review with user info, rating, timestamp, and reply functionality
 */
export function ReviewCard({ review, onReplySubmit, canReply = false }: ReviewCardProps) {
    const [isReplying, setIsReplying] = useState(false);
    const [replyText, setReplyText] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const user = useAuthStore((state) => state.user);

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

    const handleReplySubmit = async () => {
        if (!replyText.trim() || !onReplySubmit) return;

        setIsSubmitting(true);
        try {
            await onReplySubmit(review.ratingId, replyText);
            setReplyText('');
            setIsReplying(false);
        } catch (error) {
            console.error('Failed to submit reply:', error);
        } finally {
            setIsSubmitting(false);
        }
    };

    const displayName = review.userName || review.username || 'Anonymous';
    const displayRating = review.overallRating || review.rating;
    const isOwnReview = user && review.userId === user.userId;

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
                                <p className="font-semibold">{displayName}</p>
                                {displayRating && (
                                    <div className="mt-1">
                                        <StarRating value={displayRating} readonly size="sm" />
                                    </div>
                                )}
                            </div>
                        </div>

                        {/* Timestamp */}
                        <div className="flex items-center gap-1 text-sm text-muted-foreground">
                            <Calendar className="h-3 w-3" />
                            <span>{formatDate(review.createdAt)}</span>
                        </div>
                    </div>

                    {/* Review Text */}
                    {review.reviewText && (
                        <p className="text-muted-foreground leading-relaxed pl-12">
                            {review.reviewText}
                        </p>
                    )}

                    {/* Reply Section */}
                    {review.reply && (
                        <div className="ml-12 mt-4 p-4 bg-muted/50 rounded-lg border-l-2 border-primary/30">
                            <div className="flex items-start gap-2">
                                <MessageSquare className="h-4 w-4 text-primary mt-0.5" />
                                <div className="flex-1">
                                    <p className="text-sm font-medium text-primary">
                                        {review.reply.username || review.reply.repliedByUsername || 'User'}
                                    </p>
                                    <p className="text-sm text-muted-foreground mt-1">
                                        {review.reply.replyText}
                                    </p>
                                    {(review.reply.createdAt || review.reply.repliedAt) && (
                                        <p className="text-xs text-muted-foreground mt-2">
                                            {formatDate(review.reply.createdAt || review.reply.repliedAt!)}
                                        </p>
                                    )}
                                </div>
                            </div>
                        </div>
                    )}


                    {!isOwnReview && canReply && !review.reply && onReplySubmit && (
                        <div className="ml-12 mt-4">
                            {isReplying ? (
                                <div className="space-y-2">
                                    <textarea
                                        value={replyText}
                                        onChange={(e) => setReplyText(e.target.value)}
                                        placeholder="Write your reply..."
                                        className="w-full px-3 py-2 border rounded-md resize-none focus:outline-none focus:ring-2 focus:ring-primary"
                                        rows={3}
                                        disabled={isSubmitting}
                                    />
                                    <div className="flex gap-2">
                                        <Button
                                            size="sm"
                                            onClick={handleReplySubmit}
                                            disabled={!replyText.trim() || isSubmitting}
                                        >
                                            <Send className="h-3 w-3 mr-1" />
                                            {isSubmitting ? 'Sending...' : 'Send Reply'}
                                        </Button>
                                        <Button
                                            size="sm"
                                            variant="outline"
                                            onClick={() => {
                                                setIsReplying(false);
                                                setReplyText('');
                                            }}
                                            disabled={isSubmitting}
                                        >
                                            <X className="h-3 w-3 mr-1" />
                                            Cancel
                                        </Button>
                                    </div>
                                </div>
                            ) : (
                                <Button
                                    size="sm"
                                    variant="ghost"
                                    onClick={() => setIsReplying(true)}
                                    className="text-primary hover:text-primary hover:bg-primary/10"
                                >
                                    <MessageSquare className="h-3 w-3 mr-1" />
                                    Reply
                                </Button>
                            )}
                        </div>
                    )}

                    {/* Show message if user can't reply */}
                    {!isOwnReview && !canReply && !review.reply && (
                        <div className="ml-12 mt-4">
                            <p className="text-xs text-muted-foreground italic">
                                You must rate this place before replying to reviews
                            </p>
                        </div>
                    )}
                </div>
            </CardContent>
        </Card>
    );
}
