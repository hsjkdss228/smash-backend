package kr.megaptera.smash.exceptions;

public class UserNotFound extends RuntimeException {
    public UserNotFound() {
        super("주어진 사용자 번호에 해당하는 사용자를 찾을 수 없습니다.");
    }
}
