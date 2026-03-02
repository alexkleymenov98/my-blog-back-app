package org.blog.controller;

import org.blog.model.Post;
import org.blog.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public List<Post> getPosts() {
        return service.getAllPosts();
    }

    @GetMapping("/{postId}")
    public Post getPosts(@PathVariable(name = "postId") Long postId) {
        return service.findPostById(postId);
    }
}
