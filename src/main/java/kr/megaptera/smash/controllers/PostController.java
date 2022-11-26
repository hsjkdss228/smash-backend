package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.CreatePostAndGameResultDto;
import kr.megaptera.smash.dtos.CreatePostFailedErrorDto;
import kr.megaptera.smash.dtos.PostAndGameRequestDto;
import kr.megaptera.smash.dtos.PostDetailDto;
import kr.megaptera.smash.dtos.PostsDto;
import kr.megaptera.smash.dtos.PostsFailedErrorDto;
import kr.megaptera.smash.exceptions.CreatePostFailed;
import kr.megaptera.smash.exceptions.PostsFailed;
import kr.megaptera.smash.services.CreatePostService;
import kr.megaptera.smash.services.GetPostService;
import kr.megaptera.smash.services.GetPostsService;
import kr.megaptera.smash.validations.ValidationSequence;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("posts")
public class PostController {
    private static final Integer BLANK_GAME_EXERCISE = 100;
    private static final Integer BLANK_GAME_DATE = 101;
    private static final Integer BLANK_GAME_START_AM_PM = 102;
    private static final Integer BLANK_GAME_START_HOUR = 103;
    private static final Integer BLANK_GAME_START_MINUTE = 104;
    private static final Integer BLANK_GAME_END_AM_PM = 105;
    private static final Integer BLANK_GAME_END_HOUR = 106;
    private static final Integer BLANK_GAME_END_MINUTE = 107;
    private static final Integer BLANK_GAME_PLACE = 108;
    private static final Integer NULL_GAME_TARGET_MEMBER_COUNT = 109;
    private static final Integer BLANK_POST_DETAIL = 110;
    private static final Integer USER_NOT_FOUND = 111;
    private static final Integer DEFAULT_ERROR = 112;

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

    @ExceptionHandler(PostsFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public PostsFailedErrorDto postsFailed(PostsFailed exception) {
        return new PostsFailedErrorDto(exception.getMessage());
    }

    @ExceptionHandler(CreatePostFailed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CreatePostFailedErrorDto createPostFailed(CreatePostFailed exception) {
        Map<Integer, String> errorCodeAndMessages
            = exception.errorMessages()
            .stream()
            .collect(Collectors.toMap(
                this::mapToErrorCode,
                errorMessage -> mapToErrorCode(errorMessage).equals(DEFAULT_ERROR)
                    ? "알 수 없는 게시글 작성 오류입니다."
                    : errorMessage
            ));

        return new CreatePostFailedErrorDto(errorCodeAndMessages);
    }

    private Integer mapToErrorCode(String errorMessage) {
        return switch (errorMessage) {
            case "운동을 입력해주세요." -> BLANK_GAME_EXERCISE;
            case "운동 날짜를 입력해주세요." -> BLANK_GAME_DATE;
            case "시작시간 오전/오후 구분을 입력해주세요." -> BLANK_GAME_START_AM_PM;
            case "시작 시간을 입력해주세요." -> BLANK_GAME_START_HOUR;
            case "시작 분을 입력해주세요." -> BLANK_GAME_START_MINUTE;
            case "종료시간 오전/오후 구분을 입력해주세요." -> BLANK_GAME_END_AM_PM;
            case "종료 시간을 입력해주세요." -> BLANK_GAME_END_HOUR;
            case "종료 분을 입력해주세요." -> BLANK_GAME_END_MINUTE;
            case "운동 장소 이름을 입력해주세요." -> BLANK_GAME_PLACE;
            case "사용자 수를 입력해주세요." -> NULL_GAME_TARGET_MEMBER_COUNT;
            case "게시물 상세 내용을 입력해주세요." -> BLANK_POST_DETAIL;
            case "접속한 사용자를 찾을 수 없습니다." -> USER_NOT_FOUND;
            default -> DEFAULT_ERROR;
        };
    }
}
