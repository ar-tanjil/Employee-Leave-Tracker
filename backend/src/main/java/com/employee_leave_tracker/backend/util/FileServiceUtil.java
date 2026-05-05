package com.employee_leave_tracker.backend.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Base64;

@Component
public final class FileServiceUtil {

    public String convertImageToBase64(MultipartFile file) throws Exception {

        if (file == null || file.isEmpty()) {
            return "";
        }

        // 1. Check MIME type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // 2. Validate actual content (important)
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) {
            throw new IllegalArgumentException("Invalid image file");
        }

        // 3. Convert to Base64
        String base64 = Base64.getEncoder().encodeToString(file.getBytes());

        // 4. Add prefix (for frontend rendering)
        return "data:" + contentType + ";base64," + base64;
    }
}
