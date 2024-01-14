package com.reddit.reddit.service;

import com.reddit.reddit.dto.PostRequest;
import com.reddit.reddit.dto.PostResponse;
import com.reddit.reddit.exception.PostNotFoundException;
import com.reddit.reddit.exception.SubredditNotFoundException;
import com.reddit.reddit.mapper.PostMapper;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.Subreddit;
import com.reddit.reddit.model.User;
import com.reddit.reddit.repository.PostRepository;
import com.reddit.reddit.repository.SubredditRepository;
import com.reddit.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
  private final SubredditRepository subredditRepository;
  private final UserRepository userRepository;
  private final AuthService authService;
  private final PostMapper postMapper;
  private final PostRepository postRepository;
  public Post save(PostRequest postRequest) {
      Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
        .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
    User  currentUser = authService.getCurrentUser();
    return postMapper.map(postRequest,subreddit,currentUser);
  }

  @Transactional(readOnly = true)
  public PostResponse getPost(Long id) {
    Post post = postRepository.findById(id)
      .orElseThrow(() -> new PostNotFoundException(id.toString()));
    return postMapper.mapToDto(post);
  }

  @Transactional(readOnly = true)
  public List<PostResponse> getAllPosts() {
    return postRepository.findAll()
      .stream()
      .map(postMapper::mapToDto)
      .collect(toList());
  }

  @Transactional(readOnly = true)
  public List<PostResponse> getPostsBySubreddit(Long subredditId) {
    Subreddit subreddit = subredditRepository.findById(subredditId)
      .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
    List<Post> posts = postRepository.findAllBySubreddit(subreddit);
    return posts.stream().map(postMapper::mapToDto).collect(toList());
  }

  @Transactional(readOnly = true)
  public List<PostResponse> getPostsByUsername(String username) {
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UsernameNotFoundException(username));
    return postRepository.findByUser(user)
      .stream()
      .map(postMapper::mapToDto)
      .collect(toList());
  }

}
