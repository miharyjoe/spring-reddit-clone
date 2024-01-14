package com.reddit.reddit.service;

import com.reddit.reddit.dto.CommentsDto;
import com.reddit.reddit.exception.PostNotFoundException;
import com.reddit.reddit.mapper.CommentMapper;
import com.reddit.reddit.model.Comment;
import com.reddit.reddit.model.NotificationEmail;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.User;
import com.reddit.reddit.repository.CommentRepository;
import com.reddit.reddit.repository.PostRepository;
import com.reddit.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {
  private static final String POST_URL = "";
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final CommentMapper commentMapper;
  private final AuthService authService;
  private final MailService mailService;
  private final MailContentBuilder mailContentBuilder;
  public void save(CommentsDto commentsDto) {
    Post post = postRepository.findById(commentsDto.getPostId())
      .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
    Comment comment = commentMapper.map(commentsDto,post,authService.getCurrentUser());
    commentRepository.save(comment);

    String message = mailContentBuilder.build(post.getUser().getUsername()
      + " posted a comment on your post." + POST_URL);
    sendCommentNotification(message, post.getUser());
  }

  private void sendCommentNotification(String message, User user) {
    mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
  }

  public List<CommentsDto> getAllCommentsForPost(Long postId) {
    Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
    return commentRepository.findByPost(post)
      .stream()
      .map(commentMapper::mapToDto)
      .collect(toList());
  }

  public List<CommentsDto> getAllCommentsForUser(String userName) {
    User user = userRepository.findByUsername(userName)
      .orElseThrow(() -> new UsernameNotFoundException(userName));
    return commentRepository.findAllByUser(user)
      .stream()
      .map(commentMapper::mapToDto)
      .toList();
  }
}
