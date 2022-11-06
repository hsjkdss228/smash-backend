package kr.megaptera.smash.exceptions;

public class GameNotFound extends RuntimeException {
  public GameNotFound() {
    super("게시물에 해당하는 경기 정보를 찾을 수 없습니다.");
  }
}
