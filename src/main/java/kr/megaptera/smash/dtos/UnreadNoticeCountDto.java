package kr.megaptera.smash.dtos;

public class UnreadNoticeCountDto {
    private final Long count;

    public UnreadNoticeCountDto(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }
}
