package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.dtos.PostsFailedErrorDto;
import kr.megaptera.smash.exceptions.PostsFailed;
import kr.megaptera.smash.services.GetPostsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
    private final GetPostsService getPostsService;

    public PostController(GetPostsService getPostsService) {
        this.getPostsService = getPostsService;
    }

    @GetMapping("/posts")
    public PostsDto posts(
        @RequestAttribute("userId") Long accessedUserId
    ) {
        return getPostsService.findAll(accessedUserId);
    }

    @ExceptionHandler(PostsFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public PostsFailedErrorDto postsFailed(PostsFailed exception) {
        return new PostsFailedErrorDto(exception.getMessage());
    }
}
