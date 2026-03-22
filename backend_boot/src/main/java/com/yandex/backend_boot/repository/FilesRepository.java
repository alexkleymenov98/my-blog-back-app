package com.yandex.backend_boot.repository;

import com.yandex.backend_boot.model.FileDto;

import java.util.Optional;

public interface FilesRepository {
    void upload(FileDto fileDto);
    Optional<String> getFileName(Long id);
}
