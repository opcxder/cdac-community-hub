import { useState } from "react";
import { useNavigate } from "react-router-dom";
import type { AxiosError } from "axios";

import client from "@/api/client";
import { useAuthStore, type User } from "@/stores/authStore";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Alert, AlertDescription } from "@/components/ui/alert";

interface LoginPageResponse {
    user: User;
    accessToken: string;
    refreshToken: string;
}

interface ErrorResponse {
    message?: string;
}

export default function LoginPage() {
    const navigate = useNavigate();
    const setAuth = useAuthStore((state) => state.setAuth);

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (loading) return;

        console.log('🔐 [LOGIN] Starting login attempt for:', email);
        setLoading(true);
        setError(null);

        try {
            console.log('📡 [LOGIN] Making API call to /api/auth/login');
            const response = await client.post<LoginPageResponse>(
                "/api/auth/login",
                { email, password }
            );

            console.log('✅ [LOGIN] API call successful!');
            console.log('📦 [LOGIN] Response data:', response.data);
            console.log('📦 [LOGIN] Response status:', response.status);
            console.log('📦 [LOGIN] Response headers:', response.headers);

            const { user, accessToken, refreshToken } = response.data;

            console.log('👤 [LOGIN] User:', user);
            console.log('🔑 [LOGIN] Access Token:', accessToken ? 'Present' : 'Missing');
            console.log('🔑 [LOGIN] Refresh Token:', refreshToken ? 'Present' : 'Missing');

            // Persist auth state (user + tokens)
            setAuth(user, accessToken, refreshToken);

            console.log('✅ [LOGIN] Auth state set, redirecting...');
            // Role-based redirect
            navigate(user.userId === -1 ? "/admin" : "/dashboard");
        } catch (err) {
            console.error('❌ [LOGIN] Login failed!');
            console.error('❌ [LOGIN] Error object:', err);

            const error = err as AxiosError<ErrorResponse>;
            console.error('❌ [LOGIN] Error response:', error.response);
            console.error('❌ [LOGIN] Error status:', error.response?.status);
            console.error('❌ [LOGIN] Error data:', error.response?.data);
            console.error('❌ [LOGIN] Error message:', error.message);

            const status = error.response?.status;
            const message = error.response?.data?.message;

            if (status === 401) {
                setError(message || "Invalid email or password");
            } else if (status === 403) {
                setError(message || "Your account is pending approval");
            } else if (!error.response) {
                console.error('❌ [LOGIN] No response received - network error');
                setError("Network error. Please check your connection.");
            } else {
                setError(message || "Login failed. Please try again.");
            }
        } finally {
            setLoading(false);
            console.log('✅ [LOGIN] Login attempt complete');
        }
    };

    return (
        <div className="flex min-h-screen items-center justify-center bg-background">
            <Card className="w-full max-w-md">
                <CardHeader>
                    <CardTitle className="text-center text-2xl">
                        Sign in
                    </CardTitle>
                </CardHeader>

                <CardContent>
                    <form onSubmit={handleSubmit} className="space-y-6">
                        {error && (
                            <Alert variant="destructive">
                                <AlertDescription>{error}</AlertDescription>
                            </Alert>
                        )}

                        <div className="space-y-2">
                            <Label htmlFor="email">Email</Label>
                            <Input
                                id="email"
                                type="email"
                                placeholder="admin@cdac.in"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                                autoComplete="email"
                            />
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="password">Password</Label>
                            <Input
                                id="password"
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                autoComplete="current-password"
                            />
                        </div>

                        <Button
                            type="submit"
                            className="w-full"
                            disabled={loading}
                        >
                            {loading ? "Signing in..." : "Sign in"}
                        </Button>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
}
