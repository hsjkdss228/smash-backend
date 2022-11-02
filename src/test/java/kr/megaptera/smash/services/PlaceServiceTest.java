package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Place;
import kr.megaptera.smash.models.Post;
import kr.megaptera.smash.repositories.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PlaceServiceTest {
  private PlaceService placeService;
  private PlaceRepository placeRepository;

  @BeforeEach
  void setUp() {
    placeRepository = mock(PlaceRepository.class);
    placeService = new PlaceService(placeRepository);
  }

  @Test
  void placesInPosts() {
    List<Post> posts = List.of(
        new Post(1L, 1L, "Gathering", 15, "Play baseball with me"),
        new Post(2L, 2L, "Gathering", 30, "Play football with me")
    );
    List<Place> places = List.of(
        new Place(1L, 1L, "Guui Baseball Park"),
        new Place(2L, 2L, "Jayang Middle School"));

    given(placeRepository.findByPostId(1L)).willReturn(places.get(0));
    given(placeRepository.findByPostId(2L)).willReturn(places.get(1));

    List<Place> placesInPosts = placeService.placesInPosts(posts);

    assertThat(placesInPosts).isNotNull();
    assertThat(placesInPosts.get(0).name()).contains("Baseball Park");

    verify(placeRepository).findByPostId(1L);
    verify(placeRepository).findByPostId(2L);
  }
}
