package kr.megaptera.smash.dtos;

import kr.megaptera.smash.validations.NotBlankGroup;

import javax.validation.constraints.NotBlank;

public class ExerciseForPostCreateRequestDto {
    @NotBlank(
        groups = NotBlankGroup.class,
        message = "운동을 입력해주세요.")
    private String name;

    public ExerciseForPostCreateRequestDto() {

    }

    public ExerciseForPostCreateRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
