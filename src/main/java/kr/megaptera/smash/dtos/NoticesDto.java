package kr.megaptera.smash.dtos;

import java.util.List;

public class NoticesDto {
    private final List<NoticeDto> notices;

    public NoticesDto(List<NoticeDto> notices) {
        this.notices = notices;
    }

    public List<NoticeDto> getNotices() {
        return notices;
    }
}
