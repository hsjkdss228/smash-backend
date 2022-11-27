package kr.megaptera.smash.exceptions;

public class AlreadyGameJoined extends RuntimeException {
    public AlreadyGameJoined(Long gameId) {
        super("Already game joined (Game ID: " + gameId + ")");
    }
}
