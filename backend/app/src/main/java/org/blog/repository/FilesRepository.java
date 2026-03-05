package org.blog.repository;

import org.blog.model.FileDto;

public interface FilesRepository {
    void upload(FileDto fileDto);
    String getFileName(Long id);
}
