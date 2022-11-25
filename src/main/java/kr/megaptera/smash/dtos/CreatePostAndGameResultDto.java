package kr.megaptera.smash.dtos;

public class CreatePostAndGameResultDto {
    private final Long postId;

    public CreatePostAndGameResultDto(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }
}
