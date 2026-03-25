package com.yandex.backend_boot.repository;

import com.yandex.backend_boot.model.CreatePost;
import com.yandex.backend_boot.model.Post;
import com.yandex.backend_boot.model.UpdatePost;

import java.util.List;

public interface PostRepository {
    List<Post> findAll(String search);
    Post findById(Long id);
    Post create(CreatePost post);
    Integer incrementLikeById(Long id);
    Post updatedPostById(Long id, UpdatePost updatedPost);
    void deleteById(Long id);
}
