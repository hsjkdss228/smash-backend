package kr.megaptera.smash.exceptions;

public class GameIsFull extends RuntimeException {
    public GameIsFull(Long gameId) {
        super("Game is full (Game ID: " + gameId + ")");
    }
}
