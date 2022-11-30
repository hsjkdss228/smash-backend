package kr.megaptera.smash.exceptions;

public class AlreadyJoinedGame extends RuntimeException {
    public AlreadyJoinedGame(Long gameId) {
        super("이미 신청 중이거나 신청이 완료된 운동입니다. (gameId: " + gameId + ")");
    }
}
