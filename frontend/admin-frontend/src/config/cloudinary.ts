/**
 * Cloudinary Configuration
 * Separate cloud accounts for food and hostel services
 */

export const cloudinaryConfig = {
  food: {
    cloudName: import.meta.env.VITE_CLOUDINARY_FOOD_CLOUD_NAME || '',
    uploadPreset: import.meta.env.VITE_CLOUDINARY_FOOD_UPLOAD_PRESET || 'cdac_unsigned',
    folder: 'cdac-project',
  },
  hostel: {
    cloudName: import.meta.env.VITE_CLOUDINARY_HOSTEL_CLOUD_NAME || '',
    uploadPreset: import.meta.env.VITE_CLOUDINARY_HOSTEL_UPLOAD_PRESET || 'cdac_unsigned',
    folder: 'cdac-project',
  },
};

/**
 * Upload image to Cloudinary
 * @param file - File to upload
 * @param type - Service type (food or hostel)
 * @returns Secure URL of uploaded image
 */
export async function uploadToCloudinary(
  file: File,
  type: 'food' | 'hostel',
): Promise<string> {
  const config = cloudinaryConfig[type];

  if (!config.cloudName) {
    throw new Error(`Cloudinary cloud name not configured for ${type} service`);
  }

  const formData = new FormData();
  formData.append('file', file);
  formData.append('upload_preset', config.uploadPreset);
  formData.append('folder', config.folder);

  const response = await fetch(
    `https://api.cloudinary.com/v1_1/${config.cloudName}/image/upload`,
    {
      method: 'POST',
      body: formData,
    },
  );

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.error?.message || 'Failed to upload image');
  }

  const data = await response.json();
  return data.secure_url;
}

/**
 * Upload multiple images to Cloudinary
 * @param files - Files to upload
 * @param type - Service type (food or hostel)
 * @param onProgress - Progress callback (current, total)
 * @returns Array of secure URLs
 */
export async function uploadMultipleToCloudinary(
  files: File[],
  type: 'food' | 'hostel',
  onProgress?: (current: number, total: number) => void,
): Promise<string[]> {
  const urls: string[] = [];

  for (let i = 0; i < files.length; i++) {
    const url = await uploadToCloudinary(files[i], type);
    urls.push(url);
    onProgress?.(i + 1, files.length);
  }

  return urls;
}
