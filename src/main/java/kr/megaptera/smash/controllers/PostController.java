package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.CreatePostAndGameResultDto;
import kr.megaptera.smash.dtos.CreatePostFailedErrorDto;
import kr.megaptera.smash.dtos.PostAndGameRequestDto;
import kr.megaptera.smash.dtos.PostDetailDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.exceptions.CreatePostFailed;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.UserIsNotAuthor;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.services.CreatePostService;
import kr.megaptera.smash.services.DeletePostService;
import kr.megaptera.smash.services.GetPostService;
import kr.megaptera.smash.services.GetPostsService;
import kr.megaptera.smash.validations.ValidationSequence;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {
    private final GetPostsService getPostsService;
    private final GetPostService getPostService;
    private final CreatePostService createPostService;
    private final DeletePostService deletePostService;

    public PostController(GetPostsService getPostsService,
                          GetPostService getPostService,
                          CreatePostService createPostService,
                          DeletePostService deletePostService) {
        this.getPostsService = getPostsService;
        this.getPostService = getPostService;
        this.createPostService = createPostService;
        this.deletePostService = deletePostService;
    }

    @GetMapping
    public PostsDto posts(
        @RequestAttribute("userId") Long currentUserId
    ) {
        return getPostsService.findAll(currentUserId);
    }

    @GetMapping("{postId}")
    public PostDetailDto post(
        @RequestAttribute("userId") Long currentUserId,
        @PathVariable("postId") Long targetPostId
    ) {
        return getPostService.findTargetPost(currentUserId, targetPostId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePostAndGameResultDto createPost(
        @RequestAttribute("userId") Long accessedUserId,
        @Validated(value = {ValidationSequence.class})
        @RequestBody PostAndGameRequestDto postAndGameRequestDto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();

            throw new CreatePostFailed(errorMessages);
        }

        return createPostService.createPost(
            accessedUserId,
            postAndGameRequestDto.getGameExercise(),
            postAndGameRequestDto.getGameDate(),
            postAndGameRequestDto.getGameStartTimeAmPm(),
            postAndGameRequestDto.getGameStartHour(),
            postAndGameRequestDto.getGameStartMinute(),
            postAndGameRequestDto.getGameEndTimeAmPm(),
            postAndGameRequestDto.getGameEndHour(),
            postAndGameRequestDto.getGameEndMinute(),
            postAndGameRequestDto.getGamePlace(),
            postAndGameRequestDto.getGameTargetMemberCount(),
            postAndGameRequestDto.getPostDetail()
        );
    }

    @DeleteMapping("{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
        @RequestAttribute("userId") Long accessedUserId,
        @PathVariable("postId") Long targetPostId
    ) {
        deletePostService.deletePost(accessedUserId, targetPostId);
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

    @ExceptionHandler(CreatePostFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CreatePostFailedErrorDto createPostFailed(CreatePostFailed exception) {
        List<String> errorMessages = exception.errorMessages()
            .stream()
            .toList();

        return new CreatePostFailedErrorDto(errorMessages);
    }

    @ExceptionHandler(UserIsNotAuthor.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userIsNotAuthor() {
        return "User Is Not Author";
    }
}
