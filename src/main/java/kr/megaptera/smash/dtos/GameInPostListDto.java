package kr.megaptera.smash.dtos;

public class GameInPostListDto {
    private final String type;

    private final String date;

    private final String place;

    private final Integer currentMemberCount;

    private final Integer targetMemberCount;

    public GameInPostListDto(String type,
                             String date,
                             String place,
                             Integer currentMemberCount,
                             Integer targetMemberCount
    ) {
        this.type = type;
        this.date = date;
        this.place = place;
        this.currentMemberCount = currentMemberCount;
        this.targetMemberCount = targetMemberCount;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public Integer getCurrentMemberCount() {
        return currentMemberCount;
    }

    public Integer getTargetMemberCount() {
        return targetMemberCount;
    }
}
