package kr.megaptera.smash.dtos;

import java.util.List;

public class PostDetailDto {
    private final Long id;

    private final Long hits;

    private final String authorName;

    private final String authorPhoneNumber;

    private final String authorProfileImageUrl;

    private final Double authorMannerScore;

    private final String detail;

    private final List<String> imageUrls;

    private final Boolean isAuthor;

    public PostDetailDto(Long id,
                         Long hits,
                         String authorName,
                         String authorPhoneNumber,
                         String authorProfileImageUrl,
                         Double authorMannerScore,
                         String detail,
                         List<String> imageUrls,
                         Boolean isAuthor
    ) {
        this.id = id;
        this.hits = hits;
        this.authorName = authorName;
        this.authorPhoneNumber = authorPhoneNumber;
        this.authorProfileImageUrl = authorProfileImageUrl;
        this.authorMannerScore = authorMannerScore;
        this.detail = detail;
        this.imageUrls = imageUrls;
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

    public String getAuthorProfileImageUrl() {
        return authorProfileImageUrl;
    }

    public Double getAuthorMannerScore() {
        return authorMannerScore;
    }

    public String getDetail() {
        return detail;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public Boolean getIsAuthor() {
        return isAuthor;
    }
}
