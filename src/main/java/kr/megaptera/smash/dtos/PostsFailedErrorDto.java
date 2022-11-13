package kr.megaptera.smash.dtos;

public class PostsFailedErrorDto {
    private final String errorMessage;

    public PostsFailedErrorDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
