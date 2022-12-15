package kr.megaptera.smash.dtos;

import kr.megaptera.smash.validations.NotBlankGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class GameForPostCreateRequestDto {
    @NotBlank(
        groups = NotBlankGroup.class,
        message = "운동 날짜를 입력해주세요.")
    private String date;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "시작시간 오전/오후 구분을 입력해주세요.")
    private String startTimeAmPm;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "시작 시간을 입력해주세요.")
    private String startHour;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "시작 분을 입력해주세요.")
    private String startMinute;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "종료시간 오전/오후 구분을 입력해주세요.")
    private String endTimeAmPm;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "종료 시간을 입력해주세요.")
    private String endHour;

    @NotBlank(
        groups = NotBlankGroup.class,
        message = "종료 분을 입력해주세요.")
    private String endMinute;

    @NotNull(
        groups = NotBlankGroup.class,
        message = "사용자 수를 입력해주세요.")
    private Integer targetMemberCount;

    public GameForPostCreateRequestDto() {

    }

    public GameForPostCreateRequestDto(String date,
                                       String startTimeAmPm,
                                       String startHour,
                                       String startMinute,
                                       String endTimeAmPm,
                                       String endHour,
                                       String endMinute,
                                       Integer targetMemberCount
    ) {
        this.date = date;
        this.startTimeAmPm = startTimeAmPm;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endTimeAmPm = endTimeAmPm;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.targetMemberCount = targetMemberCount;
    }

    public String getDate() {
        return date;
    }

    public String getStartTimeAmPm() {
        return startTimeAmPm;
    }

    public String getStartHour() {
        return startHour;
    }

    public String getStartMinute() {
        return startMinute;
    }

    public String getEndTimeAmPm() {
        return endTimeAmPm;
    }

    public String getEndHour() {
        return endHour;
    }

    public String getEndMinute() {
        return endMinute;
    }

    public Integer getTargetMemberCount() {
        return targetMemberCount;
    }
}
