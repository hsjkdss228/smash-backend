package kr.megaptera.smash.dtos;

public class RegisterGameFailedErrorDto {
    private final Integer errorCode;

    private final String errorMessage;

    private final Long gameId;

    public RegisterGameFailedErrorDto(Integer errorCode,
                                      String errorMessage,
                                      Long gameId) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.gameId = gameId;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Long getGameId() {
        return gameId;
    }
}
