package com.reddit.reddit.service;

import com.reddit.reddit.dto.PostRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
  public void save(PostRequest postRequest){

  }
}
