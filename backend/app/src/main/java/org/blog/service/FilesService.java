package org.blog.service;

import org.blog.model.FileDto;
import org.blog.repository.FilesRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
          String fileName = fileRepository.getFileName(postId);

          return download(fileName);
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
