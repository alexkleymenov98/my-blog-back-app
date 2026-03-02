package org.blog.service;


import org.blog.model.Comment;
import org.blog.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getComments(Long postId) {
        return commentRepository.findAll(postId);
    }

    public Comment getComment(Long postId, Long commentId) {
        return commentRepository.findOne(postId, commentId);
    }
}
