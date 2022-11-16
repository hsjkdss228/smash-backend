package kr.megaptera.smash.exceptions;

public class LoginFailed extends RuntimeException {
    public LoginFailed(String errorMessage) {
        super(errorMessage);
    }
}
