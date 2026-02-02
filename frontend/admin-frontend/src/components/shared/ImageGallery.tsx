import { useState } from 'react';
import { ChevronLeft, ChevronRight, X } from 'lucide-react';
import { Button } from '@/components/ui/button';

interface ImageGalleryProps {
    images: string[];
    altText: string;
}

/**
 * Image Gallery Component
 * Displays a main image with thumbnail navigation
 */
export function ImageGallery({ images, altText }: ImageGalleryProps) {
    const [selectedIndex, setSelectedIndex] = useState(0);
    const [isFullscreen, setIsFullscreen] = useState(false);

    if (!images || images.length === 0) {
        return (
            <div className="aspect-video w-full bg-muted rounded-xl flex items-center justify-center">
                <p className="text-muted-foreground">No images available</p>
            </div>
        );
    }

    const handlePrevious = () => {
        setSelectedIndex((prev) => (prev === 0 ? images.length - 1 : prev - 1));
    };

    const handleNext = () => {
        setSelectedIndex((prev) => (prev === images.length - 1 ? 0 : prev + 1));
    };

    return (
        <>
            {/* Main Image */}
            <div className="relative group">
                <div className="aspect-video w-full overflow-hidden rounded-xl bg-muted">
                    <img
                        src={images[selectedIndex]}
                        alt={`${altText} - Image ${selectedIndex + 1}`}
                        className="h-full w-full object-contain cursor-pointer transition-transform duration-300 hover:scale-105 bg-gray-50"
                        onClick={() => setIsFullscreen(true)}
                    />
                </div>

                {/* Navigation Arrows (only show if multiple images) */}
                {images.length > 1 && (
                    <>
                        <Button
                            variant="secondary"
                            size="icon"
                            className="absolute left-2 top-1/2 -translate-y-1/2 opacity-0 group-hover:opacity-100 transition-opacity"
                            onClick={handlePrevious}
                        >
                            <ChevronLeft className="h-4 w-4" />
                        </Button>
                        <Button
                            variant="secondary"
                            size="icon"
                            className="absolute right-2 top-1/2 -translate-y-1/2 opacity-0 group-hover:opacity-100 transition-opacity"
                            onClick={handleNext}
                        >
                            <ChevronRight className="h-4 w-4" />
                        </Button>
                    </>
                )}

                {/* Image Counter */}
                {images.length > 1 && (
                    <div className="absolute bottom-4 right-4 bg-black/60 text-white px-3 py-1 rounded-full text-sm">
                        {selectedIndex + 1} / {images.length}
                    </div>
                )}
            </div>

            {/* Thumbnails (only show if multiple images) */}
            {images.length > 1 && (
                <div className="grid grid-cols-4 sm:grid-cols-6 md:grid-cols-8 gap-2 mt-3">
                    {images.map((image, index) => (
                        <button
                            key={index}
                            onClick={() => setSelectedIndex(index)}
                            className={`aspect-square overflow-hidden rounded-lg border-2 transition-all ${index === selectedIndex
                                ? 'border-primary ring-2 ring-primary/20'
                                : 'border-transparent hover:border-primary/50'
                                }`}
                        >
                            <img
                                src={image}
                                alt={`${altText} - Thumbnail ${index + 1}`}
                                className="h-full w-full object-cover"
                            />
                        </button>
                    ))}
                </div>
            )}

            {/* Fullscreen Modal */}
            {isFullscreen && (
                <div className="fixed inset-0 z-50 bg-black/95 flex items-center justify-center p-4">
                    <Button
                        variant="ghost"
                        size="icon"
                        className="absolute top-4 right-4 text-white hover:bg-white/20"
                        onClick={() => setIsFullscreen(false)}
                    >
                        <X className="h-6 w-6" />
                    </Button>

                    <div className="relative max-w-7xl max-h-full">
                        <img
                            src={images[selectedIndex]}
                            alt={`${altText} - Fullscreen`}
                            className="max-h-[90vh] max-w-full object-contain"
                        />

                        {images.length > 1 && (
                            <>
                                <Button
                                    variant="secondary"
                                    size="icon"
                                    className="absolute left-4 top-1/2 -translate-y-1/2"
                                    onClick={handlePrevious}
                                >
                                    <ChevronLeft className="h-6 w-6" />
                                </Button>
                                <Button
                                    variant="secondary"
                                    size="icon"
                                    className="absolute right-4 top-1/2 -translate-y-1/2"
                                    onClick={handleNext}
                                >
                                    <ChevronRight className="h-6 w-6" />
                                </Button>
                            </>
                        )}
                    </div>
                </div>
            )}
        </>
    );
}
