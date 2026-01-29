import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import client from "@/api/client";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { CheckCircle2 } from "lucide-react";
import type { CreateSuggestionRequest } from "@/types/api";

const suggestionSchema = z.object({
    category: z.enum(['FOOD', 'HOSTEL', 'GENERAL'], {
        required_error: "Please select a category"
    }),
    content: z.string().min(10, "Suggestion must be at least 10 characters").max(1000, "Suggestion must be less than 1000 characters")
});

type SuggestionFormData = z.infer<typeof suggestionSchema>;

export default function SubmitSuggestionPage() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState(false);
    const [selectedCategory, setSelectedCategory] = useState<'FOOD' | 'HOSTEL' | 'GENERAL'>('GENERAL');

    const { register, handleSubmit, formState: { errors }, reset } = useForm<SuggestionFormData>({
        resolver: zodResolver(suggestionSchema),
        defaultValues: {
            category: 'GENERAL'
        }
    });

    const onSubmit = async (data: SuggestionFormData) => {
        setLoading(true);
        setError(null);

        try {
            // Get userId from localStorage (assuming it's stored after login)
            const userStr = localStorage.getItem('user');
            if (!userStr) {
                throw new Error('Please login first');
            }
            const user = JSON.parse(userStr) as { userId: number };

            const requestData: CreateSuggestionRequest = {
                userId: user.userId,
                category: data.category,
                content: data.content
            };

            await client.post('/api/suggestions', requestData);

            setSuccess(true);
            reset();

            // Reset success message after 5 seconds
            setTimeout(() => {
                setSuccess(false);
            }, 5000);
        } catch (err: unknown) {
            const message = (err as { response?: { data?: { message?: string } } })?.response?.data?.message ||
                (err as Error).message ||
                "Failed to submit suggestion. Please try again.";
            setError(message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mx-auto p-6 max-w-2xl">
            <Card>
                <CardHeader>
                    <CardTitle className="text-2xl">Submit a Suggestion</CardTitle>
                    <CardDescription>
                        Help us improve the CDAC Community Hub by sharing your ideas and feedback
                    </CardDescription>
                </CardHeader>

                <CardContent>
                    {success && (
                        <Alert className="mb-4 border-green-200 bg-green-50">
                            <CheckCircle2 className="h-4 w-4 text-green-600" />
                            <AlertDescription className="text-green-800">
                                Thank you! Your suggestion has been submitted successfully.
                            </AlertDescription>
                        </Alert>
                    )}

                    {error && (
                        <Alert variant="destructive" className="mb-4">
                            <AlertDescription>{error}</AlertDescription>
                        </Alert>
                    )}

                    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                        <div className="space-y-2">
                            <Label htmlFor="category">Category *</Label>
                            <Select
                                value={selectedCategory}
                                onValueChange={(value) => {
                                    setSelectedCategory(value as 'FOOD' | 'HOSTEL' | 'GENERAL');
                                }}
                                disabled={loading}
                            >
                                <SelectTrigger>
                                    <SelectValue placeholder="Select a category" />
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="GENERAL">General</SelectItem>
                                    <SelectItem value="FOOD">Food Places</SelectItem>
                                    <SelectItem value="HOSTEL">Hostels</SelectItem>
                                </SelectContent>
                            </Select>
                            <input type="hidden" {...register("category")} value={selectedCategory} />
                            {errors.category && (
                                <p className="text-sm text-red-600">{errors.category.message}</p>
                            )}
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="content">Your Suggestion *</Label>
                            <Textarea
                                id="content"
                                placeholder="Share your ideas, feedback, or suggestions here..."
                                rows={8}
                                {...register("content")}
                                disabled={loading}
                                className="resize-none"
                            />
                            {errors.content && (
                                <p className="text-sm text-red-600">{errors.content.message}</p>
                            )}
                            <p className="text-xs text-muted-foreground">
                                Minimum 10 characters, maximum 1000 characters
                            </p>
                        </div>

                        <div className="flex gap-3">
                            <Button
                                type="submit"
                                className="flex-1"
                                disabled={loading}
                            >
                                {loading ? "Submitting..." : "Submit Suggestion"}
                            </Button>
                            <Button
                                type="button"
                                variant="outline"
                                onClick={() => reset()}
                                disabled={loading}
                            >
                                Clear
                            </Button>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
}
