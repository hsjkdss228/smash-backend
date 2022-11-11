package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.services.PostRegisterGameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("registers")
public class RegisterController {
    private final PostRegisterGameService postRegisterGameService;

    public RegisterController(PostRegisterGameService postRegisterGameService) {
        this.postRegisterGameService = postRegisterGameService;
    }

    @PostMapping("games/{gameId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterGameResultDto registerGame(
        @RequestAttribute("userId") Long userId,
        @PathVariable("gameId") Long gameId
    ) {
        return postRegisterGameService.registerGame(gameId, userId);
    }
}
