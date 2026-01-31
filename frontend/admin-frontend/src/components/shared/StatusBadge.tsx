import { Badge } from '@/components/ui/badge';
import type { ApprovalStatus } from '@/types';

interface StatusBadgeProps {
    status: ApprovalStatus;
    className?: string;
}

/**
 * Status badge component
 * Displays approval status with color coding
 */
export function StatusBadge({ status, className }: StatusBadgeProps) {
    const variants = {
        PENDING: 'bg-yellow-100 text-yellow-800 hover:bg-yellow-100',
        APPROVED: 'bg-green-100 text-green-800 hover:bg-green-100',
        REJECTED: 'bg-red-100 text-red-800 hover:bg-red-100',
    };

    return (
        <Badge variant="secondary" className={`${variants[status]} ${className || ''}`}>
            {status}
        </Badge>
    );
}
