package com.reddit.reddit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "token")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String token;

  @OneToOne(fetch = FetchType.LAZY)
  private User user;

  private Instant expiryDate;
}
