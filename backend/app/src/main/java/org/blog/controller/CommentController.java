package org.blog.controller;

import org.blog.model.Comment;
import org.blog.model.CreateComment;
import org.blog.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService service;

    public CommentController(CommentService commentService) {
        this.service = commentService;
    }

    @GetMapping
    public List<Comment> getComments(@PathVariable(name = "postId") Long postId) {
        return service.getComments(postId);
    }

    @GetMapping("/{commentId}")
    public Comment getComment(@PathVariable("postId") Long postId, @PathVariable(name = "commentId") Long commentId) {
       return service.getComment(postId, commentId);
    }

    @PostMapping
    public Comment createComment(@RequestBody CreateComment comment) {
        return service.createComment(comment);
    }

    @PutMapping("/{commentId}")
    public Comment updateComment( @RequestBody Comment comment) {
        return service.updateComment(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("postId") Long postId, @PathVariable(name = "commentId") Long commentId) {
        service.deleteComment(commentId);

        return ResponseEntity.ok().build();
    }

}
