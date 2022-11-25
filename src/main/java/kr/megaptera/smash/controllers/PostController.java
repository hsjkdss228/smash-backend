package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.CreatePostAndGameResultDto;
import kr.megaptera.smash.dtos.PostAndGameRequestDto;
import kr.megaptera.smash.dtos.PostDetailDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.dtos.PostsFailedErrorDto;
import kr.megaptera.smash.exceptions.PostsFailed;
import kr.megaptera.smash.services.CreatePostService;
import kr.megaptera.smash.services.GetPostService;
import kr.megaptera.smash.services.GetPostsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("posts")
public class PostController {
    private final GetPostsService getPostsService;
    private final GetPostService getPostService;
    private final CreatePostService createPostService;

    public PostController(GetPostsService getPostsService,
                          GetPostService getPostService,
                          CreatePostService createPostService) {
        this.getPostsService = getPostsService;
        this.getPostService = getPostService;
        this.createPostService = createPostService;
    }

    @GetMapping
    public PostsDto posts(
        @RequestAttribute("userId") Long accessedUserId
    ) {
        return getPostsService.findAll(accessedUserId);
    }

    @GetMapping("{postId}")
    public PostDetailDto post(
        @RequestAttribute("userId") Long accessedUserId,
        @PathVariable("postId") Long targetPostId
    ) {
        return getPostService.findTargetPost(accessedUserId, targetPostId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePostAndGameResultDto createPost(
        @RequestAttribute("userId") Long accessedUserId,
        @RequestBody PostAndGameRequestDto postAndGameRequestDto
    ) {
        System.out.println("*".repeat(10));
        System.out.println(postAndGameRequestDto.getGameDate());
        System.out.println("*".repeat(10));

        return createPostService.createPost(
            accessedUserId,
            postAndGameRequestDto.getGameExercise(),
            postAndGameRequestDto.getGameDate(),
            postAndGameRequestDto.getGameTime(),
            postAndGameRequestDto.getGamePlace(),
            postAndGameRequestDto.getGameTargetMemberCount(),
            postAndGameRequestDto.getPostDetail()
        );
    }

    @ExceptionHandler(PostsFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public PostsFailedErrorDto postsFailed(PostsFailed exception) {
        return new PostsFailedErrorDto(exception.getMessage());
    }
}
