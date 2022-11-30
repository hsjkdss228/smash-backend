package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.RegisterAcceptedDto;
import kr.megaptera.smash.dtos.RegistersAcceptedDto;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.services.GetAcceptedRegisterService;
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

@MockMvcEncoding
@WebMvcTest(MemberController.class)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetAcceptedRegisterService getAcceptedRegisterService;

    private List<RegisterAcceptedDto> registerAcceptedDtos;
    private RegistersAcceptedDto registersAcceptedDto;

    private Long gameId;

    @BeforeEach
    void setUp() {
        gameId = 1L;
        List<Register> members = Register.fakesAccepted(5, gameId);
        List<User> memberUsers = User.fakes(5);
        registerAcceptedDtos = members.stream()
            .map(member -> {
                User matchedUser = memberUsers.stream()
                    .filter(user -> user.id().equals(member.id()))
                    .findFirst().get();
                return new RegisterAcceptedDto(
                    member.id(),
                    matchedUser.name().value(),
                    matchedUser.gender().value(),
                    matchedUser.phoneNumber().value()
                );
            })
            .toList();
        registersAcceptedDto
            = new RegistersAcceptedDto(registerAcceptedDtos);
    }

    @Test
    void members() throws Exception {
        given(getAcceptedRegisterService.findAcceptedRegisters(gameId))
            .willReturn(registersAcceptedDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/games/1/members"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"phoneNumber\":\"010-0000-0000\"")
            ));
    }
}
