package com.reddit.reddit.service;


import com.reddit.reddit.dto.SubredditDto;
import com.reddit.reddit.exception.SpringRedditException;
import com.reddit.reddit.mapper.SubredditMapper;
import com.reddit.reddit.model.Subreddit;
import com.reddit.reddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

  private final SubredditRepository subredditRepository;
  private final SubredditMapper subredditMapper;

  @Transactional
  public SubredditDto save(SubredditDto subredditDto){
    Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
    subredditDto.setId(save.getId());
    return subredditDto;
  }

  @Transactional(readOnly = true)
  public List<SubredditDto> getAll() {
    return subredditRepository.findAll()
      .stream()
      .map(subredditMapper::mapSubredditToDto)
      .collect(Collectors.toList());
  }

  public SubredditDto getSubreddit(Long id){
    Subreddit subreddit = subredditRepository.findById(id)
      .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
    return subredditMapper.mapSubredditToDto(subreddit);
  }
}
