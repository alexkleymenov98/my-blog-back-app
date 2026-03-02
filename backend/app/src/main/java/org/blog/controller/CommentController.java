package org.blog.controller;

import org.blog.model.Comment;
import org.blog.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService commentService) {
        this.service = commentService;
    }

    @GetMapping
    public List<Comment> getComments(@PathVariable("postId") Long postId) {
        return service.getComments(postId);
    }

    @GetMapping("/{commentId}")
    public Comment getComment(@PathVariable("postId") Long postId, @PathVariable(name = "commentId") Long commentId) {
       return service.getComment(postId, commentId);
    }
}
