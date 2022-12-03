package kr.megaptera.smash.dtos;

import java.util.List;

public class NoticesDto {
    private final List<NoticeListDto> notices;

    public NoticesDto(List<NoticeListDto> notices) {
        this.notices = notices;
    }

    public List<NoticeListDto> getNotices() {
        return notices;
    }
}
