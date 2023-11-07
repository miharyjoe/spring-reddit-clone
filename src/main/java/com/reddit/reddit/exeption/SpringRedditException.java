package com.reddit.reddit.exeption;

public class SpringRedditException extends RuntimeException {
  public SpringRedditException(String exMessage) {
    super(exMessage);
  }
}
