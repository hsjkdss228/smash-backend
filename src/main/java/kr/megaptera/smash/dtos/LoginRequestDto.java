package kr.megaptera.smash.dtos;

import javax.validation.constraints.NotNull;

public class LoginRequestDto {
    @NotNull(message = "user Id를 입력해주세요. (200)")
    private Long userId;

    public LoginRequestDto() {

    }

    public LoginRequestDto(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
