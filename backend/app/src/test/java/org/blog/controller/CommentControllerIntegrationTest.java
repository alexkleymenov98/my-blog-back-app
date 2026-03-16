package org.blog.controller;

import org.blog.WebConfiguration;
import org.blog.configuration.DataSourceConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@SpringJUnitConfig(classes = {
        DataSourceConfiguration.class,
        WebConfiguration.class,
})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
public class CommentControllerIntegrationTest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        // Чистим и наполняем БД перед каждым тестом
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");

        jdbcTemplate.update(
                "INSERT INTO posts (title, text, likes_count, tags) VALUES (?,?,?,?)",
                 "Пост", "описание поста", 0, new String[]{"java", "spring", "test"}
        );

        Long postId = jdbcTemplate.queryForObject(
                "SELECT id FROM posts WHERE title = ?",
                Long.class,
                "Пост"
        );

        jdbcTemplate.update(
                "INSERT INTO comments (text, post_id) VALUES (?,?)",
                "Текст комментария", postId
        );

        jdbcTemplate.update(
                "INSERT INTO comments (text, post_id) VALUES (?,?)",
                "Второй комментарий", postId
        );
    }

    private Long getPostId(){
        Long postId = jdbcTemplate.queryForObject(
                "SELECT id FROM posts WHERE title = ?",
                Long.class,
                "Пост"
        );
        return postId;
    }

    private Long getTestCommentId(){
        Long commentId = jdbcTemplate.queryForObject(
                "SELECT id FROM comments WHERE text = ?",
                Long.class,
                "Текст комментария"
        );
        return commentId;
    }

    @Test
    void getComments_returnsJsonArray() throws Exception {
        Long postId = getPostId();
        mockMvc.perform(get("/api/posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].text").value("Текст комментария"))
                .andExpect(jsonPath("$[1].text").value("Второй комментарий"));
    }

    @Test
    void getComment_returnsJson() throws Exception {
        Long postId = getPostId();
        Long commentId = getTestCommentId();
        mockMvc.perform(get("/api/posts/{postId}/comments/{commentId}", postId, commentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("Текст комментария"));
    }

    @Test
    void deleteComment_noContent() throws Exception {
        Long postId = getPostId();
        Long commentId = getTestCommentId();


        // Удаляем комментарий по полученному ID
        mockMvc.perform(delete("/api/posts/{postId}/comments/{commentId}", postId, commentId))
                .andExpect(status().isOk());

        // Проверяем, что комментарий удален
        mockMvc.perform(get("/api/posts/{postId}/comments", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void updateComment_noContent() throws Exception {
        Long postId = getPostId();

        Long commentId = getTestCommentId();

        String bodyJSON = String.format("""
            {
                "id": %d,
                "text": "обновление комментария",
                "postId": 1
            }
            """, commentId);

        mockMvc.perform(put("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("обновление комментария"));

    }
}
