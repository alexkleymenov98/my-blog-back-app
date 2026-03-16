package org.blog.service;

import org.blog.helper.PaginationService;
import org.blog.model.CreatePost;
import org.blog.model.Pagination;
import org.blog.model.UpdatePost;
import org.blog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.blog.model.Post;

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
