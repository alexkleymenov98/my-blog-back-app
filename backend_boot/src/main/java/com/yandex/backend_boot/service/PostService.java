package com.yandex.backend_boot.service;

import com.yandex.backend_boot.helper.PaginationService;
import com.yandex.backend_boot.model.CreatePost;
import com.yandex.backend_boot.model.Pagination;
import com.yandex.backend_boot.model.UpdatePost;
import com.yandex.backend_boot.repository.PostRepository;
import org.springframework.stereotype.Service;
import com.yandex.backend_boot.model.Post;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Pagination<Post> getAllPosts(String search, int pageNumber, int pageSize) {
        return new PaginationService<Post>(postRepository.findAll(search), pageNumber, pageSize).getPagination();
    }

    public Post findPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(CreatePost post) {
        return postRepository.create(post);
    }

    public Post updatePost(Long id, UpdatePost post) {
        return postRepository.updatedPostById(id, post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Integer incrementLike(Long postId) {
        return postRepository.incrementLikeById(postId);
    }
}
