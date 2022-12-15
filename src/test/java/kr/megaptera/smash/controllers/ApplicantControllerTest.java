package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.RegisterProcessingDto;
import kr.megaptera.smash.dtos.RegistersProcessingDto;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.services.GetProcessingRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;

@WebMvcTest(ApplicantController.class)
class ApplicantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetProcessingRegisterService getProcessingRegisterService;

    private List<RegisterProcessingDto> registerProcessingDtos;
    private RegistersProcessingDto registersProcessingDto;

    private Long gameId;

    @BeforeEach
    void setUp() {
        gameId = 1L;
        List<Register> applicants = Register.fakesProcessing(5, gameId);
        List<User> applicantUsers = User.fakes(5);
        registerProcessingDtos = applicants.stream()
            .map(applicant -> {
                User matchedUser = applicantUsers.stream()
                    .filter(user -> user.id().equals(applicant.id()))
                    .findFirst().get();
                return new RegisterProcessingDto(
                    applicant.id(),
                    matchedUser.toPostDetailDto()
                );
            })
            .toList();
        registersProcessingDto = new RegistersProcessingDto(registerProcessingDtos);
    }

    @Test
    void applicants() throws Exception {
        given(getProcessingRegisterService.findApplicants(gameId))
            .willReturn(registersProcessingDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/games/1/applicants"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"phoneNumber\":\"010-0000-0000\"")
            ));
    }
}
