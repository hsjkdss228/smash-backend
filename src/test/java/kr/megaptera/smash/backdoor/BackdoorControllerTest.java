package kr.megaptera.smash.backdoor;

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
    void clearDatabases() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/clear-databases"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupPostsAuthor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-posts-author"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupPostsNotApplicant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-posts-not-applicant"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupPostsApplicant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-posts-applicant"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupPostsMember() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-posts-member"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupPostNotApplicantAndNotFull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-post-not-applicant-and-not-full"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupPostNotApplicantAndFull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-post-not-applicant-and-full"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupPostApplicant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-post-applicant"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupPostMember() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-post-member"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupPostAuthorWithApplicants() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-post-author-with-applicants"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupPostAuthorWithoutApplicants() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-post-author-without-applicants"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupRegistrableCase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-registrable-case"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupCancellationParticipationCase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-cancellation-participation-case"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void setupCancellationRegistrationCase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/backdoor/setup-cancellation-registration-case"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
