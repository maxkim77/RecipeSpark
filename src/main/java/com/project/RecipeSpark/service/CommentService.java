package com.project.RecipeSpark.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.RecipeSpark.domain.Comment;
import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Comment addComment(User user, Recipe recipe, String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user);
        comment.setRecipe(recipe);
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getUser().equals(user)) {
            throw new RuntimeException("You can only delete your own comments.");
        }
        commentRepository.delete(comment);
    }

    @Transactional
    public void updateComment(Long commentId, User user, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getUser().equals(user)) {
            throw new RuntimeException("You can only update your own comments.");
        }
        comment.setContent(newContent);
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByRecipe(Recipe recipe) {
        return commentRepository.findByRecipeOrderByCreatedAtDesc(recipe);
    }

    @Transactional(readOnly = true)
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }
}
