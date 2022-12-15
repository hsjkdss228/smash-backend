package kr.megaptera.smash.dtos;

public class RegisterProcessingDto {
    private final Long registerId;

    private final UserInPostDetailDto userInformation;

    public RegisterProcessingDto(Long registerId,
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
