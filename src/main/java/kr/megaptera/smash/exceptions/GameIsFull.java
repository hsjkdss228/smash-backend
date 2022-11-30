package kr.megaptera.smash.exceptions;

public class GameIsFull extends RuntimeException {
    public GameIsFull(Long gameId) {
        super("참가 정원이 모두 차 참가를 신청할 수 없습니다: (gameId: " + gameId + ")");
    }
}
