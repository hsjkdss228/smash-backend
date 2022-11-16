package kr.megaptera.smash.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BackdoorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void clearPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/clear-posts"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-posts"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void clearMembers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/clear-members"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupMembers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-members"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
