package org.blog.repository;

import org.blog.model.Comment;
import org.blog.model.CreateComment;

import java.util.List;

public interface CommentRepository {
    List<Comment> findAll(Long postId);
    Comment findOne(Long postId, Long commentId);
    void deleteById(Long commentId);
    Comment create(CreateComment comment);
    Comment update(Comment updateComment);
}
