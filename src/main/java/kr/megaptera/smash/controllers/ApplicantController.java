package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.RegistersProcessingDto;
import kr.megaptera.smash.services.GetProcessingRegisterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicantController {
    private final GetProcessingRegisterService getProcessingRegisterService;

    public ApplicantController(GetProcessingRegisterService getProcessingRegisterService) {
        this.getProcessingRegisterService = getProcessingRegisterService;
    }

    @GetMapping("games/{gameId}/applicants")
    public RegistersProcessingDto applicants(
        @PathVariable("gameId") Long targetGameId
    ) {
        return getProcessingRegisterService.findApplicants(targetGameId);
    }
}
