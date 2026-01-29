import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import client from "@/api/client";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { CheckCircle2, RefreshCw } from "lucide-react";

const signupSchema = z.object({
    email: z.string().email("Invalid email address"),
    phone: z.string().optional(),
    password: z.string().min(8, "Password must be at least 8 characters"),
    confirmPassword: z.string()
}).refine(data => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ["confirmPassword"]
});

type SignupFormData = z.infer<typeof signupSchema>;

export default function SignupPage() {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState(false);
    const [generatedUsername, setGeneratedUsername] = useState<string>("");
    const [generatingUsername, setGeneratingUsername] = useState(false);

    const { register, handleSubmit, formState: { errors } } = useForm<SignupFormData>({
        resolver: zodResolver(signupSchema)
    });

    // Generate username function
    const generateUsername = async () => {
        setGeneratingUsername(true);
        try {
            const response = await client.get<{ username: string }>('/api/auth/generate-username');
            setGeneratedUsername(response.data.username);
        } catch (err) {
            console.error('Failed to generate username:', err);
            // Fallback to client-side generation
            setGeneratedUsername('user_' + Math.random().toString(36).substring(7));
        } finally {
            setGeneratingUsername(false);
        }
    };

    // Generate username on mount
    useEffect(() => {
        generateUsername();
    }, []);

    const onSubmit = async (data: SignupFormData) => {
        setLoading(true);
        setError(null);

        try {
            await client.post('/api/auth/register', {
                email: data.email,
                phone: data.phone || null,
                password: data.password
            });

            setSuccess(true);

            // Redirect to login after 3 seconds
            setTimeout(() => {
                navigate('/login');
            }, 3000);
        } catch (err: unknown) {
            const message = (err as { response?: { data?: { message?: string } } })?.response?.data?.message || "Registration failed. Please try again.";
            setError(message);
        } finally {
            setLoading(false);
        }
    };

    if (success) {
        return (
            <div className="flex min-h-screen items-center justify-center bg-background p-4">
                <Card className="w-full max-w-md">
                    <CardHeader className="text-center">
                        <div className="mx-auto mb-4 flex h-12 w-12 items-center justify-center rounded-full bg-green-100">
                            <CheckCircle2 className="h-6 w-6 text-green-600" />
                        </div>
                        <CardTitle className="text-2xl">Account Created!</CardTitle>
                        <CardDescription>
                            Your account has been created successfully and is awaiting admin approval.
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="text-center">
                        <p className="text-sm text-muted-foreground mb-4">
                            You will be able to login once an administrator approves your account.
                            You'll receive a notification via email.
                        </p>
                        <p className="text-sm text-muted-foreground">
                            Redirecting to login page...
                        </p>
                    </CardContent>
                </Card>
            </div>
        );
    }

    return (
        <div className="flex min-h-screen items-center justify-center bg-background p-4">
            <Card className="w-full max-w-md">
                <CardHeader>
                    <CardTitle className="text-center text-2xl">Create Account</CardTitle>
                    <CardDescription className="text-center">
                        Join the CDAC Community Hub
                    </CardDescription>
                </CardHeader>

                <CardContent>
                    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                        {error && (
                            <Alert variant="destructive">
                                <AlertDescription>{error}</AlertDescription>
                            </Alert>
                        )}

                        {/* Generated Username Display */}
                        <div className="space-y-2">
                            <Label>Your Username</Label>
                            <div className="flex gap-2">
                                <Input
                                    value={generatedUsername}
                                    disabled
                                    className="bg-muted"
                                />
                                <Button
                                    type="button"
                                    variant="outline"
                                    size="icon"
                                    onClick={generateUsername}
                                    disabled={generatingUsername || loading}
                                    title="Generate new username"
                                >
                                    <RefreshCw className={`h-4 w-4 ${generatingUsername ? 'animate-spin' : ''}`} />
                                </Button>
                            </div>
                            <p className="text-xs text-muted-foreground">
                                Your username will be auto-generated. Click refresh if you'd like a different one.
                            </p>
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="email">Email *</Label>
                            <Input
                                id="email"
                                type="email"
                                placeholder="your.email@example.com"
                                {...register("email")}
                                disabled={loading}
                            />
                            {errors.email && (
                                <p className="text-sm text-red-600">{errors.email.message}</p>
                            )}
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="phone">Phone (Optional)</Label>
                            <Input
                                id="phone"
                                type="tel"
                                placeholder="+91 1234567890"
                                {...register("phone")}
                                disabled={loading}
                            />
                            {errors.phone && (
                                <p className="text-sm text-red-600">{errors.phone.message}</p>
                            )}
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="password">Password *</Label>
                            <Input
                                id="password"
                                type="password"
                                placeholder="••••••••"
                                {...register("password")}
                                disabled={loading}
                            />
                            {errors.password && (
                                <p className="text-sm text-red-600">{errors.password.message}</p>
                            )}
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="confirmPassword">Confirm Password *</Label>
                            <Input
                                id="confirmPassword"
                                type="password"
                                placeholder="••••••••"
                                {...register("confirmPassword")}
                                disabled={loading}
                            />
                            {errors.confirmPassword && (
                                <p className="text-sm text-red-600">{errors.confirmPassword.message}</p>
                            )}
                        </div>

                        <Button
                            type="submit"
                            className="w-full"
                            disabled={loading || !generatedUsername}
                        >
                            {loading ? "Creating Account..." : "Sign Up"}
                        </Button>

                        <div className="text-center text-sm">
                            <span className="text-muted-foreground">Already have an account? </span>
                            <Link to="/login" className="text-primary hover:underline">
                                Sign in
                            </Link>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
}
