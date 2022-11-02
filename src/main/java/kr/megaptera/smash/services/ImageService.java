package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Image;
import kr.megaptera.smash.repositories.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImageService {
  private final ImageRepository imageRepository;

  public ImageService(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  public List<Image> imagesByPost(Long postId) {
    return imageRepository.findAllByPostId(postId);
  }
}
