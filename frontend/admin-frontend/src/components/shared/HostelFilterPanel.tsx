import { useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Label } from '@/components/ui/label';
import { Input } from '@/components/ui/input';
import { Checkbox } from '@/components/ui/checkbox';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { X, Search } from 'lucide-react';
import type { HostelFilters } from '@/types';

interface HostelFilterPanelProps {
    filters: HostelFilters;
    onFiltersChange: (filters: HostelFilters) => void;
    onReset: () => void;
}

/**
 * Hostel filter panel component
 * Provides filtering options for hostels
 */
export function HostelFilterPanel({ filters, onFiltersChange, onReset }: HostelFilterPanelProps) {
    const [localFilters, setLocalFilters] = useState<HostelFilters>(filters);

    const handleSearchChange = (value: string) => {
        const newFilters = { ...localFilters, search: value || undefined };
        setLocalFilters(newFilters);
        onFiltersChange(newFilters);
    };

    const handleRentMinChange = (value: string) => {
        const newFilters = { ...localFilters, rentMin: value ? parseInt(value) : undefined };
        setLocalFilters(newFilters);
        onFiltersChange(newFilters);
    };

    const handleRentMaxChange = (value: string) => {
        const newFilters = { ...localFilters, rentMax: value ? parseInt(value) : undefined };
        setLocalFilters(newFilters);
        onFiltersChange(newFilters);
    };

    const handleAmenityToggle = (amenity: keyof HostelFilters) => {
        const newFilters = { ...localFilters, [amenity]: !localFilters[amenity] };
        setLocalFilters(newFilters);
        onFiltersChange(newFilters);
    };

    const handleCityChange = (value: string) => {
        const newFilters = { ...localFilters, city: value || undefined };
        setLocalFilters(newFilters);
        onFiltersChange(newFilters);
    };

    const activeFilterCount =
        (localFilters.search ? 1 : 0) +
        (localFilters.rentMin ? 1 : 0) +
        (localFilters.rentMax ? 1 : 0) +
        (localFilters.hasWifi ? 1 : 0) +
        (localFilters.hasAc ? 1 : 0) +
        (localFilters.hasMess ? 1 : 0) +
        (localFilters.hasLaundry ? 1 : 0) +
        (localFilters.city ? 1 : 0);

    return (
        <Card>
            <CardHeader>
                <div className="flex items-center justify-between">
                    <CardTitle className="text-lg">Filters</CardTitle>
                    {activeFilterCount > 0 && (
                        <Button variant="ghost" size="sm" onClick={onReset}>
                            <X className="mr-1 h-4 w-4" />
                            Clear ({activeFilterCount})
                        </Button>
                    )}
                </div>
            </CardHeader>

            <CardContent className="space-y-4">
                {/* Search */}
                <div className="space-y-2">
                    <Label>Search</Label>
                    <div className="relative">
                        <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
                        <Input
                            placeholder="Search hostels..."
                            value={localFilters.search || ''}
                            onChange={(e) => handleSearchChange(e.target.value)}
                            className="pl-8"
                        />
                    </div>
                </div>

                {/* Rent Range */}
                <div className="space-y-2">
                    <Label>Monthly Rent (₹)</Label>
                    <div className="grid grid-cols-2 gap-2">
                        <Input
                            type="number"
                            placeholder="Min"
                            value={localFilters.rentMin || ''}
                            onChange={(e) => handleRentMinChange(e.target.value)}
                        />
                        <Input
                            type="number"
                            placeholder="Max"
                            value={localFilters.rentMax || ''}
                            onChange={(e) => handleRentMaxChange(e.target.value)}
                        />
                    </div>
                </div>

                {/* Amenities */}
                <div className="space-y-2">
                    <Label>Amenities</Label>
                    <div className="space-y-2">
                        <div className="flex items-center space-x-2">
                            <Checkbox
                                id="wifi"
                                checked={localFilters.hasWifi || false}
                                onCheckedChange={() => handleAmenityToggle('hasWifi')}
                            />
                            <label htmlFor="wifi" className="text-sm font-medium leading-none">
                                WiFi
                            </label>
                        </div>
                        <div className="flex items-center space-x-2">
                            <Checkbox
                                id="ac"
                                checked={localFilters.hasAc || false}
                                onCheckedChange={() => handleAmenityToggle('hasAc')}
                            />
                            <label htmlFor="ac" className="text-sm font-medium leading-none">
                                AC
                            </label>
                        </div>
                        <div className="flex items-center space-x-2">
                            <Checkbox
                                id="mess"
                                checked={localFilters.hasMess || false}
                                onCheckedChange={() => handleAmenityToggle('hasMess')}
                            />
                            <label htmlFor="mess" className="text-sm font-medium leading-none">
                                Mess
                            </label>
                        </div>
                        <div className="flex items-center space-x-2">
                            <Checkbox
                                id="laundry"
                                checked={localFilters.hasLaundry || false}
                                onCheckedChange={() => handleAmenityToggle('hasLaundry')}
                            />
                            <label htmlFor="laundry" className="text-sm font-medium leading-none">
                                Laundry
                            </label>
                        </div>
                    </div>
                </div>

                {/* City */}
                <div className="space-y-2">
                    <Label>City</Label>
                    <Input
                        placeholder="Enter city..."
                        value={localFilters.city || ''}
                        onChange={(e) => handleCityChange(e.target.value)}
                    />
                </div>

                {/* Active Filters Summary */}
                {activeFilterCount > 0 && (
                    <div className="space-y-2 border-t pt-4">
                        <Label className="text-xs text-muted-foreground">Active Filters</Label>
                        <div className="flex flex-wrap gap-1">
                            {localFilters.search && (
                                <Badge variant="secondary" className="text-xs">
                                    Search: {localFilters.search}
                                </Badge>
                            )}
                            {localFilters.rentMin && (
                                <Badge variant="secondary" className="text-xs">
                                    Min: ₹{localFilters.rentMin.toLocaleString()}
                                </Badge>
                            )}
                            {localFilters.rentMax && (
                                <Badge variant="secondary" className="text-xs">
                                    Max: ₹{localFilters.rentMax.toLocaleString()}
                                </Badge>
                            )}
                            {localFilters.hasWifi && <Badge variant="secondary" className="text-xs">WiFi</Badge>}
                            {localFilters.hasAc && <Badge variant="secondary" className="text-xs">AC</Badge>}
                            {localFilters.hasMess && <Badge variant="secondary" className="text-xs">Mess</Badge>}
                            {localFilters.hasLaundry && <Badge variant="secondary" className="text-xs">Laundry</Badge>}
                            {localFilters.city && (
                                <Badge variant="secondary" className="text-xs">
                                    City: {localFilters.city}
                                </Badge>
                            )}
                        </div>
                    </div>
                )}
            </CardContent>
        </Card>
    );
}
