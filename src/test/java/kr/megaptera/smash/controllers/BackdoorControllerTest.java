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
    void setupMembersPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-members-posts"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupMembersNotFinished() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/backdoor/setup-members-not-finished"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupMembersFinished() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/backdoor/setup-members-finished"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupMembersRegistered() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/backdoor/setup-members-registered"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupMembersWithApplicants() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/backdoor/setup-members-with-applicants"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupMembersWithoutApplicants() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/backdoor/setup-members-without-applicants"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
