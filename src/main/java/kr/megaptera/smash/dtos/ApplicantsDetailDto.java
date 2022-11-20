package kr.megaptera.smash.dtos;

import java.util.List;

public class ApplicantsDetailDto {
    private final List<ApplicantDetailDto> applicants;

    public ApplicantsDetailDto(List<ApplicantDetailDto> applicants) {
        this.applicants = applicants;
    }

    public List<ApplicantDetailDto> getApplicants() {
        return applicants;
    }
}
