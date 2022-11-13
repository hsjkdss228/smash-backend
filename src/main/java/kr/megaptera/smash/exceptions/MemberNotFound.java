package kr.megaptera.smash.exceptions;

public class MemberNotFound extends RuntimeException {
    public MemberNotFound() {
        super("주어진 게임 번호, 사용자 번호와 일치하는 참가자를 확인할 수 없습니다.");
    }
}
