package kr.megaptera.smash.dtos;

import kr.megaptera.smash.validations.NotBlankGroup;
import kr.megaptera.smash.validations.PatternMatchGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
        message = "운동 시간을 입력해주세요.")
    @Pattern(
        groups = PatternMatchGroup.class,
        regexp = "^(?=.*\\d)\\d{1,2},\\d{1,2},\\d{1,2},\\d{1,2}$",
        message = "입력하지 않은 운동 시간이 있습니다.")
    private String gameTime;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "운동 장소 이름을 입력해주세요.")
    private String gamePlace;

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
                                 String gameTime,
                                 String gamePlace,
                                 Integer gameTargetMemberCount,
                                 String postDetail) {
        this.gameExercise = gameExercise;
        this.gameDate = gameDate;
        this.gameTime = gameTime;
        this.gamePlace = gamePlace;
        this.gameTargetMemberCount = gameTargetMemberCount;
        this.postDetail = postDetail;
    }

    public String getGameExercise() {
        return gameExercise;
    }

    public String getGameDate() {
        return gameDate;
    }

    public String getGameTime() {
        return gameTime;
    }

    public String getGamePlace() {
        return gamePlace;
    }

    public Integer getGameTargetMemberCount() {
        return gameTargetMemberCount;
    }

    public String getPostDetail() {
        return postDetail;
    }
}
