package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.PostNotFoundErrorDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.exceptions.PostNotFound;
import kr.megaptera.smash.services.GetPostsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
    private final GetPostsService getPostsService;

    public PostController(GetPostsService getPostsService) {
        this.getPostsService = getPostsService;
    }

    @GetMapping("/posts")
    public PostsDto posts() {
        return getPostsService.findAll();
    }

    @ExceptionHandler(PostNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public PostNotFoundErrorDto postNotFound(PostNotFound exception) {
        return new PostNotFoundErrorDto(exception.getMessage());
    }
}
