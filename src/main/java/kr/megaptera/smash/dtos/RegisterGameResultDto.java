package kr.megaptera.smash.dtos;

public class RegisterGameResultDto {
    private final Long gameId;

    public RegisterGameResultDto(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGameId() {
        return gameId;
    }
}
