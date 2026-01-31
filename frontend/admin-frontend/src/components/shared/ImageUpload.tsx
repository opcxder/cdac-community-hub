import { useState, useRef } from 'react';
import { Upload, X, Image as ImageIcon } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card } from '@/components/ui/card';

interface ImageUploadProps {
    maxImages?: number;
    onImagesChange: (files: File[]) => void;
    existingImages?: string[];
}

/**
 * Image upload component
 * Supports multiple image upload with preview
 */
export function ImageUpload({ maxImages = 5, onImagesChange, existingImages = [] }: ImageUploadProps) {
    const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
    const [previewUrls, setPreviewUrls] = useState<string[]>([]);
    const fileInputRef = useRef<HTMLInputElement>(null);

    const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
        const files = Array.from(event.target.files || []);
        const remainingSlots = maxImages - selectedFiles.length - existingImages.length;
        const filesToAdd = files.slice(0, remainingSlots);

        if (filesToAdd.length > 0) {
            const newFiles = [...selectedFiles, ...filesToAdd];
            setSelectedFiles(newFiles);
            onImagesChange(newFiles);

            // Create preview URLs
            const newUrls = filesToAdd.map((file) => URL.createObjectURL(file));
            setPreviewUrls([...previewUrls, ...newUrls]);
        }
    };

    const handleRemove = (index: number) => {
        const newFiles = selectedFiles.filter((_, i) => i !== index);
        const newUrls = previewUrls.filter((_, i) => i !== index);

        // Revoke URL to prevent memory leak
        URL.revokeObjectURL(previewUrls[index]);

        setSelectedFiles(newFiles);
        setPreviewUrls(newUrls);
        onImagesChange(newFiles);
    };

    const totalImages = existingImages.length + selectedFiles.length;
    const canAddMore = totalImages < maxImages;

    return (
        <div className="space-y-4">
            {/* Upload Button */}
            {canAddMore && (
                <div>
                    <input
                        ref={fileInputRef}
                        type="file"
                        accept="image/*"
                        multiple
                        onChange={handleFileSelect}
                        className="hidden"
                    />
                    <Button
                        type="button"
                        variant="outline"
                        onClick={() => fileInputRef.current?.click()}
                        className="w-full"
                    >
                        <Upload className="mr-2 h-4 w-4" />
                        Upload Images ({totalImages}/{maxImages})
                    </Button>
                    <p className="mt-1 text-xs text-muted-foreground">
                        {selectedFiles.length === 0 ? 'At least 1 image required' : `${maxImages - totalImages} more allowed`}
                    </p>
                </div>
            )}

            {/* Image Previews */}
            {(existingImages.length > 0 || previewUrls.length > 0) && (
                <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4">
                    {/* Existing Images */}
                    {existingImages.map((url, index) => (
                        <Card key={`existing-${index}`} className="relative overflow-hidden">
                            <img src={url} alt={`Existing ${index + 1}`} className="h-32 w-full object-cover" />
                            <div className="absolute right-1 top-1">
                                <div className="rounded-full bg-green-500 px-2 py-1 text-xs text-white">
                                    Uploaded
                                </div>
                            </div>
                        </Card>
                    ))}

                    {/* New Image Previews */}
                    {previewUrls.map((url, index) => (
                        <Card key={`preview-${index}`} className="group relative overflow-hidden">
                            <img src={url} alt={`Preview ${index + 1}`} className="h-32 w-full object-cover" />
                            <Button
                                type="button"
                                variant="destructive"
                                size="icon"
                                className="absolute right-1 top-1 h-6 w-6 opacity-0 transition-opacity group-hover:opacity-100"
                                onClick={() => handleRemove(index)}
                            >
                                <X className="h-4 w-4" />
                            </Button>
                        </Card>
                    ))}
                </div>
            )}

            {/* Empty State */}
            {existingImages.length === 0 && selectedFiles.length === 0 && (
                <Card className="flex flex-col items-center justify-center border-dashed py-8">
                    <ImageIcon className="mb-2 h-8 w-8 text-muted-foreground" />
                    <p className="text-sm text-muted-foreground">No images selected</p>
                </Card>
            )}
        </div>
    );
}
