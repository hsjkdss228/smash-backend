package kr.megaptera.smash.dtos;

import kr.megaptera.smash.validations.NotBlankGroup;

import javax.validation.constraints.NotBlank;

public class PostForPostCreateRequestDto {
    @NotBlank(
        groups = NotBlankGroup.class,
        message = "게시글 상세 내용을 입력해주세요.")
    private String detail;

    public PostForPostCreateRequestDto() {

    }

    public PostForPostCreateRequestDto(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}
