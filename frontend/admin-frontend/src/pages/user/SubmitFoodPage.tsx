import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import client from "@/api/client";
import { useAuthStore } from "@/stores/authStore";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Alert, AlertDescription } from "@/components/ui/alert";
import ImageUpload from "@/components/form/ImageUpload";
import { toast } from "sonner";

const foodPlaceSchema = z.object({
    placeName: z.string().min(3, "Name must be at least 3 characters"),
    description: z.string().optional(),
    address: z.string().min(5, "Address is required"),
    city: z.string().min(2, "City is required"),
    locality: z.string().min(2, "Locality is required"),
    landmark: z.string().optional(),
    priceRange: z.enum(["BUDGET", "MEDIUM", "EXPENSIVE"]),
    categoryId: z.number({ required_error: "Please select a category" })
});

type FoodPlaceFormData = z.infer<typeof foodPlaceSchema>;

interface Category {
    categoryId: number;
    categoryName: string;
}

export default function SubmitFoodPage() {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [categories, setCategories] = useState<Category[]>([]);
    const [images, setImages] = useState<File[]>([]);
    const [imageError, setImageError] = useState<string>("");

    const { register, handleSubmit, formState: { errors }, setValue, watch } = useForm<FoodPlaceFormData>({
        resolver: zodResolver(foodPlaceSchema)
    });

    const selectedCategoryId = watch("categoryId");

    useEffect(() => {
        async function fetchCategories() {
            try {
                console.log('📡 [SUBMIT-FOOD] Fetching categories from /api/food/categories');
                const response = await client.get<Category[]>("/api/food/categories");
                console.log('✅ [SUBMIT-FOOD] Categories fetched successfully:', response.data);
                console.log('📊 [SUBMIT-FOOD] Number of categories:', response.data.length);
                setCategories(response.data);

                if (response.data.length === 0) {
                    console.warn('⚠️  [SUBMIT-FOOD] No categories available! Check if food-service is running and categories are seeded.');
                }
            } catch (err) {
                console.error("❌ [SUBMIT-FOOD] Failed to load categories:", err);
                console.error("❌ [SUBMIT-FOOD] Error details:", err);
            }
        }
        fetchCategories();
    }, []);

    const onSubmit = async (data: FoodPlaceFormData) => {
        // Validate images
        if (images.length === 0) {
            setImageError("Please upload at least 1 image");
            return;
        }
        setImageError("");

        setLoading(true);
        setError(null);

        try {
            console.log('🍽️ [SUBMIT-FOOD] Submitting food place data:', data);

            // Get userId from auth store
            const userId = useAuthStore.getState().user?.userId;
            if (!userId) {
                throw new Error("User not authenticated");
            }

            console.log('👤 [SUBMIT-FOOD] User ID:', userId);

            // Step 1: Create food place
            const placeResponse = await client.post("/api/food/places", {
                placeName: data.placeName,
                description: data.description || "",
                address: data.address,
                city: data.city,
                locality: data.locality,
                landmark: data.landmark || "",
                priceRange: data.priceRange,
                bestForCategoryId: data.categoryId,  // Main category
                categoryIds: [data.categoryId],      // Array of categories (for now just one)
                submittedByUserId: userId
            });

            console.log('✅ [SUBMIT-FOOD] Food place created successfully:', placeResponse.data);
            const placeId = placeResponse.data.placeId;

            // Step 2: Upload images
            const formData = new FormData();
            images.forEach(image => formData.append('images', image));

            console.log('📤 [SUBMIT-FOOD] Uploading images for place ID:', placeId);
            await client.post(`/api/food/places/${placeId}/images`, formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });

            console.log('✅ [SUBMIT-FOOD] Images uploaded successfully');
            toast.success("Food place submitted for approval!");
            navigate("/dashboard");
        } catch (err: any) {
            console.error("❌ [SUBMIT-FOOD] Submission failed:", err);
            console.error("❌ [SUBMIT-FOOD] Error response:", err?.response?.data);

            const message = err?.response?.data?.message || err?.response?.data?.error || err.message || "Failed to submit food place";
            setError(message);
            toast.error(message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-b from-background to-muted/20 py-4 sm:py-6 md:py-8">
            <div className="container mx-auto px-3 sm:px-4 max-w-3xl">
                <Card className="overflow-hidden rounded-xl shadow-lg border border-border/50">
                    <CardHeader className="bg-muted/30 border-b">
                        <CardTitle className="text-xl sm:text-2xl">Submit Food Place</CardTitle>
                        <CardDescription className="text-sm">
                            Share your favorite food spot with the CDAC community
                        </CardDescription>
                    </CardHeader>

                    <CardContent className="p-4 sm:p-6">
                        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4 md:space-y-6">
                            {error && (
                                <Alert variant="destructive">
                                    <AlertDescription>{error}</AlertDescription>
                                </Alert>
                            )}

                            <div className="space-y-2">
                                <Label htmlFor="placeName">Place Name *</Label>
                                <Input
                                    id="placeName"
                                    placeholder="e.g., Biryani House"
                                    {...register("placeName")}
                                    disabled={loading}
                                />
                                {errors.placeName && (
                                    <p className="text-sm text-red-600">{errors.placeName.message}</p>
                                )}
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="description">Description</Label>
                                <Textarea
                                    id="description"
                                    placeholder="Tell us about this place..."
                                    rows={3}
                                    {...register("description")}
                                    disabled={loading}
                                />
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <Label htmlFor="address">Address *</Label>
                                    <Input
                                        id="address"
                                        placeholder="Street address"
                                        {...register("address")}
                                        disabled={loading}
                                    />
                                    {errors.address && (
                                        <p className="text-sm text-red-600">{errors.address.message}</p>
                                    )}
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="city">City *</Label>
                                    <Input
                                        id="city"
                                        placeholder="e.g., Hyderabad"
                                        {...register("city")}
                                        disabled={loading}
                                    />
                                    {errors.city && (
                                        <p className="text-sm text-red-600">{errors.city.message}</p>
                                    )}
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <Label htmlFor="locality">Locality *</Label>
                                    <Input
                                        id="locality"
                                        placeholder="e.g., Gachibowli"
                                        {...register("locality")}
                                        disabled={loading}
                                    />
                                    {errors.locality && (
                                        <p className="text-sm text-red-600">{errors.locality.message}</p>
                                    )}
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="landmark">Landmark</Label>
                                    <Input
                                        id="landmark"
                                        placeholder="e.g., Near CDAC"
                                        {...register("landmark")}
                                        disabled={loading}
                                    />
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <Label htmlFor="priceRange">Price Range *</Label>
                                    <Select
                                        onValueChange={(value) => setValue("priceRange", value as any)}
                                        disabled={loading}
                                    >
                                        <SelectTrigger>
                                            <SelectValue placeholder="Select price range" />
                                        </SelectTrigger>
                                        <SelectContent>
                                            <SelectItem value="BUDGET">Budget (₹)</SelectItem>
                                            <SelectItem value="MEDIUM">Medium (₹₹)</SelectItem>
                                            <SelectItem value="EXPENSIVE">Expensive (₹₹₹)</SelectItem>
                                        </SelectContent>
                                    </Select>
                                    {errors.priceRange && (
                                        <p className="text-sm text-red-600">{errors.priceRange.message}</p>
                                    )}
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="category">Category *</Label>
                                    <Select
                                        onValueChange={(value) => setValue("categoryId", parseInt(value))}
                                        disabled={loading}
                                    >
                                        <SelectTrigger>
                                            <SelectValue placeholder="Select category" />
                                        </SelectTrigger>
                                        <SelectContent>
                                            {categories.map((cat) => (
                                                <SelectItem key={cat.categoryId} value={cat.categoryId.toString()}>
                                                    {cat.categoryName}
                                                </SelectItem>
                                            ))}
                                        </SelectContent>
                                    </Select>
                                    {errors.categoryId && (
                                        <p className="text-sm text-red-600">{errors.categoryId.message}</p>
                                    )}
                                </div>
                            </div>

                            <ImageUpload
                                images={images}
                                onChange={setImages}
                                maxImages={5}
                                error={imageError}
                            />

                            <div className="flex flex-col sm:flex-row gap-3 sm:gap-4 pt-2">
                                <Button
                                    type="button"
                                    variant="outline"
                                    onClick={() => navigate("/dashboard")}
                                    disabled={loading}
                                    className="w-full sm:flex-1 h-12"
                                >
                                    Cancel
                                </Button>
                                <Button
                                    type="submit"
                                    disabled={loading}
                                    className="w-full sm:flex-1 h-12"
                                >
                                    {loading ? "Submitting..." : "Submit for Approval"}
                                </Button>
                            </div>
                        </form>
                    </CardContent>
                </Card>
            </div>
        </div>
    );
}
