package kr.megaptera.smash.dtos;

public class NoticeDto {
    private final Long id;

    private final String status;

    private final String createdAt;

    private final String title;

    private final String detail;

    public NoticeDto(Long id,
                     String status,
                     String createdAt,
                     String title,
                     String detail
    ) {
        this.id = id;
        this.status = status;
        this.createdAt = createdAt;
        this.title = title;
        this.detail = detail;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }
}
