import { Star } from 'lucide-react';
import { useState } from 'react';

interface MultiCriteriaRatingProps {
    value: {
        cleanliness: number;
        foodQuality: number;
        safety: number;
        location: number;
        affordability: number;
    };
    onChange?: (ratings: {
        cleanliness: number;
        foodQuality: number;
        safety: number;
        location: number;
        affordability: number;
    }) => void;
    disabled?: boolean;
    readonly?: boolean;
}

const criteria = [
    { key: 'cleanliness' as const, label: 'Cleanliness', icon: '🧹' },
    { key: 'foodQuality' as const, label: 'Food Quality', icon: '🍽️' },
    { key: 'safety' as const, label: 'Safety', icon: '🔒' },
    { key: 'location' as const, label: 'Location', icon: '📍' },
    { key: 'affordability' as const, label: 'Affordability', icon: '💰' },
];

/**
 * Multi-Criteria Rating Component
 * Allows users to rate hostels on 5 different criteria
 */
export function MultiCriteriaRating({ value, onChange, disabled = false, readonly = false }: MultiCriteriaRatingProps) {
    const [hoveredCriterion, setHoveredCriterion] = useState<string | null>(null);
    const [hoveredStar, setHoveredStar] = useState<number>(0);

    const handleStarClick = (criterion: keyof typeof value, rating: number) => {
        if (disabled || readonly || !onChange) return;
        onChange({ ...value, [criterion]: rating });
    };

    const handleStarHover = (criterion: string, star: number) => {
        if (disabled || readonly) return;
        setHoveredCriterion(criterion);
        setHoveredStar(star);
    };

    const handleMouseLeave = () => {
        setHoveredCriterion(null);
        setHoveredStar(0);
    };

    return (
        <div className="space-y-4">
            {criteria.map(({ key, label, icon }) => {
                const currentRating = value[key] || 0;
                const displayRating = hoveredCriterion === key && hoveredStar > 0 ? hoveredStar : currentRating;

                return (
                    <div key={key} className="space-y-2">
                        <div className="flex items-center justify-between">
                            <span className="text-sm font-medium flex items-center gap-2">
                                <span className="text-lg">{icon}</span>
                                {label}
                            </span>
                            <span className="text-sm text-muted-foreground">
                                {currentRating > 0 ? `${currentRating}/5` : 'Not rated'}
                            </span>
                        </div>
                        <div
                            className="flex gap-1"
                            onMouseLeave={handleMouseLeave}
                        >
                            {[1, 2, 3, 4, 5].map((star) => {
                                const isFilled = star <= displayRating;
                                const isHovered = hoveredCriterion === key && star <= hoveredStar;

                                return (
                                    <button
                                        key={star}
                                        type="button"
                                        disabled={disabled || readonly}
                                        onClick={() => handleStarClick(key, star)}
                                        onMouseEnter={() => handleStarHover(key, star)}
                                        className={`transition-all duration-150 ${disabled || readonly ? 'cursor-default' : 'cursor-pointer hover:scale-110'
                                            }`}
                                    >
                                        <Star
                                            className={`h-6 w-6 transition-colors ${isFilled
                                                ? isHovered
                                                    ? 'fill-yellow-500 text-yellow-500'
                                                    : 'fill-yellow-400 text-yellow-400'
                                                : isHovered
                                                    ? 'fill-yellow-200 text-yellow-200'
                                                    : 'fill-none text-gray-300'
                                                }`}
                                        />
                                    </button>
                                );
                            })}
                        </div>
                    </div>
                );
            })}

            {/* Overall Average Display */}
            {Object.values(value).some(v => v > 0) && (
                <div className="pt-4 border-t">
                    <div className="flex items-center justify-between">
                        <span className="text-sm font-semibold">Overall Average</span>
                        <span className="text-lg font-bold text-primary">
                            {(Object.values(value).reduce((a, b) => a + b, 0) / Object.values(value).filter(v => v > 0).length).toFixed(1)}/5
                        </span>
                    </div>
                </div>
            )}
        </div>
    );
}
