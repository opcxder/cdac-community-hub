import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import client from "@/api/client";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { Checkbox } from "@/components/ui/checkbox";
import ImageUpload from "@/components/form/ImageUpload";
import { toast } from "sonner";

const hostelSchema = z.object({
    name: z.string().min(3, "Name must be at least 3 characters"),
    description: z.string().optional(),
    address: z.string().min(5, "Address is required"),
    city: z.string().min(2, "City is required"),
    locality: z.string().min(2, "Locality is required"),
    distanceFromCDAC: z.string().min(1, "Distance is required"),
    monthlyRentMin: z.number({ required_error: "Minimum rent is required" }).min(0),
    monthlyRentMax: z.number({ required_error: "Maximum rent is required" }).min(0),
    contactPersonName: z.string().min(2, "Contact person name is required"),
    contactPhone: z.string().min(10, "Valid phone number is required"),
    forGender: z.enum(["BOYS", "GIRLS", "BOTH"]),
    categoryId: z.number({ required_error: "Please select a category" })
});

type HostelFormData = z.infer<typeof hostelSchema>;

interface Category {
    categoryId: number;
    categoryName: string;
}

const FACILITIES = ["WiFi", "AC", "Mess", "Laundry"];
const ROOM_TYPES = ["Single", "Double", "Triple"];

export default function SubmitHostelPage() {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [categories, setCategories] = useState<Category[]>([]);
    const [images, setImages] = useState<File[]>([]);
    const [imageError, setImageError] = useState<string>("");
    const [selectedFacilities, setSelectedFacilities] = useState<string[]>([]);
    const [selectedRoomTypes, setSelectedRoomTypes] = useState<string[]>([]);

    const { register, handleSubmit, formState: { errors }, setValue } = useForm<HostelFormData>({
        resolver: zodResolver(hostelSchema)
    });

    useEffect(() => {
        async function fetchCategories() {
            try {
                const response = await client.get<Category[]>("/api/hostel/categories");
                setCategories(response.data);
            } catch (err) {
                console.error("Failed to load categories:", err);
            }
        }
        fetchCategories();
    }, []);

    const toggleFacility = (facility: string) => {
        setSelectedFacilities(prev =>
            prev.includes(facility) ? prev.filter(f => f !== facility) : [...prev, facility]
        );
    };

    const toggleRoomType = (roomType: string) => {
        setSelectedRoomTypes(prev =>
            prev.includes(roomType) ? prev.filter(r => r !== roomType) : [...prev, roomType]
        );
    };

    const onSubmit = async (data: HostelFormData) => {
        if (images.length === 0) {
            setImageError("Please upload at least 1 image");
            return;
        }
        setImageError("");

        setLoading(true);
        setError(null);

        try {
            // Step 1: Create hostel
            const hostelResponse = await client.post("/api/hostel/hostels", {
                name: data.name,
                description: data.description || "",
                address: data.address,
                city: data.city,
                locality: data.locality,
                distanceFromCDAC: data.distanceFromCDAC,
                monthlyRentMin: data.monthlyRentMin,
                monthlyRentMax: data.monthlyRentMax,
                contactPersonName: data.contactPersonName,
                contactPhone: data.contactPhone,
                forGender: data.forGender,
                categoryId: data.categoryId,
                facilities: selectedFacilities,
                roomTypes: selectedRoomTypes
            });

            const hostelId = hostelResponse.data.hostelId;

            // Step 2: Upload images
            const formData = new FormData();
            images.forEach(image => formData.append('images', image));

            await client.post(`/api/hostel/hostels/${hostelId}/images`, formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });

            toast.success("Hostel submitted for approval!");
            navigate("/dashboard");
        } catch (err: any) {
            const message = err?.response?.data?.message || "Failed to submit hostel";
            setError(message);
            toast.error(message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-background py-8">
            <div className="container mx-auto px-4 max-w-3xl">
                <Card>
                    <CardHeader>
                        <CardTitle className="text-2xl">Submit Hostel</CardTitle>
                        <CardDescription>
                            Share accommodation information with the CDAC community
                        </CardDescription>
                    </CardHeader>

                    <CardContent>
                        <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                            {error && (
                                <Alert variant="destructive">
                                    <AlertDescription>{error}</AlertDescription>
                                </Alert>
                            )}

                            <div className="space-y-2">
                                <Label htmlFor="name">Hostel Name *</Label>
                                <Input
                                    id="name"
                                    placeholder="e.g., CDAC Boys Hostel"
                                    {...register("name")}
                                    disabled={loading}
                                />
                                {errors.name && (
                                    <p className="text-sm text-red-600">{errors.name.message}</p>
                                )}
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="description">Description</Label>
                                <Textarea
                                    id="description"
                                    placeholder="Tell us about this hostel..."
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
                                    <Label htmlFor="distanceFromCDAC">Distance from CDAC *</Label>
                                    <Input
                                        id="distanceFromCDAC"
                                        placeholder="e.g., 2 km"
                                        {...register("distanceFromCDAC")}
                                        disabled={loading}
                                    />
                                    {errors.distanceFromCDAC && (
                                        <p className="text-sm text-red-600">{errors.distanceFromCDAC.message}</p>
                                    )}
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <Label htmlFor="monthlyRentMin">Monthly Rent Min (₹) *</Label>
                                    <Input
                                        id="monthlyRentMin"
                                        type="number"
                                        placeholder="8000"
                                        {...register("monthlyRentMin", { valueAsNumber: true })}
                                        disabled={loading}
                                    />
                                    {errors.monthlyRentMin && (
                                        <p className="text-sm text-red-600">{errors.monthlyRentMin.message}</p>
                                    )}
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="monthlyRentMax">Monthly Rent Max (₹) *</Label>
                                    <Input
                                        id="monthlyRentMax"
                                        type="number"
                                        placeholder="12000"
                                        {...register("monthlyRentMax", { valueAsNumber: true })}
                                        disabled={loading}
                                    />
                                    {errors.monthlyRentMax && (
                                        <p className="text-sm text-red-600">{errors.monthlyRentMax.message}</p>
                                    )}
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <Label htmlFor="contactPersonName">Contact Person *</Label>
                                    <Input
                                        id="contactPersonName"
                                        placeholder="Full name"
                                        {...register("contactPersonName")}
                                        disabled={loading}
                                    />
                                    {errors.contactPersonName && (
                                        <p className="text-sm text-red-600">{errors.contactPersonName.message}</p>
                                    )}
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="contactPhone">Contact Phone *</Label>
                                    <Input
                                        id="contactPhone"
                                        placeholder="+91 1234567890"
                                        {...register("contactPhone")}
                                        disabled={loading}
                                    />
                                    {errors.contactPhone && (
                                        <p className="text-sm text-red-600">{errors.contactPhone.message}</p>
                                    )}
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <Label htmlFor="forGender">For Gender *</Label>
                                    <Select
                                        onValueChange={(value) => setValue("forGender", value as any)}
                                        disabled={loading}
                                    >
                                        <SelectTrigger>
                                            <SelectValue placeholder="Select gender" />
                                        </SelectTrigger>
                                        <SelectContent>
                                            <SelectItem value="BOYS">Boys Only</SelectItem>
                                            <SelectItem value="GIRLS">Girls Only</SelectItem>
                                            <SelectItem value="BOTH">Both</SelectItem>
                                        </SelectContent>
                                    </Select>
                                    {errors.forGender && (
                                        <p className="text-sm text-red-600">{errors.forGender.message}</p>
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

                            <div className="space-y-2">
                                <Label>Facilities</Label>
                                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                                    {FACILITIES.map((facility) => (
                                        <div key={facility} className="flex items-center space-x-2">
                                            <Checkbox
                                                id={facility}
                                                checked={selectedFacilities.includes(facility)}
                                                onCheckedChange={() => toggleFacility(facility)}
                                                disabled={loading}
                                            />
                                            <label
                                                htmlFor={facility}
                                                className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                            >
                                                {facility}
                                            </label>
                                        </div>
                                    ))}
                                </div>
                            </div>

                            <div className="space-y-2">
                                <Label>Room Types</Label>
                                <div className="grid grid-cols-3 gap-4">
                                    {ROOM_TYPES.map((roomType) => (
                                        <div key={roomType} className="flex items-center space-x-2">
                                            <Checkbox
                                                id={roomType}
                                                checked={selectedRoomTypes.includes(roomType)}
                                                onCheckedChange={() => toggleRoomType(roomType)}
                                                disabled={loading}
                                            />
                                            <label
                                                htmlFor={roomType}
                                                className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                            >
                                                {roomType}
                                            </label>
                                        </div>
                                    ))}
                                </div>
                            </div>

                            <ImageUpload
                                images={images}
                                onChange={setImages}
                                maxImages={5}
                                error={imageError}
                            />

                            <div className="flex gap-4">
                                <Button
                                    type="button"
                                    variant="outline"
                                    onClick={() => navigate("/dashboard")}
                                    disabled={loading}
                                    className="flex-1"
                                >
                                    Cancel
                                </Button>
                                <Button
                                    type="submit"
                                    disabled={loading}
                                    className="flex-1"
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
