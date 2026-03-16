package org.blog.repository;

import org.blog.model.CreatePost;
import org.blog.model.Post;
import org.blog.model.UpdatePost;

import java.util.List;

public interface PostRepository {
    List<Post> findAll(String search);
    Post findById(Long id);
    Post create(CreatePost post);
    Integer incrementLikeById(Long id);
    Post updatedPostById(Long id, UpdatePost updatedPost);
    void deleteById(Long id);
}
