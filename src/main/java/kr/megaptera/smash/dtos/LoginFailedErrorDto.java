package kr.megaptera.smash.dtos;

public class LoginFailedErrorDto {
    private final String errorMessage;

    public LoginFailedErrorDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
