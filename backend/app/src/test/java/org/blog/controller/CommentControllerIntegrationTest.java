package org.blog.controller;

import com.jayway.jsonpath.JsonPath;
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
import org.springframework.test.web.servlet.MvcResult;
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
    void getComments_returnsJsonArray() throws Exception {
        mockMvc.perform(get("/api/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].text").value("Текст комментария"))
                .andExpect(jsonPath("$[1].text").value("Второй комментарий"));
    }

    @Test
    void getComment_returnsJson() throws Exception {
        mockMvc.perform(get("/api/posts/1/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("Текст комментария"));
    }

    @Test
    void deleteComment_noContent() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        // Извлекаем ID первого комментария
        Integer firstCommentId = JsonPath.read(jsonResponse, "$[0].id");

        // Удаляем комментарий по полученному ID
        mockMvc.perform(delete("/api/posts/1/comments/{commentId}", firstCommentId))
                .andExpect(status().isOk());

        // Проверяем, что комментарий удален
        mockMvc.perform(get("/api/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void updateComment_noContent() throws Exception {
        // Сначала получаем ID первого комментария
        MvcResult result = mockMvc.perform(get("/api/posts/1/comments"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Integer firstCommentId = JsonPath.read(jsonResponse, "$[0].id");

        String bodyJSON = String.format("""
            {
                "id": %d,
                "text": "обновление комментария",
                "postId": 1
            }
            """, firstCommentId);

        mockMvc.perform(put("/api/posts/1/comments/{firstCommentId}", firstCommentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("обновление комментария"));

    }
}
