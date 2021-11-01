package com.example.demo.repasitory;

import com.example.demo.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository <Comment,Integer> {
    List<Comment> findCommentByProductId(long productId);
}


