package kr.megaptera.smash.dtos;

public class LoginResultDto {
    private final String accessToken;

    public LoginResultDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
