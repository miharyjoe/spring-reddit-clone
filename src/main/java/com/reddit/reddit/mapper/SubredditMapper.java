package com.reddit.reddit.mapper;


import com.reddit.reddit.dto.SubredditDto;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.Subreddit;
import com.reddit.reddit.repository.PostRepository;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SubredditMapper {

  @Autowired
  private PostRepository postRepository;
  @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit))")
  public abstract SubredditDto mapSubredditToDto(Subreddit subreddit);

  Integer mapPosts(Subreddit subreddit) {
    return postRepository.findAllBySubreddit(subreddit).size();
  }
  @InheritInverseConfiguration
  @Mapping(target = "posts", ignore = true)
  public abstract  Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
