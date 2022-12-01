package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.AlreadyJoinedGame;
import kr.megaptera.smash.exceptions.GameIsFull;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.services.PatchRegisterToAcceptedService;
import kr.megaptera.smash.services.PatchRegisterToCanceledService;
import kr.megaptera.smash.services.PatchRegisterToRejectedService;
import kr.megaptera.smash.services.JoinGameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    private final JoinGameService joinGameService;
    private final PatchRegisterToCanceledService patchRegisterToCanceledService;
    private final PatchRegisterToAcceptedService patchRegisterToAcceptedService;
    private final PatchRegisterToRejectedService patchRegisterToRejectedService;

    public RegisterController(JoinGameService joinGameService,
                              PatchRegisterToCanceledService patchRegisterToCanceledService,
                              PatchRegisterToAcceptedService patchRegisterToAcceptedService,
                              PatchRegisterToRejectedService patchRegisterToRejectedService) {
        this.joinGameService = joinGameService;
        this.patchRegisterToCanceledService = patchRegisterToCanceledService;
        this.patchRegisterToAcceptedService = patchRegisterToAcceptedService;
        this.patchRegisterToRejectedService = patchRegisterToRejectedService;
    }

    @PostMapping("games/{gameId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterGameResultDto registerGame(
        @RequestAttribute("userId") Long currentUserId,
        @PathVariable("gameId") Long gameId
    ) {
        return joinGameService.joinGame(gameId, currentUserId);
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

    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFound() {
        return "User Not Found";
    }

    @ExceptionHandler(GameNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String gameNotFound() {
        return "Game Not Found";
    }

    @ExceptionHandler(AlreadyJoinedGame.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String alreadyJoinedGame() {
        return "Already Joined Game";
    }

    @ExceptionHandler(GameIsFull.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String gameIsFull() {
        return "Game Is Full";
    }
}
