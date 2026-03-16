package org.blog.repository;

import org.blog.configuration.DataSourceConfiguration;
import org.blog.model.Comment;
import org.blog.model.CreateComment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, JdbcNativeCommentRepository.class})
@TestPropertySource(locations = "classpath:test-application.properties")
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class JdbcNativeCommentRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM comments");

        jdbcTemplate.update(
                "INSERT INTO posts (title, text, likes_count, tags) VALUES (?,?,?,?)",
                 "Пост", "описание поста", 0, new String[]{"java", "spring", "test"}
        );

        jdbcTemplate.update(
                "INSERT INTO comments ( text, post_id) VALUES (?,?)",
                 "Текст комментария", 1L
        );

        jdbcTemplate.update(
                "INSERT INTO comments ( text, post_id) VALUES (?,?)",
                "Второй комментарий", 1L
        );

    }

    @Test
    void findById_shouldReturnCommentsForPost() {
        List<Comment> comments = commentRepository.findAll(1L);

        assertNotNull(comments);
        assertEquals(2, comments.size());
    }


    @Test
    void addedComment_shouldCreatedComment() {

        commentRepository.create(new CreateComment("Созданный комментарий", 1L));

        List<Comment> comments = commentRepository.findAll(1L);

        assertNotNull(comments);
        assertEquals(3, comments.size());
    }

    @Test
    void updateComment_shouldUpdatedComment() {

        Comment updateComment = commentRepository.update(new Comment(1L, "Обновленный комментарий", 1L));

        assertNotNull(updateComment);
        assertEquals("Обновленный комментарий", updateComment.getText());
    }

    @Test
    void deleteComment() {

        List<Comment> commentsOld = commentRepository.findAll(1L);

        Long id = commentsOld.get(0).getId();

        commentRepository.deleteById(id);

        List<Comment> comments = commentRepository.findAll(1L);

        assertNotNull(comments);
        assertEquals(1, comments.size());
    }

}
