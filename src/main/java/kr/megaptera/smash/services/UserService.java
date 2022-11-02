package kr.megaptera.smash.services;

import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User user(Long id) {
    return userRepository.findById(id).orElseThrow(UserNotFound::new);
  }
}
