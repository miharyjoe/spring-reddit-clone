package com.reddit.reddit.controller;

import com.reddit.reddit.dto.CommentsDto;
import com.reddit.reddit.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {
  private final CommentService commentService;
  @PostMapping
  public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
    commentService.save(commentsDto);
    return new ResponseEntity<>(CREATED);
  }

  @GetMapping(params = "postId")
  public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@RequestParam Long postId) {
    return ResponseEntity.status(OK)
      .body(commentService.getAllCommentsForPost(postId));
  }

  @GetMapping(params = "userName")
  public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@RequestParam String userName){
    return ResponseEntity.status(OK)
      .body(commentService.getAllCommentsForUser(userName));
  }
}
