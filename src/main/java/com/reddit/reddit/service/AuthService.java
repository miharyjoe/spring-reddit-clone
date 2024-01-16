package com.reddit.reddit.service;

import com.reddit.reddit.dto.AuthenticationResponse;
import com.reddit.reddit.dto.LoginRequest;
import com.reddit.reddit.dto.RefreshTokenRequest;
import com.reddit.reddit.dto.RegisterRequest;
import com.reddit.reddit.exception.SpringRedditException;
import com.reddit.reddit.model.NotificationEmail;
import com.reddit.reddit.model.User;
import com.reddit.reddit.model.VerificationToken;
import com.reddit.reddit.repository.UserRepository;
import com.reddit.reddit.repository.VerificationTokenRepository;
import com.reddit.reddit.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

  private final  PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;

  private final VerificationTokenRepository verificationTokenRepository;

  private final MailService mailService;

  private final AuthenticationManager authenticationManager;

  private final JwtProvider jwtProvider;

  private final RefreshTokenService refreshTokenService;

  @Transactional
  public void signup(RegisterRequest registerRequest){
    User user = new User();
    user.setUsername(registerRequest.getUsername());
    user.setEmail(registerRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    user.setCreated(Instant.now());
    user.setEnabled(false);

    userRepository.save(user);
    
    String token = generateVerificationToken(user);
    mailService.sendMail(new NotificationEmail("Please Active your Account",
      user.getEmail(), "Thank you for signing up to Spring Reddit, " +
      "please click on the below url to activate your account : " +
      "http://localhost:8080/api/auth/accountVerification/" + token));
  }

  private String generateVerificationToken(User user) {
    String token = UUID.randomUUID().toString();
    VerificationToken verificationToken = new VerificationToken();
    verificationToken.setToken(token);
    verificationToken.setUser(user);

    verificationTokenRepository.save(verificationToken);
    return token;
  }

  public void verifyAccount(String token) {
    Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
    verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
    fetchUserAndEnable(verificationToken.get());
  }
  @Transactional
  public void fetchUserAndEnable(VerificationToken verificationToken) {
   String username =  verificationToken.getUser().getUsername();
  User user =  userRepository.findByUsername(username)
     .orElseThrow(() ->  new SpringRedditException("user not found by username : " + username));
  user.setEnabled(true);
  userRepository.save(user);
  }

  public AuthenticationResponse login(LoginRequest loginRequest) {
    Authentication authenticate = authenticationManager
      .authenticate(new UsernamePasswordAuthenticationToken(
        loginRequest.getUsername(), loginRequest.getPassword()
      ));
    SecurityContextHolder.getContext().setAuthentication(authenticate);
    String token = jwtProvider.generateToken(authenticate);
    return AuthenticationResponse.builder()
      .authenticationToken(token)
      .refreshToken(refreshTokenService.generateRefreshToken().getToken())
      .expiresAt(Instant.now()
        .plusMillis(jwtProvider.getJwtExpirationInMillis()))
      .username(loginRequest.getUsername())
      .build();
  }

  @Transactional(readOnly = true)
  public User getCurrentUser() {
    Jwt principal = (Jwt) SecurityContextHolder.
      getContext().getAuthentication().getPrincipal();
    return userRepository.findByUsername(principal.getSubject())
      .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
  }

  public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
    refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
    String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
    return AuthenticationResponse.builder()
      .authenticationToken(token)
      .refreshToken(refreshTokenRequest.getRefreshToken())
      .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
      .username(refreshTokenRequest.getUsername())
      .build();
  }

}
