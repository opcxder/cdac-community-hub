import { useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Label } from '@/components/ui/label';
import { Input } from '@/components/ui/input';
import { Checkbox } from '@/components/ui/checkbox';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { X, Search } from 'lucide-react';
import type { FoodFilters, FoodCategory, PriceRange } from '@/types';

interface FoodFilterPanelProps {
    filters: FoodFilters;
    categories: FoodCategory[];
    onFiltersChange: (filters: FoodFilters) => void;
    onReset: () => void;
}

/**
 * Food filter panel component
 * Provides filtering options for food places
 */
export function FoodFilterPanel({ filters, categories, onFiltersChange, onReset }: FoodFilterPanelProps) {
    const [localFilters, setLocalFilters] = useState<FoodFilters>(filters);

    const priceRanges: PriceRange[] = ['BUDGET', 'MODERATE', 'EXPENSIVE'];

    const handleSearchChange = (value: string) => {
        const newFilters = { ...localFilters, search: value || undefined };
        setLocalFilters(newFilters);
        onFiltersChange(newFilters);
    };

    const handlePriceRangeToggle = (range: PriceRange) => {
        const current = localFilters.priceRange || [];
        const newRanges = current.includes(range)
            ? current.filter((r) => r !== range)
            : [...current, range];

        const newFilters = { ...localFilters, priceRange: newRanges.length > 0 ? newRanges : undefined };
        setLocalFilters(newFilters);
        onFiltersChange(newFilters);
    };

    const handleCategoryToggle = (categoryId: number) => {
        const current = localFilters.categoryIds || [];
        const newCategories = current.includes(categoryId)
            ? current.filter((id) => id !== categoryId)
            : [...current, categoryId];

        const newFilters = { ...localFilters, categoryIds: newCategories.length > 0 ? newCategories : undefined };
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
        (localFilters.priceRange?.length || 0) +
        (localFilters.categoryIds?.length || 0) +
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
                            placeholder="Search food places..."
                            value={localFilters.search || ''}
                            onChange={(e) => handleSearchChange(e.target.value)}
                            className="pl-8"
                        />
                    </div>
                </div>

                {/* Price Range */}
                <div className="space-y-2">
                    <Label>Price Range</Label>
                    <div className="space-y-2">
                        {priceRanges.map((range) => (
                            <div key={range} className="flex items-center space-x-2">
                                <Checkbox
                                    id={`price-${range}`}
                                    checked={localFilters.priceRange?.includes(range) || false}
                                    onCheckedChange={() => handlePriceRangeToggle(range)}
                                />
                                <label htmlFor={`price-${range}`} className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
                                    {range}
                                </label>
                            </div>
                        ))}
                    </div>
                </div>

                {/* Categories */}
                {categories.length > 0 && (
                    <div className="space-y-2">
                        <Label>Categories</Label>
                        <div className="max-h-48 space-y-2 overflow-y-auto">
                            {categories.map((category) => (
                                <div key={category.categoryId} className="flex items-center space-x-2">
                                    <Checkbox
                                        id={`category-${category.categoryId}`}
                                        checked={localFilters.categoryIds?.includes(category.categoryId) || false}
                                        onCheckedChange={() => handleCategoryToggle(category.categoryId)}
                                    />
                                    <label
                                        htmlFor={`category-${category.categoryId}`}
                                        className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                    >
                                        {category.categoryName}
                                    </label>
                                </div>
                            ))}
                        </div>
                    </div>
                )}

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
                            {localFilters.priceRange?.map((range) => (
                                <Badge key={range} variant="secondary" className="text-xs">
                                    {range}
                                </Badge>
                            ))}
                            {localFilters.categoryIds?.map((id) => {
                                const category = categories.find((c) => c.categoryId === id);
                                return category ? (
                                    <Badge key={id} variant="secondary" className="text-xs">
                                        {category.categoryName}
                                    </Badge>
                                ) : null;
                            })}
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
