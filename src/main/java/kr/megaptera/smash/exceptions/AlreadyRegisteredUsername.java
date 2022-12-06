package kr.megaptera.smash.exceptions;

public class AlreadyRegisteredUsername extends RuntimeException {
    public AlreadyRegisteredUsername() {
        super("이미 등록된 아이디입니다.");
    }
}
