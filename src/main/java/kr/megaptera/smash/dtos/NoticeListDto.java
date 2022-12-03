package kr.megaptera.smash.dtos;

public class NoticeListDto {
    private final Long id;

    private final String createdAt;

    private final String title;

    public NoticeListDto(Long id,
                         String createdAt,
                         String title
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }
}
