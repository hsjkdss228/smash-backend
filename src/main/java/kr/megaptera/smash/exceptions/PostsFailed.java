package kr.megaptera.smash.exceptions;

public class PostsFailed extends RuntimeException {
    public PostsFailed(String message) {
        super(message);
    }
}
