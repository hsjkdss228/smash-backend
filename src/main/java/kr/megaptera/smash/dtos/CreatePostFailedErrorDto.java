package kr.megaptera.smash.dtos;

import java.util.List;

public class CreatePostFailedErrorDto {
    private final List<String> errorMessages;

    public CreatePostFailedErrorDto(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
