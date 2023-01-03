package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.GameInPostListDto;
import kr.megaptera.smash.dtos.PlaceInPostListDto;
import kr.megaptera.smash.dtos.PostDetailDto;
import kr.megaptera.smash.dtos.PostListDto;
import kr.megaptera.smash.dtos.UserInPostDetailDto;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// TODO: id를 값 객체로 정의하는 경우 GeneratedValue 어노테이션을 사용할 수 없음,
//   다른 모델이 해당 모델의 id를 갖고 있는 경우 때문에 그런 것 같음
//   생성자로 생성될 때만 id 생성기를 사용해서 id를 부여해야 할 것 같은데...

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    @Embedded
    private PostHits hits;

    @Embedded
    private PostDetail detail;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ElementCollection
    private List<PostImage> images = new ArrayList<>();

    private Post() {

    }

    public Post(Long userId,
                PostHits hits,
                PostDetail detail
    ) {
        this.userId = userId;
        this.hits = hits;
        this.detail = detail;
    }

    public Post(Long id,
                Long userId,
                PostHits hits,
                PostDetail detail
    ) {
        this.id = id;
        this.userId = userId;
        this.hits = hits;
        this.detail = detail;
    }

    public Post(Long id,
                Long userId,
                PostHits hits,
                PostDetail detail,
                LocalDateTime createdAt,
                LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.hits = hits;
        this.detail = detail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Post(Long id,
                Long userId,
                PostHits hits,
                PostDetail detail,
                List<PostImage> images,
                LocalDateTime createdAt,
                LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.hits = hits;
        this.detail = detail;
        this.images = images;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long id() {
        return id;
    }

    public Long userId() {
        return userId;
    }

    public PostHits hits() {
        return hits;
    }

    public PostDetail detail() {
        return detail;
    }

    public List<PostImage> images() {
        return images;
    }

    public Boolean hasNoImages() {
        return images.isEmpty();
    }

    public String NoImagesUrl() {
        return "https://user-images.githubusercontent.com/" +
            "50052512/206902802-734cd16b-9625-4354-a7e4-6360a1970afb.png";
    }

    public String thumbnailImageUrl() {
        return images.stream()
            .filter(PostImage::isThumbnailImage)
            .findFirst().get()
            .url();
    }

    public List<String> imagesToUrls() {
        return images.stream()
            .map(PostImage::url)
            .toList();
    }

    public Boolean isAuthor(User user) {
        return user != null && userId.equals(user.id());
    }

    public Boolean isAuthor(Long userId) {
        return this.userId.equals(userId);
    }

    public void addHits() {
        hits = new PostHits(this.hits.value() + 1);
    }

    public static Post fake(Long postId) {
        return new Post(
            postId,
            1L,
            new PostHits(1234L),
            new PostDetail("게시글 내용"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public static Post fake(Long postId, Long userId) {
        return new Post(
            postId,
            userId,
            new PostHits(1234L),
            new PostDetail("게시글 내용"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public static Post fake(String detail) {
        Long postId = 1L;
        Long userId = 1L;
        return new Post(
            postId,
            userId,
            new PostHits(1234L),
            new PostDetail(detail),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public static Post fakeWithImages(List<PostImage> images) {
        Long postId = 1L;
        Long userId = 1L;
        return new Post(
            postId,
            userId,
            new PostHits(1234L),
            new PostDetail("게시글 내용"),
            images,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    public static List<Post> fakes(long generationCount) {
        List<Post> posts = new ArrayList<>();
        for (long i = 1; i <= generationCount; i += 1) {
            Long postId = i;
            Post post = new Post(
                postId,
                1L,
                new PostHits(i),
                new PostDetail("게시글 상세 정보 내용 " + i),
                LocalDateTime.now(),
                LocalDateTime.now()
            );
            posts.add(post);
        }
        return posts;
    }

    public PostListDto toListDto(Boolean isAuthor,
                                 GameInPostListDto gameInPostListDto,
                                 PlaceInPostListDto placeInPostListDto) {
        return new PostListDto(
            id,
            hits.value(),
            hasNoImages() ? NoImagesUrl() : thumbnailImageUrl(),
            isAuthor,
            gameInPostListDto,
            placeInPostListDto
        );
    }

    public PostDetailDto toDetailDto(UserInPostDetailDto userInPostDetailDto,
                                     Boolean isAuthor) {
        return new PostDetailDto(
            id,
            hits.value(),
            userInPostDetailDto,
            detail.value(),
            imagesToUrls(),
            isAuthor
        );
    }
}
