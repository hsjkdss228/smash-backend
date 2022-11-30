package kr.megaptera.smash.dtos;

public class RegisterProcessingDto {
    private final Long id;

    private final String name;

    private final String gender;

    private final String phoneNumber;

    public RegisterProcessingDto(Long id,
                                 String name,
                                 String gender,
                                 String phoneNumber) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
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
}
