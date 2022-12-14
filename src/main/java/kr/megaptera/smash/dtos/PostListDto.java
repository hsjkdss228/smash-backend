package kr.megaptera.smash.dtos;

public class PostListDto {
    private final Long id;

    private final Long hits;

    private final String thumbnailImageUrl;

    private final Boolean isAuthor;

    private final GameInPostListDto game;

    private final PlaceInPostListDto place;

    public PostListDto(Long id,
                       Long hits,
                       String thumbnailImageUrl,
                       Boolean isAuthor,
                       GameInPostListDto game,
                       PlaceInPostListDto place
    ) {
        this.id = id;
        this.hits = hits;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.isAuthor = isAuthor;
        this.game = game;
        this.place = place;
    }

    public Long getId() {
        return id;
    }

    public Long getHits() {
        return hits;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public Boolean getIsAuthor() {
        return isAuthor;
    }

    public GameInPostListDto getGame() {
        return game;
    }

    public PlaceInPostListDto getPlace() {
        return place;
    }
}
