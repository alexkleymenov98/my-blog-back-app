package org.blog.service;

import org.blog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.blog.model.Post;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post findPostById(Long id) {
        return postRepository.findById(id);
    }
}
