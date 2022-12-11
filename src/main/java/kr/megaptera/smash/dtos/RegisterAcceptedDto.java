package kr.megaptera.smash.dtos;

public class RegisterAcceptedDto {
    private final Long id;

    private final String name;

    private final String gender;

    private final String phoneNumber;

    private final String profileImageUrl;

    private final Double mannerScore;

    public RegisterAcceptedDto(Long id,
                               String name,
                               String gender,
                               String phoneNumber,
                               String profileImageUrl,
                               Double mannerScore
    ) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.mannerScore = mannerScore;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Double getMannerScore() {
        return mannerScore;
    }
}
