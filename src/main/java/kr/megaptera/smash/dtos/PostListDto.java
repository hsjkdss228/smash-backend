package kr.megaptera.smash.dtos;

public class PostListDto {
    private final Long id;

    private final Long hits;

    private final Boolean isAuthor;

    private final GameInPostListDto game;

    public PostListDto(Long id,
                       Long hits,
                       Boolean isAuthor,
                       GameInPostListDto game) {
        this.id = id;
        this.hits = hits;
        this.isAuthor = isAuthor;
        this.game = game;
    }

    public Long getId() {
        return id;
    }

    public Long getHits() {
        return hits;
    }

    public GameInPostListDto getGame() {
        return game;
    }

    public Boolean getIsAuthor() {
        return isAuthor;
    }
}
