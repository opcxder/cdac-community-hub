import { useCallback, useState } from "react";
import { Upload, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";

interface ImageUploadProps {
    images: File[];
    onChange: (images: File[]) => void;
    maxImages?: number;
    error?: string;
}

export default function ImageUpload({
    images,
    onChange,
    maxImages = 5,
    error
}: ImageUploadProps) {
    const [dragActive, setDragActive] = useState(false);

    const handleDrag = useCallback((e: React.DragEvent) => {
        e.preventDefault();
        e.stopPropagation();
        if (e.type === "dragenter" || e.type === "dragover") {
            setDragActive(true);
        } else if (e.type === "dragleave") {
            setDragActive(false);
        }
    }, []);

    const handleDrop = useCallback((e: React.DragEvent) => {
        e.preventDefault();
        e.stopPropagation();
        setDragActive(false);

        if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
            handleFiles(Array.from(e.dataTransfer.files));
        }
    }, [images, maxImages]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        e.preventDefault();
        if (e.target.files && e.target.files.length > 0) {
            handleFiles(Array.from(e.target.files));
        }
    };

    const handleFiles = (newFiles: File[]) => {
        // Filter for images only
        const imageFiles = newFiles.filter(file => file.type.startsWith('image/'));

        // Check file sizes (max 5MB per file)
        const validFiles = imageFiles.filter(file => {
            if (file.size > 5 * 1024 * 1024) {
                alert(`${file.name} is too large. Maximum size is 5MB.`);
                return false;
            }
            return true;
        });

        // Combine with existing images, respecting max limit
        const combined = [...images, ...validFiles].slice(0, maxImages);
        onChange(combined);
    };

    const removeImage = (index: number) => {
        const newImages = images.filter((_, i) => i !== index);
        onChange(newImages);
    };

    return (
        <div className="space-y-4">
            <div>
                <Label>Images (1-{maxImages} required)</Label>
                <p className="text-sm text-muted-foreground mb-2">
                    Upload up to {maxImages} images. Max 5MB per image.
                </p>
            </div>

            {/* Upload Area */}
            <div
                className={`
          relative border-2 border-dashed rounded-lg p-8 text-center transition-colors
          ${dragActive ? 'border-primary bg-primary/5' : 'border-muted-foreground/25'}
          ${images.length >= maxImages ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer hover:border-primary'}
        `}
                onDragEnter={handleDrag}
                onDragLeave={handleDrag}
                onDragOver={handleDrag}
                onDrop={handleDrop}
                onClick={() => {
                    if (images.length < maxImages) {
                        document.getElementById('image-upload-input')?.click();
                    }
                }}
            >
                <input
                    id="image-upload-input"
                    type="file"
                    multiple
                    accept="image/*"
                    onChange={handleChange}
                    className="hidden"
                    disabled={images.length >= maxImages}
                />

                <Upload className="h-10 w-10 mx-auto mb-4 text-muted-foreground" />
                <p className="text-sm font-medium mb-1">
                    {images.length >= maxImages
                        ? `Maximum ${maxImages} images reached`
                        : 'Click to upload or drag and drop'
                    }
                </p>
                <p className="text-xs text-muted-foreground">
                    PNG, JPG, WEBP up to 5MB
                </p>
            </div>

            {error && (
                <p className="text-sm text-red-600">{error}</p>
            )}

            {/* Image Previews */}
            {images.length > 0 && (
                <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4">
                    {images.map((file, index) => (
                        <div key={index} className="relative group aspect-square">
                            <img
                                src={URL.createObjectURL(file)}
                                alt={`Preview ${index + 1}`}
                                className="w-full h-full object-cover rounded-lg border"
                            />
                            <Button
                                type="button"
                                variant="destructive"
                                size="icon"
                                className="absolute top-1 right-1 h-6 w-6 opacity-0 group-hover:opacity-100 transition-opacity"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    removeImage(index);
                                }}
                            >
                                <X className="h-4 w-4" />
                            </Button>
                            <div className="absolute bottom-1 left-1 bg-black/60 text-white text-xs px-2 py-1 rounded">
                                {(file.size / 1024 / 1024).toFixed(1)}MB
                            </div>
                        </div>
                    ))}
                </div>
            )}

            <p className="text-sm text-muted-foreground">
                {images.length} / {maxImages} images selected
            </p>
        </div>
    );
}
