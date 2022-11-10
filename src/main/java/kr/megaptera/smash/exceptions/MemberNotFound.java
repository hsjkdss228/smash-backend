package kr.megaptera.smash.exceptions;

public class MemberNotFound extends RuntimeException {
    public MemberNotFound() {
        super("멤버를 찾을 수 없습니다.");
    }
}
