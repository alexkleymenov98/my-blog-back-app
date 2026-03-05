package org.blog.repository;

import org.blog.model.Comment;
import org.blog.model.CreateComment;
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

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from comments where id = ?", id);
    }

    @Override
    public Comment create(CreateComment createComment) {

        return jdbcTemplate.queryForObject("insert into comments(text, post_id) values(?, ?) returning id, text, post_id;", (rs, rowNum) -> {
            Comment comment = new Comment();
            comment.setId(rs.getLong("id"));
            comment.setText(rs.getString("text"));
            comment.setPostId(rs.getLong("post_id"));

            return comment;
        }, createComment.getText(), createComment.getPostId());
    }

    @Override
    public Comment update(Comment updateComment) {
        return jdbcTemplate.queryForObject("update comments set text = ? where id = ? returning id, text, post_id;", (rs, rowNum) -> {
            Comment comment = new Comment();
            comment.setId(rs.getLong("id"));
            comment.setText(rs.getString("text"));
            comment.setPostId(rs.getLong("post_id"));

            return comment;
        }, updateComment.getText(), updateComment.getId());
    }

}
