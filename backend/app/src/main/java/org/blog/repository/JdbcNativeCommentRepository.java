package org.blog.repository;

import org.blog.model.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comment> findAll(Long postId) {
        return jdbcTemplate.query(
                "select id, text, post_id from comments where post_id = ?",
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getString("text"),
                        rs.getLong("post_id")
                ), postId);
    }

    @Override
    public Comment findOne(Long postId, Long commentId) {
        String sql = "SELECT id, text, post_id FROM comments WHERE post_id = ? AND id = ?";

        return jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getString("text"),
                        rs.getLong("post_id")
                ),
                postId, commentId
        );
    }

}
