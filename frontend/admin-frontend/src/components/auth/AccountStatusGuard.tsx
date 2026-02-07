import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "@/stores/authStore";
import { CheckCircle, XCircle, Clock } from "lucide-react";

export default function AccountStatusGuard({ children }: { children: React.ReactNode }) {
    const user = useAuthStore((state) => state.user);
    const navigate = useNavigate();

    if (!user) {
        return null;
    }

    // Admin users bypass all checks
    if (user.userId === -1) {
        return <>{children}</>;
    }

    // Check account status
    if (user.accountStatus === "PENDING") {
        return (
            <div className="container mx-auto py-6 max-w-2xl">
                <Alert className="border-yellow-500 bg-yellow-50">
                    <Clock className="h-5 w-5 text-yellow-600" />
                    <AlertTitle className="text-yellow-900 font-semibold">Account Pending Approval</AlertTitle>
                    <AlertDescription className="text-yellow-800">
                        Your account is currently pending approval from an administrator. You will be able to access all features once your account is approved.
                    </AlertDescription>
                </Alert>

                <Card className="mt-6">
                    <CardHeader>
                        <CardTitle>What happens next?</CardTitle>
                        <CardDescription>Here's what you need to know while waiting</CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <div className="flex items-start gap-3">
                            <CheckCircle className="h-5 w-5 text-green-600 mt-0.5" />
                            <div>
                                <p className="font-medium">Account Created</p>
                                <p className="text-sm text-muted-foreground">Your account has been successfully created</p>
                            </div>
                        </div>
                        <div className="flex items-start gap-3">
                            <Clock className="h-5 w-5 text-yellow-600 mt-0.5" />
                            <div>
                                <p className="font-medium">Waiting for Approval</p>
                                <p className="text-sm text-muted-foreground">An administrator will review your account shortly</p>
                            </div>
                        </div>


                        <div className="pt-4 border-t">
                            <Button onClick={() => navigate("/dashboard")} variant="outline" className="w-full">
                                Back to Dashboard
                            </Button>
                        </div>
                    </CardContent>
                </Card>
            </div>
        );
    }

    if (user.accountStatus === "REJECTED") {
        return (
            <div className="container mx-auto py-6 max-w-2xl">
                <Alert className="border-red-500 bg-red-50">
                    <XCircle className="h-5 w-5 text-red-600" />
                    <AlertTitle className="text-red-900 font-semibold">Account Rejected</AlertTitle>
                    <AlertDescription className="text-red-800">
                        Unfortunately, your account registration has been rejected by an administrator.
                    </AlertDescription>
                </Alert>

                <Card className="mt-6">
                    <CardHeader>
                        <CardTitle>Why was my account rejected?</CardTitle>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <div className="bg-zinc-50 p-4 rounded-md">
                            <p className="text-sm font-medium mb-2">Rejection Reason:</p>
                            <p className="text-sm text-muted-foreground">
                                {user.rejectionReason || "No specific reason provided. Please contact support for more information."}
                            </p>
                        </div>

                        <div className="space-y-2">
                            <p className="text-sm font-medium">What can I do?</p>
                            <ul className="list-disc list-inside space-y-1 text-sm text-muted-foreground ml-4">
                                <li>Review the rejection reason above</li>
                                <li>Contact support if you believe this was a mistake</li>
                                <li>Create a new account with correct information</li>
                            </ul>
                        </div>

                        <div className="pt-4 border-t flex gap-2">
                            <Button onClick={() => navigate("/dashboard")} variant="outline" className="flex-1">
                                Back to Dashboard
                            </Button>
                            <Button onClick={() => navigate("/signup")} className="flex-1">
                                Create New Account
                            </Button>
                        </div>
                    </CardContent>
                </Card>
            </div>
        );
    }

    // Account is approved
    return <>{children}</>;
}
