package org.blog.repository;

import org.blog.model.Comment;
import org.blog.model.FileDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
    public String getFileName(Long id) {
        return jdbcTemplate.queryForObject(
                "select file_name from post_images where post_id = ? limit 1",
                (rs, rowNum) ->  rs.getString("file_name")
                , id);
    }

}
