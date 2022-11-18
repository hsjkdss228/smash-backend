package kr.megaptera.smash.dtos;

public class PostDetailDto {
    private final Long id;

    private final Long hits;

    private final String authorName;

    private final String authorPhoneNumber;

    private final String detail;

    private final Boolean isAuthor;

    public PostDetailDto(Long id,
                         Long hits,
                         String authorName,
                         String authorPhoneNumber,
                         String detail,
                         Boolean isAuthor
    ) {
        this.id = id;
        this.hits = hits;
        this.authorName = authorName;
        this.authorPhoneNumber = authorPhoneNumber;
        this.detail = detail;
        this.isAuthor = isAuthor;
    }

    public Long getId() {
        return id;
    }

    public Long getHits() {
        return hits;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorPhoneNumber() {
        return authorPhoneNumber;
    }

    public String getDetail() {
        return detail;
    }

    public Boolean getIsAuthor() {
        return isAuthor;
    }
}
