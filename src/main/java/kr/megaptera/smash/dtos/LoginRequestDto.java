package kr.megaptera.smash.dtos;

import javax.validation.constraints.NotBlank;

public class LoginRequestDto {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String identifier;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public LoginRequestDto() {

    }

    public LoginRequestDto(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getPassword() {
        return password;
    }
}
