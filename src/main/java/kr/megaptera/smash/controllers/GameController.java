package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.GameDetailDto;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.services.GetGameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("games")
public class GameController {
    private final GetGameService getGameService;

    public GameController(GetGameService getGameService) {
        this.getGameService = getGameService;
    }

    @GetMapping("posts/{postId}")
    public GameDetailDto game(
        @RequestAttribute(value = "userId", required = false) Long currentUserId,
        @PathVariable("postId") Long targetPostId
    ) {
        return getGameService.findTargetGame(currentUserId, targetPostId);
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
}
