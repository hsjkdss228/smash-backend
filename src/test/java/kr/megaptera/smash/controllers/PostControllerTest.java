package kr.megaptera.smash.controllers;

import kr.megaptera.smash.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

  @Test
  void posts() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/posts/list"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void post() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/posts/1"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}































