package kr.megaptera.smash.dtos;

import kr.megaptera.smash.validations.NotBlankGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PostAndGameRequestDto {
    @NotBlank(
        groups = NotBlankGroup.class,
        message = "운동을 입력해주세요.")
    private String gameExercise;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "운동 날짜를 입력해주세요.")
    private String gameDate;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "시작시간 오전/오후 구분을 입력해주세요.")
    private String gameStartTimeAmPm;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "시작 시간을 입력해주세요.")
    private String gameStartHour;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "시작 분을 입력해주세요.")
    private String gameStartMinute;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "종료시간 오전/오후 구분을 입력해주세요.")
    private String gameEndTimeAmPm;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "종료 시간을 입력해주세요.")
    private String gameEndHour;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "종료 분을 입력해주세요.")
    private String gameEndMinute;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "운동 장소 이름을 입력해주세요.")
    private String placeName;

    @NotNull(
        groups = NotBlankGroup.class,
        message = "사용자 수를 입력해주세요.")
    private Integer gameTargetMemberCount;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "게시물 상세 내용을 입력해주세요.")
    private String postDetail;

    public PostAndGameRequestDto() {

    }

    public PostAndGameRequestDto(String gameExercise,
                                 String gameDate,
                                 String gameStartTimeAmPm,
                                 String gameStartHour,
                                 String gameStartMinute,
                                 String gameEndTimeAmPm,
                                 String gameEndHour,
                                 String gameEndMinute,
                                 String placeName,
                                 Integer gameTargetMemberCount,
                                 String postDetail
    ) {
        this.gameExercise = gameExercise;
        this.gameDate = gameDate;
        this.gameStartTimeAmPm = gameStartTimeAmPm;
        this.gameStartHour = gameStartHour;
        this.gameStartMinute = gameStartMinute;
        this.gameEndTimeAmPm = gameEndTimeAmPm;
        this.gameEndHour = gameEndHour;
        this.gameEndMinute = gameEndMinute;
        this.placeName = placeName;
        this.gameTargetMemberCount = gameTargetMemberCount;
        this.postDetail = postDetail;
    }

    public String getGameExercise() {
        return gameExercise;
    }

    public String getGameDate() {
        return gameDate;
    }

    public String getGameStartTimeAmPm() {
        return gameStartTimeAmPm;
    }

    public String getGameStartHour() {
        return gameStartHour;
    }

    public String getGameStartMinute() {
        return gameStartMinute;
    }

    public String getGameEndTimeAmPm() {
        return gameEndTimeAmPm;
    }

    public String getGameEndHour() {
        return gameEndHour;
    }

    public String getGameEndMinute() {
        return gameEndMinute;
    }

    public String getPlaceName() {
        return placeName;
    }

    public Integer getGameTargetMemberCount() {
        return gameTargetMemberCount;
    }

    public String getPostDetail() {
        return postDetail;
    }
}
