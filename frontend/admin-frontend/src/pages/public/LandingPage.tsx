import { Link } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import {
    UtensilsCrossed,
    Home,
    Shield,
    Users,
    Star,
    MapPin,
    ArrowRight,
    CheckCircle2
} from 'lucide-react';

/**
 * Landing Page
 * First page users see when visiting the application
 */
export default function LandingPage() {
    return (
        <div className="min-h-screen bg-gradient-to-b from-background to-muted">
            {/* Header */}
            <header className="border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
                <div className="container mx-auto flex h-16 items-center justify-between px-4">
                    <div className="flex items-center gap-2">
                        <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-primary">
                            <UtensilsCrossed className="h-6 w-6 text-primary-foreground" />
                        </div>
                        <span className="text-xl font-bold">CDAC Connect</span>
                    </div>
                    <div className="flex items-center gap-4">
                        <Link to="/login">
                            <Button variant="ghost">Login</Button>
                        </Link>
                        <Link to="/signup">
                            <Button>Sign Up</Button>
                        </Link>
                    </div>
                </div>
            </header>

            {/* Hero Section */}
            <section className="container mx-auto px-4 py-20 text-center">
                <div className="mx-auto max-w-3xl space-y-6">
                    <h1 className="text-5xl font-bold tracking-tight sm:text-6xl">
                        Your Gateway to{' '}
                        <span className="bg-gradient-to-r from-primary to-primary/60 bg-clip-text text-transparent">
                            Food & Accommodation
                        </span>
                    </h1>
                    <p className="text-xl text-muted-foreground">
                        Discover the best food places and hostels near CDAC. Share your experiences,
                        find verified listings, and make informed decisions.
                    </p>
                    <div className="flex flex-col items-center justify-center gap-4 sm:flex-row">
                        <Link to="/signup">
                            <Button size="lg" className="gap-2">
                                Get Started <ArrowRight className="h-4 w-4" />
                            </Button>
                        </Link>
                        <Link to="/food">
                            <Button size="lg" variant="outline">
                                Browse Food Places
                            </Button>
                        </Link>
                    </div>
                </div>
            </section>

            {/* Features Section */}
            <section className="container mx-auto px-4 py-16">
                <div className="mb-12 text-center">
                    <h2 className="text-3xl font-bold">Why Choose CDAC Connect?</h2>
                    <p className="mt-2 text-muted-foreground">
                        Everything you need to find the perfect place
                    </p>
                </div>

                <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                    {/* Feature 1 */}
                    <Card>
                        <CardHeader>
                            <div className="mb-2 flex h-12 w-12 items-center justify-center rounded-lg bg-primary/10">
                                <UtensilsCrossed className="h-6 w-6 text-primary" />
                            </div>
                            <CardTitle>Verified Food Places</CardTitle>
                            <CardDescription>
                                Browse admin-approved restaurants, cafes, and eateries near CDAC
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <ul className="space-y-2 text-sm text-muted-foreground">
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Real reviews from students
                                </li>
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Detailed menus and pricing
                                </li>
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Location and contact info
                                </li>
                            </ul>
                        </CardContent>
                    </Card>

                    {/* Feature 2 */}
                    <Card>
                        <CardHeader>
                            <div className="mb-2 flex h-12 w-12 items-center justify-center rounded-lg bg-primary/10">
                                <Home className="h-6 w-6 text-primary" />
                            </div>
                            <CardTitle>Quality Hostels</CardTitle>
                            <CardDescription>
                                Find comfortable and affordable accommodation options
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <ul className="space-y-2 text-sm text-muted-foreground">
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Verified hostel listings
                                </li>
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Amenities and facilities
                                </li>
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Distance from CDAC
                                </li>
                            </ul>
                        </CardContent>
                    </Card>

                    {/* Feature 3 */}
                    <Card>
                        <CardHeader>
                            <div className="mb-2 flex h-12 w-12 items-center justify-center rounded-lg bg-primary/10">
                                <Users className="h-6 w-6 text-primary" />
                            </div>
                            <CardTitle>Community Driven</CardTitle>
                            <CardDescription>
                                Share your experiences and help fellow students
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <ul className="space-y-2 text-sm text-muted-foreground">
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Submit new places
                                </li>
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Rate and review
                                </li>
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Suggest improvements
                                </li>
                            </ul>
                        </CardContent>
                    </Card>

                    {/* Feature 4 */}
                    <Card>
                        <CardHeader>
                            <div className="mb-2 flex h-12 w-12 items-center justify-center rounded-lg bg-primary/10">
                                <Shield className="h-6 w-6 text-primary" />
                            </div>
                            <CardTitle>Admin Verified</CardTitle>
                            <CardDescription>
                                All listings are reviewed and approved by admins
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <ul className="space-y-2 text-sm text-muted-foreground">
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Quality assurance
                                </li>
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Spam prevention
                                </li>
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Accurate information
                                </li>
                            </ul>
                        </CardContent>
                    </Card>

                    {/* Feature 5 */}
                    <Card>
                        <CardHeader>
                            <div className="mb-2 flex h-12 w-12 items-center justify-center rounded-lg bg-primary/10">
                                <Star className="h-6 w-6 text-primary" />
                            </div>
                            <CardTitle>Ratings & Reviews</CardTitle>
                            <CardDescription>
                                Make informed decisions based on real feedback
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <ul className="space-y-2 text-sm text-muted-foreground">
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Honest reviews
                                </li>
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Star ratings
                                </li>
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Photo uploads
                                </li>
                            </ul>
                        </CardContent>
                    </Card>

                    {/* Feature 6 */}
                    <Card>
                        <CardHeader>
                            <div className="mb-2 flex h-12 w-12 items-center justify-center rounded-lg bg-primary/10">
                                <MapPin className="h-6 w-6 text-primary" />
                            </div>
                            <CardTitle>Location Based</CardTitle>
                            <CardDescription>
                                Find places near you or near CDAC campus
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <ul className="space-y-2 text-sm text-muted-foreground">
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Distance information
                                </li>
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Locality details
                                </li>
                                <li className="flex items-center gap-2">
                                    <CheckCircle2 className="h-4 w-4 text-primary" />
                                    Easy navigation
                                </li>
                            </ul>
                        </CardContent>
                    </Card>
                </div>
            </section>

            {/* CTA Section */}
            <section className="container mx-auto px-4 py-16">
                <Card className="border-primary/20 bg-gradient-to-r from-primary/5 to-primary/10">
                    <CardContent className="flex flex-col items-center justify-between gap-6 p-12 md:flex-row">
                        <div className="text-center md:text-left">
                            <h3 className="text-2xl font-bold">Ready to get started?</h3>
                            <p className="mt-2 text-muted-foreground">
                                Join the CDAC Connect community today and discover the best places around you.
                            </p>
                        </div>
                        <div className="flex gap-4">
                            <Link to="/signup">
                                <Button size="lg">Create Account</Button>
                            </Link>
                            <Link to="/login">
                                <Button size="lg" variant="outline">
                                    Login
                                </Button>
                            </Link>
                        </div>
                    </CardContent>
                </Card>
            </section>

            {/* Footer */}
            <footer className="border-t bg-background">
                <div className="container mx-auto px-4 py-8">
                    <div className="flex flex-col items-center justify-between gap-4 md:flex-row">
                        <div className="flex items-center gap-2">
                            <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary">
                                <UtensilsCrossed className="h-5 w-5 text-primary-foreground" />
                            </div>
                            <span className="font-semibold">CDAC Connect</span>
                        </div>
                        <p className="text-sm text-muted-foreground">
                            © 2026 CDAC Connect. All rights reserved.
                        </p>
                        <div className="flex gap-4">
                            <Link to="/food" className="text-sm text-muted-foreground hover:text-foreground">
                                Browse Food
                            </Link>
                            <Link to="/hostels" className="text-sm text-muted-foreground hover:text-foreground">
                                Browse Hostels
                            </Link>
                        </div>
                    </div>
                </div>
            </footer>
        </div>
    );
}
