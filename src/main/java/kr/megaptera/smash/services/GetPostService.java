package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.PostDetailDto;
import kr.megaptera.smash.exceptions.PostNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.PostRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GetPostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public GetPostService(PostRepository postRepository,
                          UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public PostDetailDto findTargetPost(Long currentUserId,
                                        Long targetPostId) {
        Post post = postRepository.findById(targetPostId)
            .orElseThrow(PostNotFound::new);

        User currentUser = currentUserId == null
            ? null
            : userRepository.findById(currentUserId)
            .orElseThrow(() -> new UserNotFound(currentUserId));

        User userInPost = userRepository.findById(post.userId())
            .orElseThrow(() -> new UserNotFound(post.userId()));

        post.addHits();

        Boolean isAuthor = post.isAuthor(currentUser);

        return post.toDetailDto(
            userInPost.toPostDetailDto(),
            isAuthor
        );
    }
}
