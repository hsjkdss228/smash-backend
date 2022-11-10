package kr.megaptera.smash.dtos;

public class PostListDto {
    private final Long id;

    private final Long hits;

    private final GameInPostListDto game;

    public PostListDto(Long id, Long hits, GameInPostListDto game) {
        this.id = id;
        this.hits = hits;
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
}
