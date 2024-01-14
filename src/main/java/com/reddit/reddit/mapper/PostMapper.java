package com.reddit.reddit.mapper;

import com.reddit.reddit.dto.PostRequest;
import com.reddit.reddit.dto.PostResponse;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.Subreddit;
import com.reddit.reddit.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class  PostMapper {

  @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
  @Mapping(target = "description", source = "postRequest.description")
  @Mapping(target = "subreddit", source = "subreddit")
  @Mapping(target = "user", source = "user")
  public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

  @Mapping(target = "id", source = "postId")
  @Mapping(target = "subredditName", source = "subreddit.name")
  @Mapping(target = "userName", source = "user.username")
  public abstract PostResponse mapToDto(Post post);

}
