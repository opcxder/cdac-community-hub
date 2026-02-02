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
    CheckCircle2,
    Sparkles,
    TrendingUp
} from 'lucide-react';

/**
 * Landing Page
 * Modern, premium landing page with vibrant design
 */
export default function LandingPage() {
    return (
        <div className="min-h-screen bg-gradient-to-br from-background via-background to-primary/5">
            {/* Header */}
            <header className="sticky top-0 z-50 border-b bg-background/80 backdrop-blur-xl supports-[backdrop-filter]:bg-background/60">
                <div className="container mx-auto flex h-16 sm:h-20 items-center justify-between px-4 sm:px-6">
                    <div className="flex items-center gap-2 sm:gap-3">
                        <div className="flex h-10 w-10 sm:h-12 sm:w-12 items-center justify-center rounded-xl bg-gradient-to-br from-primary to-primary/60 shadow-lg shadow-primary/20">
                            <UtensilsCrossed className="h-5 w-5 sm:h-6 sm:w-6 text-primary-foreground" />
                        </div>
                        <span className="text-lg sm:text-xl font-bold bg-gradient-to-r from-foreground to-foreground/70 bg-clip-text">
                            CDAC Connect
                        </span>
                    </div>
                    <div className="flex items-center gap-2 sm:gap-4">
                        <Link to="/login">
                            <Button variant="ghost" size="sm" className="text-xs sm:text-sm">Login</Button>
                        </Link>
                        <Link to="/signup">
                            <Button size="sm" className="text-xs sm:text-sm shadow-lg shadow-primary/20">Sign Up</Button>
                        </Link>
                    </div>
                </div>
            </header>

            {/* Hero Section */}
            <section className="container mx-auto px-4 sm:px-6 py-16 sm:py-24 lg:py-32">
                <div className="mx-auto max-w-4xl text-center space-y-6 sm:space-y-8">
                    {/* Badge */}
                    <div className="inline-flex items-center gap-2 rounded-full bg-primary/10 px-4 py-1.5 text-xs sm:text-sm font-medium text-primary border border-primary/20">
                        <Sparkles className="h-3 w-3 sm:h-4 sm:w-4" />
                        <span>Trusted by CDAC Students</span>
                    </div>

                    {/* Main Heading */}
                    <h1 className="text-4xl sm:text-5xl md:text-6xl lg:text-7xl font-bold tracking-tight leading-tight">
                        Your Gateway to{' '}
                        <span className="bg-gradient-to-r from-primary via-primary/80 to-primary/60 bg-clip-text text-transparent animate-gradient">
                            Food & Accommodation
                        </span>
                    </h1>

                    {/* Subheading */}
                    <p className="text-base sm:text-lg md:text-xl text-muted-foreground max-w-2xl mx-auto leading-relaxed">
                        Discover the best food places and hostels near CDAC. Share your experiences,
                        find verified listings, and make informed decisions with our community-driven platform.
                    </p>

                    {/* CTA Buttons */}
                    <div className="flex flex-col sm:flex-row items-center justify-center gap-3 sm:gap-4 pt-4">
                        <Link to="/signup" className="w-full sm:w-auto">
                            <Button size="lg" className="w-full sm:w-auto gap-2 shadow-xl shadow-primary/25 hover:shadow-2xl hover:shadow-primary/30 transition-all">
                                Get Started Free <ArrowRight className="h-4 w-4" />
                            </Button>
                        </Link>
                        <Link to="/login" className="w-full sm:w-auto">
                            <Button size="lg" variant="outline" className="w-full sm:w-auto border-2 hover:bg-primary/5">
                                Login to Browse
                            </Button>
                        </Link>
                    </div>

                </div>
            </section>

            {/* Features Section */}
            <section className="container mx-auto px-4 sm:px-6 py-12 sm:py-16 lg:py-24">
                <div className="mb-12 sm:mb-16 text-center">
                    <div className="inline-flex items-center gap-2 rounded-full bg-primary/10 px-4 py-1.5 text-xs sm:text-sm font-medium text-primary border border-primary/20 mb-4">
                        <TrendingUp className="h-3 w-3 sm:h-4 sm:w-4" />
                        <span>Why Choose Us</span>
                    </div>
                    <h2 className="text-3xl sm:text-4xl md:text-5xl font-bold">Everything You Need</h2>
                    <p className="mt-3 sm:mt-4 text-base sm:text-lg text-muted-foreground max-w-2xl mx-auto">
                        Powerful features designed to help you find the perfect place
                    </p>
                </div>

                <div className="grid gap-4 sm:gap-6 md:grid-cols-2 lg:grid-cols-3">
                    {/* Feature 1 */}
                    <Card className="group hover:shadow-xl hover:shadow-primary/10 transition-all duration-300 border-2 hover:border-primary/20">
                        <CardHeader>
                            <div className="mb-3 flex h-12 w-12 sm:h-14 sm:w-14 items-center justify-center rounded-xl bg-gradient-to-br from-orange-500/10 to-orange-600/10 group-hover:from-orange-500/20 group-hover:to-orange-600/20 transition-all">
                                <UtensilsCrossed className="h-6 w-6 sm:h-7 sm:w-7 text-orange-600" />
                            </div>
                            <CardTitle className="text-lg sm:text-xl">Verified Food Places</CardTitle>
                            <CardDescription className="text-sm sm:text-base">
                                Browse admin-approved restaurants, cafes, and eateries near CDAC
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <ul className="space-y-2 sm:space-y-3 text-sm">
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-orange-600 flex-shrink-0" />
                                    Real reviews from students
                                </li>
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-orange-600 flex-shrink-0" />
                                    Detailed menus and pricing
                                </li>
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-orange-600 flex-shrink-0" />
                                    Location and contact info
                                </li>
                            </ul>
                        </CardContent>
                    </Card>

                    {/* Feature 2 */}
                    <Card className="group hover:shadow-xl hover:shadow-primary/10 transition-all duration-300 border-2 hover:border-primary/20">
                        <CardHeader>
                            <div className="mb-3 flex h-12 w-12 sm:h-14 sm:w-14 items-center justify-center rounded-xl bg-gradient-to-br from-purple-500/10 to-purple-600/10 group-hover:from-purple-500/20 group-hover:to-purple-600/20 transition-all">
                                <Home className="h-6 w-6 sm:h-7 sm:w-7 text-purple-600" />
                            </div>
                            <CardTitle className="text-lg sm:text-xl">Quality Hostels</CardTitle>
                            <CardDescription className="text-sm sm:text-base">
                                Find comfortable and affordable accommodation options
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <ul className="space-y-2 sm:space-y-3 text-sm">
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-purple-600 flex-shrink-0" />
                                    Verified hostel listings
                                </li>
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-purple-600 flex-shrink-0" />
                                    Amenities and facilities
                                </li>
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-purple-600 flex-shrink-0" />
                                    Distance from CDAC
                                </li>
                            </ul>
                        </CardContent>
                    </Card>

                    {/* Feature 3 */}
                    <Card className="group hover:shadow-xl hover:shadow-primary/10 transition-all duration-300 border-2 hover:border-primary/20">
                        <CardHeader>
                            <div className="mb-3 flex h-12 w-12 sm:h-14 sm:w-14 items-center justify-center rounded-xl bg-gradient-to-br from-blue-500/10 to-blue-600/10 group-hover:from-blue-500/20 group-hover:to-blue-600/20 transition-all">
                                <Users className="h-6 w-6 sm:h-7 sm:w-7 text-blue-600" />
                            </div>
                            <CardTitle className="text-lg sm:text-xl">Community Driven</CardTitle>
                            <CardDescription className="text-sm sm:text-base">
                                Share your experiences and help fellow students
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <ul className="space-y-2 sm:space-y-3 text-sm">
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-blue-600 flex-shrink-0" />
                                    Submit new places
                                </li>
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-blue-600 flex-shrink-0" />
                                    Rate and review
                                </li>
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-blue-600 flex-shrink-0" />
                                    Suggest improvements
                                </li>
                            </ul>
                        </CardContent>
                    </Card>

                    {/* Feature 4 */}
                    <Card className="group hover:shadow-xl hover:shadow-primary/10 transition-all duration-300 border-2 hover:border-primary/20">
                        <CardHeader>
                            <div className="mb-3 flex h-12 w-12 sm:h-14 sm:w-14 items-center justify-center rounded-xl bg-gradient-to-br from-green-500/10 to-green-600/10 group-hover:from-green-500/20 group-hover:to-green-600/20 transition-all">
                                <Shield className="h-6 w-6 sm:h-7 sm:w-7 text-green-600" />
                            </div>
                            <CardTitle className="text-lg sm:text-xl">Admin Verified</CardTitle>
                            <CardDescription className="text-sm sm:text-base">
                                All listings are reviewed and approved by admins
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <ul className="space-y-2 sm:space-y-3 text-sm">
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-green-600 flex-shrink-0" />
                                    Quality assurance
                                </li>
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-green-600 flex-shrink-0" />
                                    Spam prevention
                                </li>
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-green-600 flex-shrink-0" />
                                    Accurate information
                                </li>
                            </ul>
                        </CardContent>
                    </Card>

                    {/* Feature 5 */}
                    <Card className="group hover:shadow-xl hover:shadow-primary/10 transition-all duration-300 border-2 hover:border-primary/20">
                        <CardHeader>
                            <div className="mb-3 flex h-12 w-12 sm:h-14 sm:w-14 items-center justify-center rounded-xl bg-gradient-to-br from-yellow-500/10 to-yellow-600/10 group-hover:from-yellow-500/20 group-hover:to-yellow-600/20 transition-all">
                                <Star className="h-6 w-6 sm:h-7 sm:w-7 text-yellow-600" />
                            </div>
                            <CardTitle className="text-lg sm:text-xl">Ratings & Reviews</CardTitle>
                            <CardDescription className="text-sm sm:text-base">
                                Make informed decisions based on real feedback
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <ul className="space-y-2 sm:space-y-3 text-sm">
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-yellow-600 flex-shrink-0" />
                                    Honest reviews
                                </li>
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-yellow-600 flex-shrink-0" />
                                    Star ratings
                                </li>
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-yellow-600 flex-shrink-0" />
                                    Photo uploads
                                </li>
                            </ul>
                        </CardContent>
                    </Card>

                    {/* Feature 6 */}
                    <Card className="group hover:shadow-xl hover:shadow-primary/10 transition-all duration-300 border-2 hover:border-primary/20">
                        <CardHeader>
                            <div className="mb-3 flex h-12 w-12 sm:h-14 sm:w-14 items-center justify-center rounded-xl bg-gradient-to-br from-red-500/10 to-red-600/10 group-hover:from-red-500/20 group-hover:to-red-600/20 transition-all">
                                <MapPin className="h-6 w-6 sm:h-7 sm:w-7 text-red-600" />
                            </div>
                            <CardTitle className="text-lg sm:text-xl">Location Based</CardTitle>
                            <CardDescription className="text-sm sm:text-base">
                                Find places near you or near CDAC campus
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <ul className="space-y-2 sm:space-y-3 text-sm">
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-red-600 flex-shrink-0" />
                                    Distance information
                                </li>
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-red-600 flex-shrink-0" />
                                    Locality details
                                </li>
                                <li className="flex items-center gap-2 text-muted-foreground">
                                    <CheckCircle2 className="h-4 w-4 text-red-600 flex-shrink-0" />
                                    Easy navigation
                                </li>
                            </ul>
                        </CardContent>
                    </Card>
                </div>
            </section>

            {/* CTA Section */}
            <section className="container mx-auto px-4 sm:px-6 py-12 sm:py-16 lg:py-24">
                <Card className="relative overflow-hidden border-2 border-primary/20 bg-gradient-to-br from-primary/10 via-primary/5 to-background shadow-lg">
                    <div className="absolute inset-0 bg-grid-white/5 [mask-image:linear-gradient(0deg,transparent,black)]" />
                    <CardContent className="relative flex flex-col items-center justify-between gap-6 sm:gap-8 p-8 sm:p-12 lg:p-16 md:flex-row">
                        <div className="text-center md:text-left space-y-3 sm:space-y-4">
                            <h3 className="text-2xl sm:text-3xl lg:text-4xl font-bold">Ready to get started?</h3>
                            <p className="text-sm sm:text-base lg:text-lg text-muted-foreground max-w-xl">
                                Join the CDAC Connect community today and discover the best places around you.
                                Start exploring verified listings and share your experiences!
                            </p>
                        </div>
                        <div className="flex flex-col sm:flex-row gap-3 sm:gap-4 w-full md:w-auto">
                            <Link to="/signup" className="w-full sm:w-auto">
                                <Button size="lg" className="w-full sm:w-auto shadow-xl shadow-primary/25">
                                    Create Account
                                </Button>
                            </Link>
                            <Link to="/login" className="w-full sm:w-auto">
                                <Button size="lg" variant="outline" className="w-full sm:w-auto border-2">
                                    Login
                                </Button>
                            </Link>
                        </div>
                    </CardContent>
                </Card>
            </section>

            {/* Footer */}
            <footer className="border-t bg-background/50 backdrop-blur-sm">
                <div className="container mx-auto px-4 sm:px-6 py-8 sm:py-12">
                    <div className="flex flex-col items-center justify-between gap-6 sm:gap-8 md:flex-row">
                        <div className="flex items-center gap-2 sm:gap-3">
                            <div className="flex h-8 w-8 sm:h-10 sm:w-10 items-center justify-center rounded-xl bg-gradient-to-br from-primary to-primary/60 shadow-lg shadow-primary/20">
                                <UtensilsCrossed className="h-4 w-4 sm:h-5 sm:w-5 text-primary-foreground" />
                            </div>
                            <span className="text-base sm:text-lg font-semibold">CDAC Connect</span>
                        </div>
                        <p className="text-xs sm:text-sm text-muted-foreground text-center">
                            © 2026 CDAC Connect. All rights reserved.
                        </p>
                        <div className="flex gap-4 sm:gap-6">
                            <Link to="/login" className="text-xs sm:text-sm text-muted-foreground hover:text-primary transition-colors">
                                Login
                            </Link>
                            <Link to="/signup" className="text-xs sm:text-sm text-muted-foreground hover:text-primary transition-colors">
                                Sign Up
                            </Link>
                        </div>
                    </div>
                </div>
            </footer>
        </div>
    );
}
