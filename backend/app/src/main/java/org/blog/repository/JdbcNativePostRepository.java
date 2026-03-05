package org.blog.repository;

import org.blog.model.CreatePost;
import org.blog.model.Pagination;
import org.blog.model.Post;
import org.blog.model.UpdatePost;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class JdbcNativePostRepository implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static String truncate(String text) {
        if (text == null) return null;
        if (text.length() <= 128) return text;
        return text.substring(0, 128) + "...";
    }

    private List<String> toTagList(java.sql.Array sqlArray) {
        try {
            if (sqlArray != null) {
                String[] tags = (String[]) sqlArray.getArray();
                return Arrays.asList(tags);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Post> findAll(String search) {
        return jdbcTemplate.query(
                "select p.id, p.title, p.text, p.tags, p.likes_count, count(c.id) from posts p left join comments c on p.id = c.post_id WHERE title ILIKE '%' || ? || '%' group by p.id, p.title, p.text, p.tags, p.likes_count order by p.id",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        truncate(rs.getString("text")),
                        toTagList(rs.getArray("tags")),
                        rs.getInt("likes_count"),
                        rs.getInt("count")
                ), search);
    }

    @Override
    public Post findById(Long id) {
        return jdbcTemplate.queryForObject(
                "select p.id, p.title, p.text, p.tags, p.likes_count, count(c.id) from posts p left join comments c on p.id = c.post_id where p.id = ? group by p.id, p.title, p.text, p.tags, p.likes_count;",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        toTagList(rs.getArray("tags")),
                        rs.getInt("likes_count"),
                        rs.getInt("count")
                ), id);
    }

    @Override
    public Post create(CreatePost createPost) {
        return jdbcTemplate.queryForObject("insert into posts(title, text, tags) values(?, ?, ?) returning id, title, text, tags,likes_count;",
                (rs, rowNum)->new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        toTagList(rs.getArray("tags")),
                        rs.getInt("likes_count"),
                        0
                ),
                createPost.getTitle(), createPost.getText(), createPost.getTags());

    }

    @Override
    public Integer incrementLikeById(Long id){
        return jdbcTemplate.queryForObject("update posts set likes_count = likes_count + 1 where id = ? returning likes_count;", Integer.class, id);
    }

    @Override
    public Post updatedPostById(Long id, UpdatePost updatePost) {;

        jdbcTemplate.update("update posts set title = ?,text = ?, tags = ? where id = ?",
                updatePost.getTitle(), updatePost.getText(), updatePost.getTags(), id);

        return this.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from posts where id = ?", id);
    }
}
