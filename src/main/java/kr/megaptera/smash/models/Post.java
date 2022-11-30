package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.GameInPostListDto;
import kr.megaptera.smash.dtos.PostListDto;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    public Boolean isAuthor(User currentUser) {
        return userId.equals(currentUser.id());
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
        return new Post(
            1L,
            1L,
            new PostHits(1234L),
            new PostDetail(detail),
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

    public PostListDto toPostListDto(Boolean isAuthor,
                                     GameInPostListDto gameInPostListDto) {
        return new PostListDto(
            id,
            hits.value(),
            isAuthor,
            gameInPostListDto
        );
    }
}
