package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.ApplicantDetailDto;
import kr.megaptera.smash.dtos.ApplicantsDetailDto;
import kr.megaptera.smash.dtos.MemberDetailDto;
import kr.megaptera.smash.dtos.MembersDetailDto;
import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.RegisterGameFailed;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.services.GetAcceptedRegisterService;
import kr.megaptera.smash.services.GetProcessingRegisterService;
import kr.megaptera.smash.services.PatchRegisterToAcceptedService;
import kr.megaptera.smash.services.PatchRegisterToCanceledService;
import kr.megaptera.smash.services.PatchRegisterToRejectedService;
import kr.megaptera.smash.services.PostRegisterGameService;
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

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@MockMvcEncoding
@WebMvcTest(RegisterController.class)
class RegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // GET registers/members/games/{gameId}
    @MockBean
    private GetAcceptedRegisterService getAcceptedRegisterService;

    private List<MemberDetailDto> memberDetailDtos;
    private MembersDetailDto membersDetailDto;

    // GET registers/applicants/games/{gameId}
    @MockBean
    private GetProcessingRegisterService getProcessingRegisterService;

    private List<ApplicantDetailDto> applicantDetailDtos;
    private ApplicantsDetailDto applicantsDetailDto;

    // GET registers/games/{gameId}
    @MockBean
    private PostRegisterGameService postRegisterGameService;

    // PATCH registers/{gameId}?status=canceled
    @MockBean
    private PatchRegisterToCanceledService patchRegisterToCanceledService;

    // PATCH registers/{gameId}?status=accepted
    @MockBean
    private PatchRegisterToAcceptedService patchRegisterToAcceptedService;

    // PATCH registers/{gameId}?status=rejected
    @MockBean
    private PatchRegisterToRejectedService patchRegisterToRejectedService;

    @SpyBean
    private JwtUtil jwtUtil;

    private String token;

    private Long gameId;
    private Long registerId;
    private Long userId;

    @BeforeEach
    void setUp() {
        gameId = 1L;
        userId = 1L;
        registerId = 4L;
        token = jwtUtil.encode(userId);

        // GET registers/members/games/{gameId}
        List<Register> members = Register.fakeMembers(5, gameId);
        List<User> memberUsers = User.fakes(5);
        memberDetailDtos = members.stream()
            .map(member -> {
                User matchedUser = memberUsers.stream()
                    .filter(user -> user.id().equals(member.id()))
                    .findFirst().get();
                return new MemberDetailDto(
                    member.id(),
                    matchedUser.name().value(),
                    matchedUser.gender().value(),
                    matchedUser.phoneNumber().value()
                );
            })
            .toList();
        membersDetailDto = new MembersDetailDto(memberDetailDtos);

        // GET registers/applicants/games/{gameId}
        List<Register> applicants = Register.fakeApplicants(5, gameId);
        List<User> applicantUsers = User.fakes(5);
        applicantDetailDtos = applicants.stream()
            .map(applicant -> {
                User matchedUser = applicantUsers.stream()
                    .filter(user -> user.id().equals(applicant.id()))
                    .findFirst().get();
                return new ApplicantDetailDto(
                    applicant.id(),
                    matchedUser.name().value(),
                    matchedUser.gender().value(),
                    matchedUser.phoneNumber().value()
                );
            })
            .toList();
        applicantsDetailDto = new ApplicantsDetailDto(applicantDetailDtos);
    }

    @Test
    void members() throws Exception {
        given(getAcceptedRegisterService.findMembers(gameId))
            .willReturn(membersDetailDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/registers/members/games/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"phoneNumber\":\"010-0000-0000\"")
            ));
    }

    @Test
    void applicants() throws Exception {
        given(getProcessingRegisterService.findApplicants(gameId))
            .willReturn(applicantsDetailDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/registers/applicants/games/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"phoneNumber\":\"010-0000-0000\"")
            ));
    }

    @Test
    void registerToGame() throws Exception {
        RegisterGameResultDto registerGameResultDto
            = new RegisterGameResultDto(gameId);

        given(postRegisterGameService.registerGame(gameId, userId))
            .willReturn(registerGameResultDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"gameId\":1")
            ))
        ;
    }

    @Test
    void registerGameFailedWithGameNotFound() throws Exception {
        Long wrongGameId = 222L;
        given(postRegisterGameService.registerGame(wrongGameId, userId))
            .willThrow(new RegisterGameFailed(
                "주어진 게임 번호에 해당하는 게임을 찾을 수 없습니다."));

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/222")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("100")
            ))
        ;
    }

    @Test
    void registerGameFailedWithAlreadyRegisteredGame() throws Exception {
        Long alreadyRegisteredGameId = 10L;
        given(postRegisterGameService.registerGame(alreadyRegisteredGameId, userId))
            .willThrow(new RegisterGameFailed(
                "이미 신청이 완료된 운동입니다."));

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/10")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("101")
            ))
        ;
    }

    @Test
    void registerGameFailedWithUserNotFound() throws Exception {
        Long notExistedUserId = 139L;
        token = jwtUtil.encode(notExistedUserId);
        given(postRegisterGameService.registerGame(gameId, notExistedUserId))
            .willThrow(new RegisterGameFailed(
                "주어진 사용자 번호에 해당하는 사용자를 찾을 수 없습니다."));

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("102")
            ))
        ;
    }

    @Test
    void cancelRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/registers/4")
                .header("Authorization", "Bearer " + token)
                .param("status", "canceled"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(patchRegisterToCanceledService).patchRegisterToCanceled(registerId, userId);
    }

    @Test
    void acceptRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/registers/4")
                .header("Authorization", "Bearer " + token)
                .param("status", "accepted"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(patchRegisterToAcceptedService).patchRegisterToAccepted(registerId);
    }

    @Test
    void rejectRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/registers/4")
                .header("Authorization", "Bearer " + token)
                .param("status", "rejected"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(patchRegisterToRejectedService).patchRegisterToRejected(registerId);
    }
}
