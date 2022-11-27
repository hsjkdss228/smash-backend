package kr.megaptera.smash.exceptions;

public class RegisterGameFailed extends RuntimeException {
    private Long gameId;

    public RegisterGameFailed(String message) {
        super(message);
    }

    public RegisterGameFailed(String message, Long gameId) {
        super(message);
        this.gameId = gameId;
    }

    public Long getGameId() {
        return gameId;
    }
}
