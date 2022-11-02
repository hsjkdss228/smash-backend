package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.repositories.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlaceService {
  private final PlaceRepository placeRepository;

  public PlaceService(PlaceRepository placeRepository) {
    this.placeRepository = placeRepository;
  }

  public List<Place> placesInPosts(List<Post> posts) {
    return posts.stream()
        .map(post -> {
          System.out.println(post.id());
          return placeRepository.findByPostId(post.id());
        })
        .toList();
  }
}
