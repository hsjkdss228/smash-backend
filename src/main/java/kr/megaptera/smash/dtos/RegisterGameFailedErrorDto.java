package kr.megaptera.smash.dtos;

public class RegisterGameFailedErrorDto {
    private final Integer errorCode;

    private final String errorMessage;

    public RegisterGameFailedErrorDto(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
