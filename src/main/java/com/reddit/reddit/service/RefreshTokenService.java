package com.reddit.reddit.service;

import com.reddit.reddit.exception.SpringRedditException;
import com.reddit.reddit.model.RefreshToken;
import com.reddit.reddit.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  public RefreshToken generateRefreshToken(){
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setCreatedDate(Instant.now());

    return refreshTokenRepository.save(refreshToken);
  }

  void validateRefreshToken(String token){
    refreshTokenRepository.findByToken(token)
      .orElseThrow(() -> new SpringRedditException("invalid refresh token"));

  }

  public void deleteRefreshToken(String token){
    refreshTokenRepository.deleteByToken(token);
  }
}
