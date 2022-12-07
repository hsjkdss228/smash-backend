package kr.megaptera.smash.dtos;

import java.util.List;

public class NoticeIdsRequestDto {
    private List<Long> ids;

    public NoticeIdsRequestDto() {

    }

    public NoticeIdsRequestDto(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }
}
