import { FileQuestion } from 'lucide-react';

interface EmptyStateProps {
    icon?: React.ReactNode;
    title: string;
    description?: string;
    action?: React.ReactNode;
}

/**
 * Empty state component
 * Displays when no data is available
 */
export function EmptyState({ icon, title, description, action }: EmptyStateProps) {
    return (
        <div className="flex flex-col items-center justify-center py-12 text-center">
            <div className="mb-4 text-muted-foreground">
                {icon || <FileQuestion className="h-12 w-12" />}
            </div>
            <h3 className="mb-2 text-lg font-semibold">{title}</h3>
            {description && <p className="mb-4 text-sm text-muted-foreground">{description}</p>}
            {action && <div>{action}</div>}
        </div>
    );
}
