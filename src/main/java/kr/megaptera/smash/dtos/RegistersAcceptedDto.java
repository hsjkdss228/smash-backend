package kr.megaptera.smash.dtos;

import java.util.List;

public class RegistersAcceptedDto {
    private final List<RegisterAcceptedDto> members;

    public RegistersAcceptedDto(List<RegisterAcceptedDto> members) {
        this.members = members;
    }

    public List<RegisterAcceptedDto> getMembers() {
        return members;
    }
}
