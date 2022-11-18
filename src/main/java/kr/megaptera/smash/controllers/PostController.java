package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.PostDetailDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.dtos.PostsFailedErrorDto;
import kr.megaptera.smash.exceptions.PostsFailed;
import kr.megaptera.smash.services.GetPostService;
import kr.megaptera.smash.services.GetPostsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final GetPostsService getPostsService;
    private final GetPostService getPostService;

    public PostController(GetPostsService getPostsService,
                          GetPostService getPostService) {
        this.getPostsService = getPostsService;
        this.getPostService = getPostService;
    }

    @GetMapping
    public PostsDto posts(
        @RequestAttribute("userId") Long accessedUserId
    ) {
        return getPostsService.findAll(accessedUserId);
    }

    @GetMapping("/{postId}")
    public PostDetailDto post(
        @RequestAttribute("userId") Long accessedUserId,
        @PathVariable("postId") Long targetPostId
    ) {
        return getPostService.findTargetPost(accessedUserId, targetPostId);
    }

    @ExceptionHandler(PostsFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public PostsFailedErrorDto postsFailed(PostsFailed exception) {
        return new PostsFailedErrorDto(exception.getMessage());
    }
}
