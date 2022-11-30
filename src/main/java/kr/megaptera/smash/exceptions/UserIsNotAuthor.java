package kr.megaptera.smash.exceptions;

public class UserIsNotAuthor extends RuntimeException {
    public UserIsNotAuthor() {
        super("접속한 사용자가 게시글 작성자가 아닙니다.");
    }
}
