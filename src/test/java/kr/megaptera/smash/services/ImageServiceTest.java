package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Image;
import kr.megaptera.smash.repositories.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ImageServiceTest {
  private ImageService imageService;
  private ImageRepository imageRepository;

  @BeforeEach
  void setUp() {
    imageRepository = mock(ImageRepository.class);
    imageService = new ImageService(imageRepository);
  }

  @Test
  void imagesByPost() {
    Long postId = 1L;
    List<Image> images = List.of(
        new Image(1L, 1L, "Main Cycling Image Url", true),
        new Image(2L, 1L, "Sub Cycling Image Url", false)
    );
    given(imageRepository.findAllByPostId(postId))
        .willReturn(images);

    List<Image> foundImages = imageService.imagesByPost(postId);

    assertThat(foundImages).hasSize(2);
    assertThat(foundImages.get(1).url()).contains("Image Url");

    verify(imageRepository).findAllByPostId(any(Long.class));
  }
}
