package com.reddit.reddit.repository;

import com.reddit.reddit.model.Comment;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByPost(Post post);

  List<Comment> findAllByUser(User user);
}
