package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.RegisterGameFailedErrorDto;
import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.RegisterGameFailed;
import kr.megaptera.smash.services.PostRegisterGameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("registers")
public class RegisterController {
    private static final Integer GAME_NOT_FOUND = 100;
    private static final Integer ALREADY_REGISTERED_GAME = 101;
    private static final Integer USER_NOT_FOUND = 102;
    private static final Integer DEFAULT = 103;

    private final PostRegisterGameService postRegisterGameService;

    public RegisterController(PostRegisterGameService postRegisterGameService) {
        this.postRegisterGameService = postRegisterGameService;
    }

    @PostMapping("games/{gameId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterGameResultDto registerGame(
        @RequestAttribute("userId") Long accessedUserId,
        @PathVariable("gameId") Long gameId
    ) {
        return postRegisterGameService.registerGame(gameId, accessedUserId);
    }

    @ExceptionHandler(RegisterGameFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RegisterGameFailedErrorDto registerGameFailed(RegisterGameFailed exception) {
        Integer errorCode = setCodeFromMessage(exception.getMessage());
        String errorMessage = errorCode.equals(DEFAULT)
            ? "알 수 없는 에러입니다."
            : exception.getMessage();
        return new RegisterGameFailedErrorDto(errorCode, errorMessage);
    }

    private Integer setCodeFromMessage(String errorMessage) {
        return switch (errorMessage) {
            case "주어진 게임 번호에 해당하는 게임을 찾을 수 없습니다." -> GAME_NOT_FOUND;
            case "이미 신청이 완료된 운동입니다." -> ALREADY_REGISTERED_GAME;
            case "주어진 사용자 번호에 해당하는 사용자를 찾을 수 없습니다." -> USER_NOT_FOUND;
            default -> DEFAULT;
        };
    }

}
