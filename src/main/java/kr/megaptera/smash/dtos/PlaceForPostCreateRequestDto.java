package kr.megaptera.smash.dtos;

import kr.megaptera.smash.validations.NotBlankGroup;

import javax.validation.constraints.NotBlank;

public class PlaceForPostCreateRequestDto {
    @NotBlank(
        groups = NotBlankGroup.class,
        message = "운동 장소 이름을 입력해주세요.")
    private String name;

    private String address;

    private Boolean isRegisteredPlace;

    public PlaceForPostCreateRequestDto() {

    }

    public PlaceForPostCreateRequestDto(String name,
                                        String address,
                                        Boolean isRegisteredPlace) {
        this.name = name;
        this.address = address;
        this.isRegisteredPlace = isRegisteredPlace;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getIsRegisteredPlace() {
        return isRegisteredPlace;
    }
}
