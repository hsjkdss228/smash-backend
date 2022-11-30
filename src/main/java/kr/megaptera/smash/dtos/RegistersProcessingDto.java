package kr.megaptera.smash.dtos;

import java.util.List;

public class RegistersProcessingDto {
    private final List<RegisterProcessingDto> applicants;

    public RegistersProcessingDto(List<RegisterProcessingDto> applicants) {
        this.applicants = applicants;
    }

    public List<RegisterProcessingDto> getApplicants() {
        return applicants;
    }
}
