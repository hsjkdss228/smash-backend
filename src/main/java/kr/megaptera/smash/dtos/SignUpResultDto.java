package kr.megaptera.smash.dtos;

public class SignUpResultDto {
    private final String enrolledName;

    public SignUpResultDto(String enrolledName) {
        this.enrolledName = enrolledName;
    }

    public String getEnrolledName() {
        return enrolledName;
    }
}
