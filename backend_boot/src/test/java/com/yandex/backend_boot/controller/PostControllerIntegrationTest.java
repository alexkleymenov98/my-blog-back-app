package com.yandex.backend_boot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        jdbcTemplate.execute("DELETE FROM posts");

        jdbcTemplate.update(
                "INSERT INTO posts (title, text, likes_count, tags) VALUES (?,?,?,?)",
                "Пост", "описание поста", 0, new String[]{"java", "spring", "test"}
        );

        jdbcTemplate.update(
                "INSERT INTO posts (title, text, likes_count, tags) VALUES (?,?,?,?)",
                "Пост 2", "описание второго поста", 0, new String[]{"front", "react"}
        );
    }

    @Test
    void getPosts_returnsJsonArray() throws Exception {
        mockMvc.perform(get("/api/posts?search=&pageNumber=1&pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.hasPrev").value(false))
                .andExpect(jsonPath("$.posts", hasSize(2)))
                .andExpect(jsonPath("$.posts[0].title").value("Пост"))
                .andExpect(jsonPath("$.posts[1].title").value("Пост 2"));
    }
    @Test
    void getPostsWithText_returnsJsonArray() throws Exception {
        mockMvc.perform(get("/api/posts?search=2&pageNumber=1&pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.hasPrev").value(false))
                .andExpect(jsonPath("$.posts", hasSize(1)))
                .andExpect(jsonPath("$.posts[0].title").value("Пост 2"));
    }
}
