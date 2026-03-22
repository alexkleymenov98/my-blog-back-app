package com.yandex.backend_boot.repository;

import com.yandex.backend_boot.model.FileDto;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcNativeFileRepository implements FilesRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeFileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void upload(FileDto fileDto) {

        jdbcTemplate.update("delete from post_images where post_id = ?", fileDto.getPostId());

        String sql = "insert into post_images(post_id, file_name) values(?, ?)";
        jdbcTemplate.update(sql,
                fileDto.getPostId(), fileDto.getFileName());
    }

    @Override
    public Optional<String> getFileName(Long id) {
        try {
            String fileName = jdbcTemplate.queryForObject(
                    "select file_name from post_images where post_id = ? limit 1",
                    (rs, rowNum) -> rs.getString("file_name"),
                    id
            );
            return Optional.ofNullable(fileName);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

}
