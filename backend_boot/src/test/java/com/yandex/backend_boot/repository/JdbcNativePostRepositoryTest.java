package com.yandex.backend_boot.repository;

import com.yandex.backend_boot.model.CreatePost;
import com.yandex.backend_boot.model.Post;
import com.yandex.backend_boot.model.UpdatePost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJdbcTest
@ActiveProfiles("test")  // Добавьте эту аннотацию
@Sql(scripts = "/schema-h2.sql")
@Import(TestRepositoryConfig.class) 
public class JdbcNativePostRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
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

    private Long getFirstPostId(String search){
        List<Post> postsOld = postRepository.findAll(search);

        return postsOld.getFirst().getId();
    }

    @Test
    void incrementLike_shouldReturnNewCount() {
        Long id = getFirstPostId("");
        Integer count = postRepository.incrementLikeById(id);

        assertEquals(1, count );

        Integer secondCount = postRepository.incrementLikeById(id);

        assertEquals(2, secondCount);
    }

    @Test
    void deletePost() {
        List<Post> postsOld = postRepository.findAll("");

        Long id = getFirstPostId("");

        postRepository.deleteById(id);

        List<Post> posts = postRepository.findAll("");

        assertEquals(1, posts.size());
    }

    @Test
    void findAllPostsWithoutSearch() {
        List<Post> posts = postRepository.findAll("");

        assertNotNull(posts);

        assertEquals(2, posts.size());
    }

    @Test
    void findAllPostsWithSearch(){
        List<Post> posts = postRepository.findAll("2");

        assertNotNull(posts);

        assertEquals(1, posts.size());
    }

    @Test
    void findAllPostsWithTagSearch(){
        List<Post> posts = postRepository.findAll("#react");

        assertNotNull(posts);

        assertEquals(1, posts.size());
    }


    @Test
    void findPostById_shouldReturnPost() {
        Long id = getFirstPostId("");
        Post post = postRepository.findById(id);

        assertNotNull(post);

        assertEquals("Пост", post.getTitle());
    }

    @Test
    void updatePostById_shouldReturnPost() {
        Long id = getFirstPostId("");

        Post post = postRepository.updatedPostById(id, new UpdatePost(id,"Новое название", "text", new String[]{"tag"}));

        assertNotNull(post);

        assertEquals("Новое название", post.getTitle());
        assertEquals("text", post.getText());
        assertEquals(1, post.getTags().length);
    }

    @Test
    void createPostById_shouldReturnPost() {

        Post post = postRepository.create( new CreatePost("Новое название", "text", new String[]{"tag"}));

        assertNotNull(post);

        assertEquals("Новое название", post.getTitle());
        assertEquals("text", post.getText());
        assertEquals(1, post.getTags().length);

        List<Post> posts = postRepository.findAll("");
        assertEquals(3, posts.size());

    }
}
