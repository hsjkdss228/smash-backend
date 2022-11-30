package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.RegistersAcceptedDto;
import kr.megaptera.smash.services.GetAcceptedRegisterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    private final GetAcceptedRegisterService getAcceptedRegisterService;

    public MemberController(GetAcceptedRegisterService getAcceptedRegisterService) {
        this.getAcceptedRegisterService = getAcceptedRegisterService;
    }

    @GetMapping("games/{gameId}/members")
    public RegistersAcceptedDto members(
        @PathVariable("gameId") Long targetGameId
    ) {
        return getAcceptedRegisterService.findAcceptedRegisters(targetGameId);
    }
}
