package kr.megaptera.smash.dtos;

import java.util.Map;

public class CreatePostFailedErrorDto {
    private final Map<Integer, String> errorCodeAndMessages;

    public CreatePostFailedErrorDto(Map<Integer, String> errorCodeAndMessages) {
        this.errorCodeAndMessages = errorCodeAndMessages;
    }

    public Map<Integer, String> getErrorCodeAndMessages() {
        return errorCodeAndMessages;
    }
}
