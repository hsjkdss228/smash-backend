package kr.megaptera.smash.exceptions;

public class AuthenticationError extends RuntimeException {
  public AuthenticationError() {
    super("인가 과정에서 문제가 발생했습니다.");
  }
}
