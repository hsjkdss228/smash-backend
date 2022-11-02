package kr.megaptera.smash.services;

import kr.megaptera.smash.models.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {

  public List<Role> roles() {
    return null;
  }
}
