package com.yandex.backend_boot.repository;

import com.yandex.backend_boot.model.Comment;
import com.yandex.backend_boot.model.CreateComment;

import java.util.List;

public interface CommentRepository {
    List<Comment> findAll(Long postId);
    Comment findOne(Long postId, Long commentId);
    void deleteById(Long commentId);
    Comment create(CreateComment comment);
    Comment update(Comment updateComment);
}
