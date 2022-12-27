package kr.megaptera.smash.exceptions;

public class IsNotRegisterOfCurrentUser extends RuntimeException {
    public IsNotRegisterOfCurrentUser() {
        super("신청 번호와 사용자 번호가 일치하지 않습니다.");
    }
}
