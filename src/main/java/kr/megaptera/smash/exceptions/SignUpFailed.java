package kr.megaptera.smash.exceptions;

import java.util.List;

public class SignUpFailed extends RuntimeException {
    private final List<String> errorMessages;

    public SignUpFailed(String message) {
        errorMessages = List.of(message);
    }

    public SignUpFailed(List<String> messages) {
        errorMessages  = messages.stream().toList();
    }

    public List<String> errorMessages() {
        return errorMessages.stream().toList();
    }
}
