package com.reddit.reddit.mapper;


import com.reddit.reddit.dto.SubredditDto;
import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {
  @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
  SubredditDto mapSubredditToDto(Subreddit subreddit);

  default Integer mapPosts(List<Post> numberOfPosts) {
    return numberOfPosts.size();
  }
  @InheritInverseConfiguration
  @Mapping(target = "posts", ignore = true)
  Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
