package kr.megaptera.smash.dtos;

public class GameDetailDto {
    private final Long id;

    private final String type;

    private final String date;

    private final String place;

    private final Integer currentMemberCount;

    private final Integer targetMemberCount;

    private final Long registerId;

    private final String registerStatus;

    public GameDetailDto(Long id,
                         String type,
                         String date,
                         String place,
                         Integer currentMemberCount,
                         Integer targetMemberCount,
                         Long registerId,
                         String registerStatus) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.place = place;
        this.currentMemberCount = currentMemberCount;
        this.targetMemberCount = targetMemberCount;
        this.registerId = registerId;
        this.registerStatus = registerStatus;
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

    public Long getRegisterId() {
        return registerId;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }
}
