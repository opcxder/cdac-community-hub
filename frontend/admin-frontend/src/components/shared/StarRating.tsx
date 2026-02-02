import { Star } from 'lucide-react';
import { useState } from 'react';

interface StarRatingProps {
    value: number;
    onChange?: (rating: number) => void;
    readonly?: boolean;
    size?: 'sm' | 'md' | 'lg';
    showValue?: boolean;
}

/**
 * Star Rating Component
 * Interactive star rating input or read-only display
 */
export function StarRating({
    value,
    onChange,
    readonly = false,
    size = 'md',
    showValue = false
}: StarRatingProps) {
    const [hoverValue, setHoverValue] = useState<number | null>(null);

    const sizeClasses = {
        sm: 'h-4 w-4',
        md: 'h-5 w-5',
        lg: 'h-6 w-6'
    };

    const handleClick = (rating: number) => {
        if (!readonly && onChange) {
            onChange(rating);
        }
    };

    const displayValue = hoverValue ?? value;

    return (
        <div className="flex items-center gap-1">
            {[1, 2, 3, 4, 5].map((star) => (
                <button
                    key={star}
                    type="button"
                    disabled={readonly}
                    onClick={() => handleClick(star)}
                    onMouseEnter={() => !readonly && setHoverValue(star)}
                    onMouseLeave={() => !readonly && setHoverValue(null)}
                    className={`${readonly ? 'cursor-default' : 'cursor-pointer hover:scale-110'} transition-transform`}
                >
                    <Star
                        className={`${sizeClasses[size]} ${star <= displayValue
                                ? 'fill-yellow-400 text-yellow-400'
                                : 'fill-none text-gray-300'
                            } transition-colors`}
                    />
                </button>
            ))}
            {showValue && (
                <span className="ml-2 text-sm font-medium text-muted-foreground">
                    {value.toFixed(1)}
                </span>
            )}
        </div>
    );
}
