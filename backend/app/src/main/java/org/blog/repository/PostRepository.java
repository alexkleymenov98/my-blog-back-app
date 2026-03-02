package org.blog.repository;

import org.blog.model.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findAll();
    Post findById(Long id);
}
