package kr.megaptera.smash.dtos;

import kr.megaptera.smash.validations.NotBlankGroup;

import javax.validation.constraints.NotBlank;

public class PlaceForPostCreateRequestDto {
    @NotBlank(
        groups = NotBlankGroup.class,
        message = "운동 장소 이름을 입력해주세요.")
    private String name;

    public PlaceForPostCreateRequestDto() {

    }

    public PlaceForPostCreateRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
