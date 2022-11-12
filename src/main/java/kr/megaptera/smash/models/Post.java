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

    @Embedded
    private PostHits hits;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Post() {

    }

    public Post(Long id,
                PostHits hits
    ) {
        this.id = id;
        this.hits = hits;
    }

    public Post(Long id,
                PostHits hits,
                LocalDateTime createdAt,
                LocalDateTime updatedAt
    ) {
        this.id = id;
        this.hits = hits;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long id() {
        return id;
    }

    public PostHits hits() {
        return hits;
    }

    public static List<Post> fakes(long generationCount) {
        List<Post> posts = new ArrayList<>();
        for (long id = 1; id <= generationCount; id += 1) {
            Post post = new Post(
                id,
                new PostHits(123L),
                LocalDateTime.now(),
                LocalDateTime.now()
            );
            posts.add(post);
        }
        return posts;
    }

    public PostListDto toPostListDto(GameInPostListDto gameInPostListDto) {
        return new PostListDto(
            id,
            hits.value(),
            gameInPostListDto
        );
    }
}
