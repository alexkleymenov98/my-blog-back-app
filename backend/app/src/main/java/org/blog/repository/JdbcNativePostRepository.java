package org.blog.repository;

import org.blog.model.Post;
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
    public List<Post> findAll() {
        return jdbcTemplate.query(
                "select p.id, p.title, p.text, p.tags, p.likes_count, count(c.id) from posts p left join \"comments\" c on p.id = c.post_id group by p.id, p.title, p.text, p.tags, p.likes_count order by p.id",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        toTagList(rs.getArray("tags")),
                        rs.getInt("likes_count"),
                        rs.getInt("count")
                ));
    }

    @Override
    public Post findById(Long id) {
        return jdbcTemplate.queryForObject(
                "select p.id, p.title, p.text, p.tags, p.likes_count, count(c.id) from posts p left join \"comments\" c on p.id = c.post_id where p.id = ? group by p.id, p.title, p.text, p.tags, p.likes_count;",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        toTagList(rs.getArray("tags")),
                        rs.getInt("likes_count"),
                        rs.getInt("count")
                ), id);
    }

}
