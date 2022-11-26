package kr.megaptera.smash.exceptions;

import java.util.List;

public class CreatePostFailed extends RuntimeException {
    private final List<String> errorMessages;

    public CreatePostFailed(String message) {
        errorMessages = List.of(message);
    }

    public CreatePostFailed(List<String> messages) {
        errorMessages  = messages.stream().toList();
    }

    public List<String> errorMessages() {
        return errorMessages.stream().toList();
    }
}
