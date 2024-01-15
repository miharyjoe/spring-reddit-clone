package com.reddit.reddit.repository;

import com.reddit.reddit.model.Post;
import com.reddit.reddit.model.User;
import com.reddit.reddit.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
  Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
