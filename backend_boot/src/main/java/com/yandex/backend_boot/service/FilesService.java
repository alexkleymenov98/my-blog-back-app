package com.yandex.backend_boot.service;

import com.yandex.backend_boot.model.FileDto;
import com.yandex.backend_boot.repository.FilesRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FilesService {
    private final FilesRepository fileRepository;
    public static final String UPLOAD_DIR = "uploads/";

    public FilesService(FilesRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void uploadStorage(MultipartFile file, Long postId){
        String fileName = upload(file);
        fileRepository.upload(new FileDto(fileName, postId));

    }

    public Resource getFromStorage(Long postId){
          Optional<String> fileName = fileRepository.getFileName(postId);

        if (fileName.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found for post ID: " + postId);
        }

        return download(fileName.get());
    }

    public String upload(MultipartFile file) {
        try {
            Path uploadDir = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(file.getOriginalFilename());
            file.transferTo(filePath);

            return file.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Resource download(String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            byte[] content = Files.readAllBytes(filePath);

            return new ByteArrayResource(content);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
