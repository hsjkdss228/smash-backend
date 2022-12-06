package kr.megaptera.smash.exceptions;

public class UnmatchedPasswordAndAConfirmPassword extends RuntimeException {
    public UnmatchedPasswordAndAConfirmPassword() {
        super("비밀번호 확인이 일치하지 않습니다.");
    }
}
