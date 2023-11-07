package com.reddit.reddit.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public enum VoteType {
  UPVOTE(1),DOWNVOTE(-1);

  VoteType(int direction){

  }

}
