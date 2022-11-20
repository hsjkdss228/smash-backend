package kr.megaptera.smash.exceptions;

public class RegisterIdAndUserIdNotMatch extends RuntimeException {
    public RegisterIdAndUserIdNotMatch() {
        super("신청 번호와 사용자 번호가 일치하지 않습니다.");
    }
}
