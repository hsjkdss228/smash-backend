package kr.megaptera.smash.exceptions;

public class CreatePostFailed extends RuntimeException {
    public CreatePostFailed(String message) {
        super(message);
    }
}
