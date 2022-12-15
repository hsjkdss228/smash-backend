package kr.megaptera.smash.dtos;

public class RegisterAcceptedDto {
    private final Long registerId;

    private final UserInPostDetailDto userInformation;

    public RegisterAcceptedDto(Long registerId,
                               UserInPostDetailDto userInformation) {
        this.registerId = registerId;
        this.userInformation = userInformation;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public UserInPostDetailDto getUserInformation() {
        return userInformation;
    }
}
