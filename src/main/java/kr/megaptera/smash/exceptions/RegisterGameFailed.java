package kr.megaptera.smash.exceptions;

public class RegisterGameFailed extends RuntimeException {
    public RegisterGameFailed(String message) {
        super(message);
    }
}
