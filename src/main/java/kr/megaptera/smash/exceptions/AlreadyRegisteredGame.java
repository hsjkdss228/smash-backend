package kr.megaptera.smash.exceptions;

public class AlreadyRegisteredGame extends RuntimeException {
    public AlreadyRegisteredGame() {
        super("이미 신청이 완료된 운동입니다.");
    }
}
