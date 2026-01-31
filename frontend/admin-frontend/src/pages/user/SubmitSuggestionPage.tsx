import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useToast } from "@/hooks/use-toast";
import client from "@/api/client";
import { MessageSquare } from "lucide-react";
import { useAuthStore } from "@/stores/authStore";

export default function SubmitSuggestionPage() {
    const [suggestion, setSuggestion] = useState("");
    const [category, setCategory] = useState("OTHER");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const { toast } = useToast();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!suggestion.trim()) {
            toast({
                title: "Validation Error",
                description: "Please enter your suggestion",
                variant: "destructive",
            });
            return;
        }

        try {
            setLoading(true);

            // Get userId from auth store
            const userId = useAuthStore.getState().user?.userId;
            console.log("📝 [SUBMIT-SUGGESTION] Submitting suggestion:", {
                suggestionText: suggestion,
                category: category,
                userId: userId
            });

            const response = await client.post("/api/suggestions", {
                suggestionText: suggestion,
                category: category,
                userId: userId
            });

            console.log("✅ [SUBMIT-SUGGESTION] Success:", response.data);

            toast({
                title: "Success",
                description: "Your suggestion has been submitted successfully!",
            });
            setSuggestion("");
            setCategory("OTHER");
            navigate("/dashboard");
        } catch (error: any) {
            console.error("❌ [SUBMIT-SUGGESTION] Error:", error);
            console.error("❌ [SUBMIT-SUGGESTION] Response:", error.response?.data);
            toast({
                title: "Error",
                description: error.response?.data?.message || "Failed to submit suggestion. Please try again.",
                variant: "destructive",
            });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mx-auto py-6 max-w-2xl">
            <div className="mb-6">
                <h1 className="text-3xl font-bold">Share Your Suggestions</h1>
                <p className="text-muted-foreground">
                    Help us improve the platform with your feedback and ideas
                </p>
            </div>

            <Card>
                <CardHeader>
                    <div className="flex items-center gap-2">
                        <MessageSquare className="h-5 w-5 text-primary" />
                        <CardTitle>Submit a Suggestion</CardTitle>
                    </div>
                    <CardDescription>
                        Share your thoughts on how we can make this platform better for the CDAC community
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div className="space-y-2">
                            <Label htmlFor="category">Category *</Label>
                            <Select value={category} onValueChange={setCategory}>
                                <SelectTrigger>
                                    <SelectValue placeholder="Select a category" />
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="OTHER">General / Other</SelectItem>
                                    <SelectItem value="FACILITIES">Facilities</SelectItem>
                                    <SelectItem value="CANTEEN">Canteen</SelectItem>
                                    <SelectItem value="CLASSROOM">Classroom</SelectItem>
                                </SelectContent>
                            </Select>
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="suggestion">Your Suggestion *</Label>
                            <Textarea
                                id="suggestion"
                                placeholder="e.g., It would be great if we could filter food places by cuisine type..."
                                value={suggestion}
                                onChange={(e) => setSuggestion(e.target.value)}
                                rows={8}
                                className="resize-none"
                            />
                            <p className="text-sm text-muted-foreground">
                                Be as detailed as possible to help us understand your suggestion better
                            </p>
                        </div>

                        <div className="flex gap-2">
                            <Button type="submit" disabled={loading || !suggestion.trim()}>
                                {loading ? "Submitting..." : "Submit Suggestion"}
                            </Button>
                            <Button
                                type="button"
                                variant="outline"
                                onClick={() => navigate("/dashboard")}
                                disabled={loading}
                            >
                                Cancel
                            </Button>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
}
