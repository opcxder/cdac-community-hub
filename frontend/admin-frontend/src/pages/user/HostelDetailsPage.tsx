import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, MapPin, Home, Phone, Star, Wifi, Wind, UtensilsCrossed, Shirt, Users } from 'lucide-react';
import client from '@/api/client';
import { useAuthStore } from '@/stores/authStore';
import { toast } from 'sonner';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Skeleton } from '@/components/ui/skeleton';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { ImageGallery } from '@/components/shared/ImageGallery';
import { MultiCriteriaRating } from '@/components/shared/MultiCriteriaRating';
import { ReviewCard } from '@/components/shared/ReviewCard';
import { ReviewForm } from '@/components/shared/ReviewForm';

interface Hostel {
    hostelId: number;
    hostelName: string;
    description: string;
    address: string;
    city: string;
    locality: string;
    landmark: string | null;
    distanceFromCdac: string;
    monthlyRentMin: number;
    monthlyRentMax: number;
    hasWifi: boolean;
    hasAc: boolean;
    hasMess: boolean;
    hasLaundry: boolean;
    roomCapacity: number | null;
    contactPersonName: string;
    contactPersonPhone: string;
    forGender: 'BOYS' | 'GIRLS' | 'BOTH';
    images: string[];
    categories?: string[];  // Add categories
    mapLocation: string | null;
}

interface RatingStats {
    overallRating: number;
    cleanlinessRating: number;
    foodQualityRating: number;
    safetyRating: number;
    locationRating: number;
    affordabilityRating: number;
}

interface MultiCriteriaRatings {
    cleanliness: number;
    foodQuality: number;
    safety: number;
    location: number;
    affordability: number;
}

interface Review {
    ratingId: number;
    userId: number;
    reviewText: string;
    createdAt: string;
    overallRating: number;
}

/**
 * Hostel Details Page
 * Displays comprehensive information about a hostel with multi-criteria rating functionality
 */
export default function HostelDetailsPage() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [hostel, setHostel] = useState<Hostel | null>(null);
    const [ratingStats, setRatingStats] = useState<RatingStats | null>(null);
    const [reviews, setReviews] = useState<Review[]>([]);
    const [userRatings, setUserRatings] = useState<MultiCriteriaRatings>({
        cleanliness: 0,
        foodQuality: 0,
        safety: 0,
        location: 0,
        affordability: 0
    });
    const [hasRated, setHasRated] = useState(false);
    const [isSubmittingRating, setIsSubmittingRating] = useState(false);
    const [isSubmittingReview, setIsSubmittingReview] = useState(false);
    const [loadingReviews, setLoadingReviews] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const user = useAuthStore((state) => state.user);

    // Fetch hostel details
    useEffect(() => {
        async function fetchHostelDetails() {
            if (!id) return;

            setLoading(true);
            setError(null);

            try {
                console.log('🏠 [HOSTEL-DETAILS] Fetching details for hostel ID:', id);
                const response = await client.get<Hostel>(`/api/hostel/hostels/${id}`);
                console.log('🏠 [HOSTEL-DETAILS] Response:', response.data);
                setHostel(response.data);
            } catch (err: any) {
                console.error('🏠 [HOSTEL-DETAILS] Error:', err);
                setError(err?.response?.data?.message || 'Failed to load hostel details');
            } finally {
                setLoading(false);
            }
        }

        fetchHostelDetails();
    }, [id]);

    // Fetch rating stats and reviews
    useEffect(() => {
        async function fetchRatingsAndReviews() {
            if (!id) return;

            setLoadingReviews(true);

            try {
                console.log('🏠 [HOSTEL-DETAILS] Fetching ratings and reviews for hostel ID:', id);

                // Fetch rating stats - FIXED PATH
                const statsResponse = await client.get<RatingStats>(`/api/hostels/${id}/ratings/summary`);
                console.log('🏠 [HOSTEL-DETAILS] Rating stats:', statsResponse.data);
                setRatingStats(statsResponse.data);

                // Fetch all reviews - FIXED PATH
                const reviewsResponse = await client.get<Review[]>(`/api/hostels/${id}/ratings`);
                console.log('🏠 [HOSTEL-DETAILS] Reviews:', reviewsResponse.data);

                // Filter out reviews without text and user's own review
                const filteredReviews = reviewsResponse.data.filter(
                    (review) => review.reviewText && review.reviewText.trim() !== '' && review.userId !== user?.userId
                );
                setReviews(filteredReviews);

            } catch (err: any) {
                console.error('🏠 [HOSTEL-DETAILS] Error fetching ratings:', err);
            } finally {
                setLoadingReviews(false);
            }
        }

        fetchRatingsAndReviews();
    }, [id, user?.userId]);

    // Fetch user's existing ratings
    useEffect(() => {
        async function fetchUserRatings() {
            if (!id || !user) return;

            try {
                console.log('🏠 [HOSTEL-DETAILS] Fetching user ratings for hostel ID:', id);
                // Fetch all ratings and find user's ratings - FIXED PATH
                const response = await client.get<any[]>(`/api/hostels/${id}/ratings`);
                const userExistingRating = response.data.find(r => r.userId === user.userId);

                if (userExistingRating) {
                    setUserRatings({
                        cleanliness: userExistingRating.cleanlinessRating || 0,
                        foodQuality: userExistingRating.foodQualityRating || 0,
                        safety: userExistingRating.safetyRating || 0,
                        location: userExistingRating.locationRating || 0,
                        affordability: userExistingRating.affordabilityRating || 0
                    });
                    setHasRated(true);
                    console.log('🏠 [HOSTEL-DETAILS] Found existing user ratings:', userExistingRating);
                }
            } catch (err: any) {
                console.log('🏠 [HOSTEL-DETAILS] No existing ratings or error:', err);
            }
        }

        fetchUserRatings();
    }, [id, user]);

    // Handle rating submission
    const handleRatingSubmit = async (ratings: MultiCriteriaRatings) => {
        if (!id || !user) {
            toast.error('Please log in to rate this hostel');
            return;
        }

        // Check if at least one criterion is rated
        const hasAnyRating = Object.values(ratings).some(r => r > 0);
        if (!hasAnyRating) {
            toast.error('Please rate at least one criterion');
            return;
        }

        setIsSubmittingRating(true);

        try {
            console.log('🏠 [HOSTEL-DETAILS] Submitting ratings:', { hostelId: id, ratings, userId: user.userId });

            // FIXED: Send multi-criteria ratings with correct DTO structure
            await client.post(`/api/hostels/${id}/rate?userId=${user.userId}`, {
                cleanlinessRating: ratings.cleanliness || null,
                foodQualityRating: ratings.foodQuality || null,
                safetyRating: ratings.safety || null,
                locationRating: ratings.location || null,
                affordabilityRating: ratings.affordability || null,
                reviewText: null  // Rating only, no review text
            });

            setUserRatings(ratings);
            setHasRated(true);
            toast.success('Ratings submitted successfully!');

            // Refresh rating stats
            const statsResponse = await client.get<RatingStats>(`/api/hostels/${id}/ratings/summary`);
            setRatingStats(statsResponse.data);

        } catch (err: any) {
            console.error('🏠 [HOSTEL-DETAILS] Rating submission error:', err);
            const errorMessage = err?.response?.data?.message || 'Failed to submit ratings';

            if (errorMessage.includes('already rated')) {
                toast.error('Your ratings have been updated.');
                setUserRatings(ratings);
                setHasRated(true);
            } else {
                toast.error(errorMessage);
            }
        } finally {
            setIsSubmittingRating(false);
        }
    };

    // Handle review submission
    const handleReviewSubmit = async (reviewText: string) => {
        if (!id || !user) {
            toast.error('Please log in to submit a review');
            return;
        }

        if (!hasRated) {
            toast.error('Please rate this hostel first before adding a review');
            return;
        }

        setIsSubmittingReview(true);

        try {
            console.log('🏠 [HOSTEL-DETAILS] Submitting review:', { hostelId: id, userId: user.userId });

            // Update the existing rating with review text
            await client.post(`/api/hostels/${id}/rate?userId=${user.userId}`, {
                cleanlinessRating: userRatings.cleanliness || null,
                foodQualityRating: userRatings.foodQuality || null,
                safetyRating: userRatings.safety || null,
                locationRating: userRatings.location || null,
                affordabilityRating: userRatings.affordability || null,
                reviewText: reviewText
            });

            toast.success('Review added successfully!');

            // Refresh reviews
            const reviewsResponse = await client.get<Review[]>(`/api/hostels/${id}/ratings`);
            const filteredReviews = reviewsResponse.data.filter(
                (review) => review.reviewText && review.reviewText.trim() !== '' && review.userId !== user.userId
            );
            setReviews(filteredReviews);

        } catch (err: any) {
            console.error('🏠 [HOSTEL-DETAILS] Review submission error:', err);
            const errorMessage = err?.response?.data?.message || 'Failed to submit review';

            if (errorMessage.includes('already rated')) {
                toast.error('You have already rated and reviewed this hostel');
            } else {
                toast.error(errorMessage);
            }
        } finally {
            setIsSubmittingReview(false);
        }
    };

    // Handle reply submission
    const handleReplySubmit = async (ratingId: number, replyText: string) => {
        if (!user) {
            toast.error('Please log in to reply');
            return;
        }

        try {
            console.log('🏠 [HOSTEL-DETAILS] Submitting reply:', { ratingId, userId: user.userId });

            await client.post(`/api/hostel/hostels/ratings/${ratingId}/reply?userId=${user.userId}`, {
                replyText: replyText
            });

            toast.success('Reply added successfully!');

            // Refresh reviews
            const reviewsResponse = await client.get<Review[]>(`/api/hostels/${id}/ratings`);
            const filteredReviews = reviewsResponse.data.filter(
                (review) => review.reviewText && review.reviewText.trim() !== '' && review.userId !== user.userId
            );
            setReviews(filteredReviews);

        } catch (err: any) {
            console.error('🏠 [HOSTEL-DETAILS] Reply submission error:', err);
            toast.error(err?.response?.data?.message || 'Failed to submit reply');
        }
    };

    // Render facilities
    const renderFacilities = () => {
        const facilities = [
            { name: 'WiFi', icon: Wifi, available: hostel?.hasWifi },
            { name: 'AC', icon: Wind, available: hostel?.hasAc },
            { name: 'Mess', icon: UtensilsCrossed, available: hostel?.hasMess },
            { name: 'Laundry', icon: Shirt, available: hostel?.hasLaundry },
        ];

        return (
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
                {facilities.map(({ name, icon: Icon, available }) => (
                    <div
                        key={name}
                        className={`flex items-center gap-2 p-3 rounded-lg border ${available ? 'bg-green-50 border-green-200' : 'bg-gray-50 border-gray-200 opacity-50'
                            }`}
                    >
                        <Icon className={`h-5 w-5 ${available ? 'text-green-600' : 'text-gray-400'}`} />
                        <span className={`text-sm font-medium ${available ? 'text-green-900' : 'text-gray-500'}`}>
                            {name}
                        </span>
                    </div>
                ))}
            </div>
        );
    };

    // Get gender badge color
    const getGenderBadgeColor = (gender: string) => {
        const colors = {
            BOYS: 'bg-blue-100 text-blue-800',
            GIRLS: 'bg-pink-100 text-pink-800',
            BOTH: 'bg-purple-100 text-purple-800'
        };
        return colors[gender as keyof typeof colors] || 'bg-gray-100 text-gray-800';
    };

    // Loading state
    if (loading) {
        return (
            <div className="min-h-screen bg-gradient-to-b from-background to-muted/20">
                <div className="container mx-auto px-4 py-6 max-w-6xl">
                    <Skeleton className="h-10 w-32 mb-6" />
                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                        <div className="lg:col-span-2 space-y-6">
                            <Skeleton className="h-96 w-full rounded-xl" />
                            <Skeleton className="h-64 w-full rounded-xl" />
                        </div>
                        <div className="space-y-6">
                            <Skeleton className="h-48 w-full rounded-xl" />
                            <Skeleton className="h-48 w-full rounded-xl" />
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    // Error state
    if (error || !hostel) {
        return (
            <div className="min-h-screen bg-gradient-to-b from-background to-muted/20">
                <div className="container mx-auto px-4 py-6 max-w-6xl">
                    <Button
                        variant="ghost"
                        onClick={() => navigate('/hostels')}
                        className="mb-6 hover:bg-primary/10"
                    >
                        <ArrowLeft className="h-4 w-4 mr-2" />
                        Back to Browse
                    </Button>
                    <Alert variant="destructive">
                        <AlertDescription>{error || 'Hostel not found'}</AlertDescription>
                    </Alert>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-b from-background to-muted/20">
            <div className="container mx-auto px-4 py-6 max-w-6xl">
                {/* Back Button */}
                <Button
                    variant="ghost"
                    onClick={() => navigate('/hostels')}
                    className="mb-6 hover:bg-primary/10"
                >
                    <ArrowLeft className="h-4 w-4 mr-2" />
                    Back to Browse
                </Button>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                    {/* Main Content */}
                    <div className="lg:col-span-2 space-y-6">
                        {/* Image Gallery */}
                        {hostel.images && hostel.images.length > 0 && (
                            <ImageGallery images={hostel.images} altText={hostel.hostelName} />
                        )}

                        {/* Hostel Information */}
                        <Card className="shadow-lg">
                            <CardHeader>
                                <div className="flex items-start justify-between">
                                    <div className="flex-1">
                                        <CardTitle className="text-3xl font-bold mb-2">{hostel.hostelName}</CardTitle>
                                        <div className="flex items-center gap-2 text-muted-foreground mb-3">
                                            <MapPin className="h-4 w-4" />
                                            <span>{hostel.address}, {hostel.city}</span>
                                        </div>
                                        {hostel.locality && (
                                            <p className="text-sm text-muted-foreground">Locality: {hostel.locality}</p>
                                        )}
                                        {hostel.landmark && (
                                            <p className="text-sm text-muted-foreground">Landmark: {hostel.landmark}</p>
                                        )}
                                    </div>
                                    <Badge className={getGenderBadgeColor(hostel.forGender)} variant="secondary">
                                        {hostel.forGender}
                                    </Badge>
                                </div>
                            </CardHeader>
                            <CardContent className="space-y-6">
                                {/* Description */}
                                {hostel.description && (
                                    <div>
                                        <h3 className="font-semibold text-lg mb-2">About</h3>
                                        <p className="text-muted-foreground leading-relaxed">{hostel.description}</p>
                                    </div>
                                )}

                                {/* Rent Range */}
                                <div>
                                    <h3 className="font-semibold text-lg mb-2">Monthly Rent</h3>
                                    <p className="text-2xl font-bold text-primary">
                                        ₹{hostel.monthlyRentMin} - ₹{hostel.monthlyRentMax}
                                    </p>
                                </div>

                                {/* Distance from CDAC */}
                                {hostel.distanceFromCdac && (
                                    <div>
                                        <h3 className="font-semibold text-lg mb-2">Distance from CDAC</h3>
                                        <p className="text-muted-foreground">{hostel.distanceFromCdac} km</p>
                                    </div>
                                )}

                                {/* Room Capacity */}
                                {hostel.roomCapacity && (
                                    <div>
                                        <h3 className="font-semibold text-lg mb-2">Room Capacity</h3>
                                        <div className="flex items-center gap-2">
                                            <Users className="h-5 w-5 text-muted-foreground" />
                                            <span className="text-muted-foreground">{hostel.roomCapacity} person(s) per room</span>
                                        </div>
                                    </div>
                                )}

                                {/* Amenities/Facilities */}
                                <div>
                                    <h3 className="font-semibold text-lg mb-3">Amenities</h3>
                                    {renderFacilities()}
                                </div>

                                {/* Categories */}
                                {hostel.categories && hostel.categories.length > 0 && (
                                    <div>
                                        <h3 className="font-semibold text-lg mb-3">Categories</h3>
                                        <div className="flex flex-wrap gap-2">
                                            {hostel.categories.map((category, idx) => (
                                                <Badge key={idx} variant="outline" className="px-3 py-1">
                                                    {category}
                                                </Badge>
                                            ))}
                                        </div>
                                    </div>
                                )}

                                {/* Contact Information */}
                                <div>
                                    <h3 className="font-semibold text-lg mb-3">Contact Information</h3>
                                    <div className="space-y-2">
                                        <div className="flex items-center gap-2">
                                            <Home className="h-4 w-4 text-muted-foreground" />
                                            <span className="text-muted-foreground">{hostel.contactPersonName}</span>
                                        </div>
                                        <div className="flex items-center gap-2">
                                            <Phone className="h-4 w-4 text-muted-foreground" />
                                            <a href={`tel:${hostel.contactPersonPhone}`} className="text-primary hover:underline">
                                                {hostel.contactPersonPhone}
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </CardContent>
                        </Card>

                        {/* Reviews Section */}
                        <Card className="shadow-lg">
                            <CardHeader>
                                <CardTitle className="text-2xl">Reviews</CardTitle>
                            </CardHeader>
                            <CardContent className="space-y-4">
                                {loadingReviews ? (
                                    <div className="space-y-4">
                                        {[...Array(3)].map((_, i) => (
                                            <Skeleton key={i} className="h-32 w-full" />
                                        ))}
                                    </div>
                                ) : reviews.length === 0 ? (
                                    <p className="text-center text-muted-foreground py-8">
                                        No reviews yet. Be the first to review!
                                    </p>
                                ) : (
                                    reviews.map((review) => (
                                        <ReviewCard
                                            key={review.ratingId}
                                            review={review}
                                            onReplySubmit={handleReplySubmit}
                                        />
                                    ))
                                )}
                            </CardContent>
                        </Card>
                    </div>

                    {/* Sidebar */}
                    <div className="space-y-6">
                        {/* Rating Stats */}
                        <Card className="shadow-lg">
                            <CardHeader>
                                <CardTitle className="flex items-center gap-2">
                                    <Star className="h-5 w-5 text-yellow-500" />
                                    Overall Rating
                                </CardTitle>
                            </CardHeader>
                            <CardContent>
                                <div className="text-center mb-6">
                                    <span className="text-5xl font-bold">{(ratingStats?.overallRating || 0).toFixed(1)}</span>
                                    <p className="text-muted-foreground mt-2">
                                        Based on Bayesian ranking
                                    </p>
                                </div>
                                {ratingStats && (
                                    <div className="space-y-3">
                                        <div className="flex justify-between text-sm">
                                            <span>🧹 Cleanliness</span>
                                            <span className="font-semibold">{ratingStats.cleanlinessRating.toFixed(1)}</span>
                                        </div>
                                        <div className="flex justify-between text-sm">
                                            <span>🍽️ Food Quality</span>
                                            <span className="font-semibold">{ratingStats.foodQualityRating.toFixed(1)}</span>
                                        </div>
                                        <div className="flex justify-between text-sm">
                                            <span>🔒 Safety</span>
                                            <span className="font-semibold">{ratingStats.safetyRating.toFixed(1)}</span>
                                        </div>
                                        <div className="flex justify-between text-sm">
                                            <span>📍 Location</span>
                                            <span className="font-semibold">{ratingStats.locationRating.toFixed(1)}</span>
                                        </div>
                                        <div className="flex justify-between text-sm">
                                            <span>💰 Affordability</span>
                                            <span className="font-semibold">{ratingStats.affordabilityRating.toFixed(1)}</span>
                                        </div>
                                    </div>
                                )}
                            </CardContent>
                        </Card>

                        {/* User Rating */}
                        {user && (
                            <Card className="shadow-lg">
                                <CardHeader>
                                    <CardTitle>Rate This Hostel</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <MultiCriteriaRating
                                        value={userRatings}
                                        onChange={handleRatingSubmit}
                                        disabled={isSubmittingRating}
                                    />
                                    {hasRated && (
                                        <p className="text-sm text-muted-foreground mt-4 text-center">
                                            You have rated this hostel
                                        </p>
                                    )}
                                </CardContent>
                            </Card>
                        )}

                        {/* Review Form */}
                        {user && (
                            <Card className="shadow-lg">
                                <CardHeader>
                                    <CardTitle>Write a Review</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <ReviewForm
                                        onSubmit={handleReviewSubmit}
                                        isSubmitting={isSubmittingReview}
                                        disabled={!hasRated}
                                        placeholder={!hasRated ? "Please rate this hostel first before writing a review" : "Share your experience..."}
                                    />
                                </CardContent>
                            </Card>
                        )}

                        {/* Login Prompt */}
                        {!user && (
                            <Card className="shadow-lg border-primary/20">
                                <CardContent className="pt-6">
                                    <p className="text-center text-muted-foreground mb-4">
                                        Log in to rate and review this hostel
                                    </p>
                                    <Button
                                        className="w-full"
                                        onClick={() => navigate('/login')}
                                    >
                                        Log In
                                    </Button>
                                </CardContent>
                            </Card>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}
