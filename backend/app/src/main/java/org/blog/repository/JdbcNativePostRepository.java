package org.blog.repository;

import org.blog.model.CreatePost;
import org.blog.model.Post;
import org.blog.model.UpdatePost;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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

    private String[] convertSqlArrayToStringArray(java.sql.Array sqlArray) throws SQLException {
        if (sqlArray == null) {
            return new String[0];
        }

        // Получаем массив как Object[]
        Object[] objectArray = (Object[]) sqlArray.getArray();

        // Преобразуем Object[] в String[]
        String[] stringArray = new String[objectArray.length];
        for (int i = 0; i < objectArray.length; i++) {
            stringArray[i] = objectArray[i] != null ? objectArray[i].toString() : null;
        }

        return stringArray;
    }

    @Override
    public List<Post> findAll(String search) {

        String searchTerm = search.isEmpty() ? search : search.trim();

        String likeSearch;

        String sql;

        if(searchTerm.startsWith("#")){
            likeSearch = searchTerm.substring(1);

            sql = """
                SELECT    p.id,
                          p.title,
                          p.text,
                          p.tags,
                          p.likes_count,
                          Count(c.id) as count
                FROM      posts p
                LEFT JOIN comments c
                ON        p.id = c.post_id
                WHERE ? = ANY(p.tags)
                GROUP BY  p.id,
                          p.title,
                          p.text,
                          p.tags,
                          p.likes_count
                ORDER BY  p.id
                """;

        }
        else {
            likeSearch = searchTerm;

            sql = """
                SELECT    p.id,
                          p.title,
                          p.text,
                          p.tags,
                          p.likes_count,
                          Count(c.id) as count
                FROM      posts p
                LEFT JOIN comments c
                ON        p.id = c.post_id
                WHERE CONCAT(p.title, ' ', p.text) ILIKE '%' || ? || '%'
                GROUP BY  p.id,
                          p.title,
                          p.text,
                          p.tags,
                          p.likes_count
                ORDER BY  p.id
                """;
        }

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        truncate(rs.getString("text")),
                        convertSqlArrayToStringArray(rs.getArray("tags")),
                        rs.getInt("likes_count"),
                        rs.getInt("count")
                ), likeSearch);
    }

    @Override
    public Post findById(Long id) {
        String sql = """
                SELECT p.id,
                       p.title,
                       p.text,
                       p.tags,
                       p.likes_count,
                       Count(c.id) as count
                FROM   posts p
                       LEFT JOIN comments c
                              ON p.id = c.post_id
                WHERE  p.id = ?
                GROUP  BY p.id,
                          p.title,
                          p.text,
                          p.tags,
                          p.likes_count;\s
                """;
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        convertSqlArrayToStringArray(rs.getArray("tags")),
                        rs.getInt("likes_count"),
                        rs.getInt("count")
                ), id);
    }

    @Override
    public Post create(CreatePost createPost) {
        String sql = "INSERT INTO posts (title, text, tags) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, createPost.getTitle());
            ps.setString(2, createPost.getText());
            ps.setArray(3, connection.createArrayOf("text", createPost.getTags()));
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();

        // Получаем созданный пост
        return jdbcTemplate.queryForObject(
                "SELECT id, title, text, tags, likes_count FROM posts WHERE id = ?",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        convertSqlArrayToStringArray(rs.getArray("tags")),
                        rs.getInt("likes_count"),
                        0
                ),
                generatedId
        );

    }

    @Override
    public Integer incrementLikeById(Long id){
        jdbcTemplate.update("update posts set likes_count = likes_count + 1 where id = ?", id);

        return jdbcTemplate.queryForObject("select likes_count from posts where id = ?", (rs, rowNum)->rs.getInt("likes_count"), id);
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
