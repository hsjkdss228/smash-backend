package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.ApplicantsDetailDto;
import kr.megaptera.smash.dtos.MembersDetailDto;
import kr.megaptera.smash.dtos.RegisterGameFailedErrorDto;
import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.RegisterGameFailed;
import kr.megaptera.smash.services.GetAcceptedRegisterService;
import kr.megaptera.smash.services.GetProcessingRegisterService;
import kr.megaptera.smash.services.PatchRegisterToAcceptedService;
import kr.megaptera.smash.services.PatchRegisterToCanceledService;
import kr.megaptera.smash.services.PatchRegisterToRejectedService;
import kr.megaptera.smash.services.PostRegisterGameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("registers")
public class RegisterController {
    private static final Integer GAME_NOT_FOUND = 100;
    private static final Integer ALREADY_REGISTERED_GAME = 101;
    private static final Integer USER_NOT_FOUND = 102;
    private static final Integer FULLY_PARTICIPANTS = 103;
    private static final Integer DEFAULT = 104;

    private final GetAcceptedRegisterService getAcceptedRegisterService;
    private final GetProcessingRegisterService getProcessingRegisterService;
    private final PostRegisterGameService postRegisterGameService;
    private final PatchRegisterToCanceledService patchRegisterToCanceledService;
    private final PatchRegisterToAcceptedService patchRegisterToAcceptedService;
    private final PatchRegisterToRejectedService patchRegisterToRejectedService;

    public RegisterController(GetAcceptedRegisterService getAcceptedRegisterService,
                              GetProcessingRegisterService getProcessingRegisterService,
                              PostRegisterGameService postRegisterGameService,
                              PatchRegisterToCanceledService patchRegisterToCanceledService,
                              PatchRegisterToAcceptedService patchRegisterToAcceptedService,
                              PatchRegisterToRejectedService patchRegisterToRejectedService) {
        this.getAcceptedRegisterService = getAcceptedRegisterService;
        this.getProcessingRegisterService = getProcessingRegisterService;
        this.postRegisterGameService = postRegisterGameService;
        this.patchRegisterToCanceledService = patchRegisterToCanceledService;
        this.patchRegisterToAcceptedService = patchRegisterToAcceptedService;
        this.patchRegisterToRejectedService = patchRegisterToRejectedService;
    }

    @GetMapping("/members/games/{gameId}")
    public MembersDetailDto members(
        @PathVariable("gameId") Long targetGameId
    ) {
        return getAcceptedRegisterService.findMembers(targetGameId);
    }

    @GetMapping("/applicants/games/{gameId}")
    public ApplicantsDetailDto applicants(
        @PathVariable("gameId") Long targetGameId
    ) {
        return getProcessingRegisterService.findApplicants(targetGameId);
    }

    @PostMapping("games/{gameId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterGameResultDto registerGame(
        @RequestAttribute("userId") Long accessedUserId,
        @PathVariable("gameId") Long gameId
    ) {
        return postRegisterGameService.registerGame(gameId, accessedUserId);
    }

    @PatchMapping("{registerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeRegister(
        @RequestAttribute("userId") Long accessedUserId,
        @PathVariable("registerId") Long registerId,
        @RequestParam(value = "status") String status
    ) {
        switch (status) {
            case "canceled" -> patchRegisterToCanceledService.
                patchRegisterToCanceled(registerId, accessedUserId);
            case "accepted" -> patchRegisterToAcceptedService.
                patchRegisterToAccepted(registerId);
            case "rejected" -> patchRegisterToRejectedService.
                patchRegisterToRejected(registerId);
        }
    }

    @ExceptionHandler(RegisterGameFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RegisterGameFailedErrorDto registerGameFailed(RegisterGameFailed exception) {
        Integer errorCode = setCodeFromMessage(exception.getMessage());
        String errorMessage = errorCode.equals(DEFAULT)
            ? "알 수 없는 에러입니다."
            : exception.getMessage();

        return new RegisterGameFailedErrorDto(
            errorCode,
            errorMessage,
            exception.getGameId()
        );
    }

    private Integer setCodeFromMessage(String errorMessage) {
        return switch (errorMessage) {
            case "주어진 게임 번호에 해당하는 게임을 찾을 수 없습니다." -> GAME_NOT_FOUND;
            case "이미 신청이 완료된 운동입니다." -> ALREADY_REGISTERED_GAME;
            case "주어진 사용자 번호에 해당하는 사용자를 찾을 수 없습니다." -> USER_NOT_FOUND;
            case "참가 정원이 모두 차 참가를 신청할 수 없습니다." -> FULLY_PARTICIPANTS;
            default -> DEFAULT;
        };
    }

}
