package kr.megaptera.smash.controllers;

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

import kr.megaptera.smash.dtos.ApplicantsDetailDto;
import kr.megaptera.smash.dtos.MembersDetailDto;
import kr.megaptera.smash.dtos.RegisterGameFailedErrorDto;
import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.AlreadyGameJoined;
import kr.megaptera.smash.exceptions.GameIsFull;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.RegisterGameFailed;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.services.GetAcceptedRegisterService;
import kr.megaptera.smash.services.GetProcessingRegisterService;
import kr.megaptera.smash.services.JoinGameService;
import kr.megaptera.smash.services.PatchRegisterToAcceptedService;
import kr.megaptera.smash.services.PatchRegisterToCanceledService;
import kr.megaptera.smash.services.PatchRegisterToRejectedService;

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
    private final JoinGameService joinGameService;
    private final PatchRegisterToCanceledService patchRegisterToCanceledService;
    private final PatchRegisterToAcceptedService patchRegisterToAcceptedService;
    private final PatchRegisterToRejectedService patchRegisterToRejectedService;

    // -> 이렇게 많은 필드가 있다는 건 뭔가 잘못되고 있다는 아주 강력한 신호입니다.

    public RegisterController(GetAcceptedRegisterService getAcceptedRegisterService,
                              GetProcessingRegisterService getProcessingRegisterService,
                              JoinGameService postRegisterGameService,
                              PatchRegisterToCanceledService patchRegisterToCanceledService,
                              PatchRegisterToAcceptedService patchRegisterToAcceptedService,
                              PatchRegisterToRejectedService patchRegisterToRejectedService) {
        this.getAcceptedRegisterService = getAcceptedRegisterService;
        this.getProcessingRegisterService = getProcessingRegisterService;
        this.joinGameService = postRegisterGameService;
        this.patchRegisterToCanceledService = patchRegisterToCanceledService;
        this.patchRegisterToAcceptedService = patchRegisterToAcceptedService;
        this.patchRegisterToRejectedService = patchRegisterToRejectedService;
    }

    // 잘못된 URL. /games/{gameId}/members -> MemberController에서 처리.
    @GetMapping("/members/games/{gameId}")
    public MembersDetailDto members(
            @PathVariable("gameId") Long targetGameId
    ) {
        return getAcceptedRegisterService.findMembers(targetGameId);
    }

    // 잘못된 URL. /games/{gameId}/applicants -> ApplicantController에서 처리.
    @GetMapping("/applicants/games/{gameId}")
    public ApplicantsDetailDto applicants(
            @PathVariable("gameId") Long targetGameId
    ) {
        return getProcessingRegisterService.findApplicants(targetGameId);
    }

    // 잘못된 URL. /games/{gameId}/registers가 돼야 합니다.
    @PostMapping("games/{gameId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterGameResultDto registerGame(
            @RequestAttribute("userId") Long accessedUserId,
            @PathVariable("gameId") Long gameId
    ) {
        return joinGameService.joinGame(gameId, accessedUserId);
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

    @ExceptionHandler(GameNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String gameNotFound() {
        return "Game not found";
    }

    @ExceptionHandler(AlreadyGameJoined.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String alreadyGameJoined() {
        return "...";
    }

    @ExceptionHandler(GameIsFull.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String gameIsFull() {
        return "...";
    }

    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userNotFound() {
        return "...";
        // 강의와 다르게 해서 여기가 고통스럽습니다.
        // https://github.com/megaptera-kr/web-01-spring/blob/main/week11/Authentication/20220829-%ED%99%A9%EC%9D%B8%EC%9A%B0/user/src/main/java/com/inu/user/interceptors/AuthenticationInterceptor.java
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

    // 뭔가 잘못되고 있다는 아주 강력한 신호입니다.
    // 1. 에러 메시지로 뭔가를 하는 게 틀렸고,
    // 2. 에러 코드를 만드는 것도 틀렸습니다.
    // 3. 심지어 setter라뇨?! -> 근데 setter도 아닙니다.
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
