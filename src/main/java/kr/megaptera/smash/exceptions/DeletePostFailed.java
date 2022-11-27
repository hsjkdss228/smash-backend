package kr.megaptera.smash.exceptions;

public class DeletePostFailed extends RuntimeException {
    public DeletePostFailed(String message) {
        super(message);
    }
}
