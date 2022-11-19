package kr.megaptera.smash.dtos;

import java.util.List;

public class ApplicantsDetailDto {
    private final List<ApplicantDetailDto> applicantDetailDtos;

    public ApplicantsDetailDto(List<ApplicantDetailDto> applicantDetailDtos) {
        this.applicantDetailDtos = applicantDetailDtos;
    }

    public List<ApplicantDetailDto> getApplicantDetailDtos() {
        return applicantDetailDtos;
    }
}
