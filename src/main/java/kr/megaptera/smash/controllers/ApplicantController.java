package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.RegistersProcessingDto;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.services.GetProcessingRegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
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
        return getProcessingRegisterService.findProcessingRegisters(targetGameId);
    }

    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFound() {
        return "User Not Found";
    }
}
