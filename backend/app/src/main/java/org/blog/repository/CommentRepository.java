package org.blog.repository;

import org.blog.model.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> findAll(Long postId);
    Comment findOne(Long postId, Long commentId);
}
