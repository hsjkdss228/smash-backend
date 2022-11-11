package kr.megaptera.smash.dtos;

public class GameInPostListDto {
    private final Long id;

    private final String type;

    private final String date;

    private final String place;

    private final Integer currentMemberCount;

    private final Integer targetMemberCount;

    private final Boolean isRegistered;

    public GameInPostListDto(Long id,
                             String type,
                             String date,
                             String place,
                             Integer currentMemberCount,
                             Integer targetMemberCount,
                             Boolean isRegistered) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.place = place;
        this.currentMemberCount = currentMemberCount;
        this.targetMemberCount = targetMemberCount;
        this.isRegistered = isRegistered;
    }

    public Long getId() {
        return id;
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

    public Boolean getIsRegistered() {
        return isRegistered;
    }
}
