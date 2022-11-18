package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.GameDetailDto;
import kr.megaptera.smash.services.GetGameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GetGameService getGameService;

    public GameController(GetGameService getGameService) {
        this.getGameService = getGameService;
    }

    @GetMapping("posts/{postId}")
    public GameDetailDto game(
        @RequestAttribute("userId") Long accessedUserId,
        @PathVariable("postId") Long targetPostId
    ) {
        return getGameService.findTargetGame(accessedUserId, targetPostId);
    }
}
