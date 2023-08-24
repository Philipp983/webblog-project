package de.brightslearning.webblog.media;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalImageStorageService implements ImageStorageService {

    @Value("${image.storage.path}")
    private String storagePath;

    @Override
    public String store(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + extension;

            File targetFile = new File(storagePath + File.separator + newFileName);
            FileCopyUtils.copy(file.getInputStream(), Files.newOutputStream(targetFile.toPath()));

            return newFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }

    @Override
    public byte[] retrieve(String storedPath) {
        try {
            return Files.readAllBytes(Paths.get(storagePath, storedPath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image", e);
        }
    }

    @Override
    public void delete(String storedPath) {
        try {
            Files.deleteIfExists(Paths.get(storagePath, storedPath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }
}
