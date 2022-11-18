package kr.megaptera.smash.dtos;

import java.util.List;

public class MembersDetailDto {
    private final List<MemberDetailDto> members;

    public MembersDetailDto(List<MemberDetailDto> members) {
        this.members = members;
    }

    public List<MemberDetailDto> getMembers() {
        return members;
    }
}
