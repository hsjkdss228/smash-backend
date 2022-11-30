package kr.megaptera.smash.exceptions;

public class PostNotFound extends RuntimeException {
  public PostNotFound() {
    super("주어진 게시물 번호에 해당하는 게시물을 찾을 수 없습니다.");
  }

  public PostNotFound(Long postId) {
    super("주어진 게시물 번호에 해당하는 게시물을 찾을 수 없습니다. (postId: " + postId + ")");
  }
}
