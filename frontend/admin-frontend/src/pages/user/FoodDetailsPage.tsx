import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, MapPin, DollarSign, Phone, Star, TrendingUp } from 'lucide-react';
import client from '@/api/client';
import { useAuthStore } from '@/stores/authStore';
import { toast } from 'sonner';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Skeleton } from '@/components/ui/skeleton';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { ImageGallery } from '@/components/shared/ImageGallery';
import { StarRating } from '@/components/shared/StarRating';
import { RatingBreakdown } from '@/components/shared/RatingBreakdown';
import { ReviewCard } from '@/components/shared/ReviewCard';
import { ReviewForm } from '@/components/shared/ReviewForm';


interface Category {
    categoryId: number;
    categoryName: string;
}

interface FoodPlace {
    placeId: number;
    placeName: string;
    description: string;
    address: string;
    city: string;
    locality: string;
    landmark: string;
    contactInfo: string | null;
    priceRange: 'BUDGET' | 'MODERATE' | 'EXPENSIVE';
    imageUrls: string[];
    categories: Category[];
    averageRating: number;
    status: string;
    mapLocation: string | null;
}

interface RatingStats {
    averageRating: number;
    totalRatings: number;
    ratingBreakdown: {
        5: number;
        4: number;
        3: number;
        2: number;
        1: number;
    };
}

interface Review {
    ratingId: number;  // Backend uses ratingId, not reviewId
    userId: number;
    username: string;  // Backend uses username, not userName
    rating?: number;
    reviewText: string;
    createdAt: string;
}

/**
 * Food Details Page
 * Displays comprehensive information about a food place
 */
export default function FoodDetailsPage() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [foodPlace, setFoodPlace] = useState<FoodPlace | null>(null);
    const [ratingStats, setRatingStats] = useState<RatingStats | null>(null);
    const [reviews, setReviews] = useState<Review[]>([]);
    const [userRating, setUserRating] = useState<number | null>(null);
    const [isSubmittingRating, setIsSubmittingRating] = useState(false);
    const [isSubmittingReview, setIsSubmittingReview] = useState(false);
    const [loadingReviews, setLoadingReviews] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const user = useAuthStore((state) => state.user);

    useEffect(() => {
        async function fetchFoodPlaceDetails() {
            if (!id) {
                setError('Invalid food place ID');
                setLoading(false);
                return;
            }

            setLoading(true);
            setError(null);

            try {
                console.log('🍽️ [FOOD-DETAILS] Fetching details for place ID:', id);

                // Fetch food place details
                const response = await client.get<FoodPlace>(`/api/food/places/${id}`);
                console.log('🍽️ [FOOD-DETAILS] Response:', response.data);

                setFoodPlace(response.data);

                // TODO: Fetch rating stats when backend endpoint is ready
                // For now, use mock data
                setRatingStats({
                    averageRating: response.data.averageRating || 0,
                    totalRatings: 0,
                    ratingBreakdown: {
                        5: 0,
                        4: 0,
                        3: 0,
                        2: 0,
                        1: 0
                    }
                });

            } catch (err: any) {
                console.error('🍽️ [FOOD-DETAILS] Error:', err);
                if (err?.response?.status === 404) {
                    setError('Food place not found');
                } else {
                    setError(err?.response?.data?.message || 'Failed to load food place details');
                }
            } finally {
                setLoading(false);
            }
        }

        fetchFoodPlaceDetails();
    }, [id]);

    // Fetch user's existing rating
    useEffect(() => {
        async function fetchUserRating() {
            if (!id || !user) return;

            try {
                console.log('🍽️ [FOOD-DETAILS] Fetching user rating for place ID:', id);
                // TODO: Implement when backend endpoint is ready
                // const response = await client.get(`/ api / food / places / ${ id }/my-rating`);
                // setUserRating(response.data.rating);
            } catch (err: any) {
                console.log('🍽️ [FOOD-DETAILS] No existing rating or error:', err);
                // User hasn't rated yet, which is fine
            }
        }

        fetchUserRating();
    }, [id, user]);

    // Handle rating submission
    const handleRatingSubmit = async (rating: number) => {
        if (!id || !user) {
            toast.error('Please log in to rate this place');
            return;
        }

        setIsSubmittingRating(true);

        try {
            console.log('🍽️ [FOOD-DETAILS] Submitting rating:', { placeId: id, rating, userId: user.userId });

            // TODO: Replace with actual API endpoint when ready
            await client.post(`/api/food/places/${id}/rate?userId=${user.userId}`, {
                rating: rating,
                reviewText: null  // Rating only, no review text
            });

            setUserRating(rating);
            toast.success('Rating submitted successfully! You can now add a review below.');

            // Refresh rating stats
            // TODO: Fetch updated stats from backend
            if (ratingStats) {
                const newBreakdown = { ...ratingStats.ratingBreakdown };
                newBreakdown[rating as keyof typeof newBreakdown] += 1;

                const newTotalRatings = ratingStats.totalRatings + 1;
                const newAverage = (
                    (ratingStats.averageRating * ratingStats.totalRatings + rating) / newTotalRatings
                );

                setRatingStats({
                    averageRating: newAverage,
                    totalRatings: newTotalRatings,
                    ratingBreakdown: newBreakdown
                });
            }

        } catch (err: any) {
            console.error('🍽️ [FOOD-DETAILS] Rating submission error:', err);
            toast.error(err?.response?.data?.message || 'Failed to submit rating');
        } finally {
            setIsSubmittingRating(false);
        }
    };

    // Fetch reviews
    useEffect(() => {
        async function fetchReviews() {
            if (!id) return;

            setLoadingReviews(true);

            try {
                console.log('🍽️ [FOOD-DETAILS] Fetching reviews for place ID:', id);
                // Fetch all ratings (which include reviews)
                const response = await client.get<Review[]>(`/api/food/places/${id}/ratings`);
                // Filter out user's own rating from the reviews list
                const filteredReviews = response.data.filter(review => review.userId !== user?.userId);
                setReviews(filteredReviews);
            } catch (err: any) {
                console.error('🍽️ [FOOD-DETAILS] Error fetching reviews:', err);
                // Don't show error to user, just log it
            } finally {
                setLoadingReviews(false);
            }
        }

        fetchReviews();
    }, [id]);

    // Handle review submission
    const handleReviewSubmit = async (reviewText: string) => {
        if (!id || !user) {
            toast.error('Please log in to submit a review');
            return;
        }

        setIsSubmittingReview(true);

        try {
            console.log('🍽️ [FOOD-DETAILS] Submitting review:', { placeId: id, userId: user.userId });

            // Submit as a rating with review text (no star rating)
            const response = await client.post<Review>(`/api/food/places/${id}/rate?userId=${user.userId}`, {
                rating: null,  // No rating, just review text
                reviewText: reviewText
            });

            // Add new review to the list
            setReviews((prev) => [response.data, ...prev]);
            toast.success('Review submitted successfully!');

        } catch (err: any) {
            console.error('🍽️ [FOOD-DETAILS] Review submission error:', err);
            toast.error(err?.response?.data?.message || 'Failed to submit review');
        } finally {
            setIsSubmittingReview(false);
        }
    };

    const getPriceRangeDisplay = (range: string) => {
        const displays = {
            BUDGET: { label: 'Budget Friendly', color: 'bg-green-100 text-green-800', icon: '$' },
            MODERATE: { label: 'Moderate', color: 'bg-yellow-100 text-yellow-800', icon: '$$' },
            EXPENSIVE: { label: 'Premium', color: 'bg-red-100 text-red-800', icon: '$$$' }
        };
        return displays[range as keyof typeof displays] || displays.MODERATE;
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gradient-to-b from-background to-muted/20">
                <div className="container mx-auto px-4 py-6 max-w-6xl">
                    {/* Back Button Skeleton */}
                    <Skeleton className="h-10 w-32 mb-6" />

                    {/* Image Gallery Skeleton */}
                    <Skeleton className="aspect-video w-full rounded-xl mb-6" />

                    {/* Content Skeletons */}
                    <div className="grid gap-6 lg:grid-cols-3">
                        <div className="lg:col-span-2 space-y-6">
                            <Skeleton className="h-12 w-3/4" />
                            <Skeleton className="h-24 w-full" />
                            <Skeleton className="h-48 w-full" />
                        </div>
                        <div className="space-y-6">
                            <Skeleton className="h-64 w-full" />
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    if (error || !foodPlace) {
        return (
            <div className="min-h-screen bg-gradient-to-b from-background to-muted/20">
                <div className="container mx-auto px-4 py-6 max-w-6xl">
                    <Button
                        variant="ghost"
                        onClick={() => navigate('/food')}
                        className="mb-6"
                    >
                        <ArrowLeft className="h-4 w-4 mr-2" />
                        Back to Browse
                    </Button>

                    <Alert variant="destructive">
                        <AlertDescription>{error || 'Food place not found'}</AlertDescription>
                    </Alert>
                </div>
            </div>
        );
    }

    const priceDisplay = getPriceRangeDisplay(foodPlace.priceRange);

    return (
        <div className="min-h-screen bg-gradient-to-b from-background to-muted/20">
            <div className="container mx-auto px-4 py-6 max-w-6xl">
                {/* Back Button */}
                <Button
                    variant="ghost"
                    onClick={() => navigate('/food')}
                    className="mb-6 hover:bg-primary/10"
                >
                    <ArrowLeft className="h-4 w-4 mr-2" />
                    Back to Browse
                </Button>

                {/* Image Gallery */}
                <div className="mb-8">
                    <ImageGallery
                        images={foodPlace.imageUrls}
                        altText={foodPlace.placeName}
                    />
                </div>

                {/* Main Content */}
                <div className="grid gap-6 lg:grid-cols-3">
                    {/* Left Column - Main Information */}
                    <div className="lg:col-span-2 space-y-6">
                        {/* Header */}
                        <div>
                            <div className="flex items-start justify-between gap-4 mb-3">
                                <h1 className="text-3xl sm:text-4xl font-bold">{foodPlace.placeName}</h1>
                                <Badge className={priceDisplay.color} variant="secondary">
                                    {priceDisplay.icon}
                                </Badge>
                            </div>

                            {/* Rating */}
                            <div className="flex items-center gap-4 mb-4">
                                <StarRating value={ratingStats?.averageRating || 0} readonly size="lg" />
                                <span className="text-2xl font-bold">{(ratingStats?.averageRating || 0).toFixed(1)}</span>
                                <span className="text-muted-foreground">
                                    ({ratingStats?.totalRatings || 0} {ratingStats?.totalRatings === 1 ? 'rating' : 'ratings'})
                                </span>
                            </div>

                            {/* Price Range Label */}
                            <div className="flex items-center gap-2 text-muted-foreground">
                                <DollarSign className="h-4 w-4" />
                                <span>{priceDisplay.label}</span>
                            </div>
                        </div>

                        {/* Description */}
                        {foodPlace.description && (
                            <Card>
                                <CardHeader>
                                    <CardTitle className="text-lg">About</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <p className="text-muted-foreground leading-relaxed">
                                        {foodPlace.description}
                                    </p>
                                </CardContent>
                            </Card>
                        )}

                        {/* Categories */}
                        {foodPlace.categories && foodPlace.categories.length > 0 && (
                            <Card>
                                <CardHeader>
                                    <CardTitle className="text-lg">Cuisines & Categories</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <div className="flex flex-wrap gap-2">
                                        {foodPlace.categories.map((category) => (
                                            <Badge key={category.categoryId} variant="outline" className="text-sm">
                                                {category.categoryName}
                                            </Badge>
                                        ))}
                                    </div>
                                </CardContent>
                            </Card>
                        )}

                        {/* Rating Breakdown - Always show */}
                        <Card>
                            <CardHeader>
                                <CardTitle className="text-lg flex items-center gap-2">
                                    <TrendingUp className="h-5 w-5" />
                                    Rating Breakdown
                                </CardTitle>
                            </CardHeader>
                            <CardContent>
                                {ratingStats ? (
                                    <RatingBreakdown
                                        ratings={ratingStats.ratingBreakdown}
                                        totalRatings={ratingStats.totalRatings}
                                    />
                                ) : (
                                    <p className="text-sm text-muted-foreground text-center py-4">
                                        No ratings yet. Be the first to rate this place!
                                    </p>
                                )}
                            </CardContent>
                        </Card>
                    </div>

                    {/* Right Column - Location & Contact */}
                    <div className="space-y-6">
                        {/* Location */}
                        <Card>
                            <CardHeader>
                                <CardTitle className="text-lg flex items-center gap-2">
                                    <MapPin className="h-5 w-5" />
                                    Location
                                </CardTitle>
                            </CardHeader>
                            <CardContent className="space-y-3">
                                {foodPlace.locality && (
                                    <div>
                                        <p className="text-sm font-medium text-muted-foreground">Locality</p>
                                        <p className="font-medium">{foodPlace.locality}</p>
                                    </div>
                                )}
                                {foodPlace.landmark && (
                                    <div>
                                        <p className="text-sm font-medium text-muted-foreground">Landmark</p>
                                        <p className="font-medium">{foodPlace.landmark}</p>
                                    </div>
                                )}
                                {foodPlace.address && (
                                    <div>
                                        <p className="text-sm font-medium text-muted-foreground">Address</p>
                                        <p className="font-medium">{foodPlace.address}</p>
                                    </div>
                                )}
                                {foodPlace.city && (
                                    <div>
                                        <p className="text-sm font-medium text-muted-foreground">City</p>
                                        <p className="font-medium">{foodPlace.city}</p>
                                    </div>
                                )}
                            </CardContent>
                        </Card>

                        {/* Contact Information */}
                        {foodPlace.contactInfo && (
                            <Card>
                                <CardHeader>
                                    <CardTitle className="text-lg flex items-center gap-2">
                                        <Phone className="h-5 w-5" />
                                        Contact
                                    </CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <p className="font-medium">{foodPlace.contactInfo}</p>
                                </CardContent>
                            </Card>
                        )}

                        {/* Rate This Place */}
                        <Card className="bg-primary/5 border-primary/20">
                            <CardHeader>
                                <CardTitle className="text-lg flex items-center gap-2">
                                    <Star className="h-5 w-5 text-primary" />
                                    {userRating ? 'Your Rating' : 'Rate This Place'}
                                </CardTitle>
                            </CardHeader>
                            <CardContent>
                                {userRating ? (
                                    <div className="space-y-4">
                                        <div className="flex items-center justify-center gap-2 p-4 bg-background rounded-lg">
                                            <StarRating value={userRating} readonly size="lg" />
                                            <span className="text-2xl font-bold">{userRating}.0</span>
                                        </div>
                                        <p className="text-sm text-muted-foreground text-center">
                                            You rated this place {userRating} {userRating === 1 ? 'star' : 'stars'}
                                        </p>
                                    </div>
                                ) : (
                                    <div className="space-y-4">
                                        <p className="text-sm text-muted-foreground">
                                            Share your experience with others
                                        </p>
                                        <div className="flex flex-col items-center gap-3 p-4 bg-background rounded-lg">
                                            <p className="text-sm font-medium">Click to rate:</p>
                                            <StarRating
                                                value={0}
                                                onChange={handleRatingSubmit}
                                                size="lg"
                                            />
                                        </div>
                                        {isSubmittingRating && (
                                            <p className="text-sm text-center text-muted-foreground">
                                                Submitting your rating...
                                            </p>
                                        )}
                                    </div>
                                )}
                            </CardContent>
                        </Card>
                    </div>
                </div>

                {/* Reviews Section */}
                <div className="mt-12">
                    <h2 className="text-2xl font-bold mb-6">Reviews & Comments</h2>

                    <div className="space-y-6">
                        {/* Review Form */}
                        {user && (
                            <ReviewForm
                                onSubmit={handleReviewSubmit}
                                isSubmitting={isSubmittingReview}
                            />
                        )}

                        {/* Reviews List */}
                        {loadingReviews ? (
                            <div className="space-y-4">
                                <Skeleton className="h-32 w-full" />
                                <Skeleton className="h-32 w-full" />
                                <Skeleton className="h-32 w-full" />
                            </div>
                        ) : reviews.length > 0 ? (
                            <div className="space-y-4">
                                {reviews.map((review) => (
                                    <ReviewCard
                                        key={review.ratingId}
                                        userName={review.username}
                                        rating={review.rating}
                                        reviewText={review.reviewText}
                                        createdAt={review.createdAt}
                                    />
                                ))}
                            </div>
                        ) : (
                            <Card>
                                <CardContent className="py-12 text-center">
                                    <p className="text-muted-foreground">
                                        No reviews yet. Be the first to share your experience!
                                    </p>
                                </CardContent>
                            </Card>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}
