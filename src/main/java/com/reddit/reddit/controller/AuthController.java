package com.reddit.reddit.controller;

import com.reddit.reddit.dto.AuthenticationResponse;
import com.reddit.reddit.dto.LoginRequest;
import com.reddit.reddit.dto.RefreshTokenRequest;
import com.reddit.reddit.dto.RegisterRequest;
import com.reddit.reddit.service.AuthService;
import com.reddit.reddit.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final RefreshTokenService refreshTokenService;

  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
    authService.signup(registerRequest);
    return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
  }

  @GetMapping("/accountVerification/{token}")
  public ResponseEntity<String> verifyAccount(@PathVariable String token){
      authService.verifyAccount(token);
    return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
  }

  @PostMapping("/login")
  public AuthenticationResponse login (@RequestBody LoginRequest loginRequest){
   return authService.login(loginRequest);
  }

  @PostMapping("/refresh/token")
  public AuthenticationResponse refreshToken (
    @Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
    return authService.refreshToken(refreshTokenRequest);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
  }
}
