package kr.megaptera.smash.exceptions;

public class GameNotFound extends RuntimeException {
    public GameNotFound() {
        super("게임을 찾을 수 없습니다.");
    }

    public GameNotFound(Long gameId) {
        super("주어진 게임 번호에 해당하는 게임을 찾을 수 없습니다. (gameId: " + gameId + ")");
    }
}
