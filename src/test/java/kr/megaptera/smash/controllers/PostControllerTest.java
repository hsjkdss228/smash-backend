package kr.megaptera.smash.controllers;

import kr.megaptera.smash.services.PostService;
import kr.megaptera.smash.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;

@WebMvcTest(PostController.class)
class PostControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

  @SpyBean
  private JwtUtil jwtUtil;

  private String token;

  @BeforeEach
  void setUp() {
    Long userId = 1L;
    token  = jwtUtil.encode(userId);
  }

  @Test
  void posts() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/posts/list"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void post() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/posts/1")
        .header("Authorization", "Bearer " + token))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}































