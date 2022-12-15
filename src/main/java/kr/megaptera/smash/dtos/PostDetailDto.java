package kr.megaptera.smash.dtos;

import java.util.List;

public class PostDetailDto {
    private final Long id;

    private final Long hits;

    private final UserInPostDetailDto authorInformation;

    private final String detail;

    private final List<String> imageUrls;

    private final Boolean isAuthor;

    public PostDetailDto(Long id,
                         Long hits,
                         UserInPostDetailDto authorInformation,
                         String detail,
                         List<String> imageUrls,
                         Boolean isAuthor
    ) {
        this.id = id;
        this.hits = hits;
        this.authorInformation = authorInformation;
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

    public UserInPostDetailDto getAuthorInformation() {
        return authorInformation;
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
