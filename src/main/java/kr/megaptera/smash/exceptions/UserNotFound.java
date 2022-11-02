package kr.megaptera.smash.exceptions;

public class UserNotFound extends RuntimeException {
  public UserNotFound() {
    super("사용자를 찾을 수 없습니다.");
  }
}
