package de.brightslearning.webblog.media;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    String store(MultipartFile file);
    byte[] retrieve(String storedPath);
    void delete(String storedPath);
}
