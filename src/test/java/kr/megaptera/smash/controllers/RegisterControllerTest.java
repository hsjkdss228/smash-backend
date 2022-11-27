package kr.megaptera.smash.controllers;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.ApplicantDetailDto;
import kr.megaptera.smash.dtos.ApplicantsDetailDto;
import kr.megaptera.smash.dtos.MemberDetailDto;
import kr.megaptera.smash.dtos.MembersDetailDto;
import kr.megaptera.smash.dtos.RegisterGameResultDto;
import kr.megaptera.smash.exceptions.AlreadyGameJoined;
import kr.megaptera.smash.exceptions.GameIsFull;
import kr.megaptera.smash.exceptions.GameNotFound;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Register;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.services.GetAcceptedRegisterService;
import kr.megaptera.smash.services.GetProcessingRegisterService;
import kr.megaptera.smash.services.JoinGameService;
import kr.megaptera.smash.services.PatchRegisterToAcceptedService;
import kr.megaptera.smash.services.PatchRegisterToCanceledService;
import kr.megaptera.smash.services.PatchRegisterToRejectedService;
import kr.megaptera.smash.utils.JwtUtil;

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
    // 이런 주석을 쓴다는 것 자체가 뭔가 잘못 되고 있다는 아주 강력한 신호입니다.
    // 주석 없이는 차마 볼 수 없는 코드라는 거죠.
    // 이걸 쓰는 순간에 고통을 느끼고 바로 다르게 해야 했어요.
    @MockBean
    private JoinGameService joinGameService;

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

        given(joinGameService.joinGame(gameId, userId))
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
        Long gameId = 222L;

        given(joinGameService.joinGame(gameId, userId))
                .willThrow(new GameNotFound());

        mockMvc.perform(MockMvcRequestBuilders.post(
                                "/registers/games/" + gameId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        // 아래 코드는 아무 의미가 없습니다.
        // 프론트엔드에서 실제로 쓰나요? 써도 문제입니다.
        // 이렇게 하는 건 대체 누가 시작한 건가요?
//                .andExpect(MockMvcResultMatchers.content().string(
//                        containsString("100")
//                ))
    }

    @Test
    void registerGameFailedWithAlreadyRegisteredGame() throws Exception {
        Long gameId = 10L;
        given(joinGameService.joinGame(gameId, userId))
                .willThrow(new AlreadyGameJoined(gameId));

        mockMvc.perform(MockMvcRequestBuilders.post(
                                "/registers/games/" + gameId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//                .andExpect(MockMvcResultMatchers.content().string(
//                        containsString("101")
//                ))
    }

    @Test
    void registerGameFailedWithUserNotFound() throws Exception {
        // setUp에서 userId는 숨기고 token만 노출하다 보니 이 부분이 복잡합니다.
        // 예를 들어 static field로 분리만 해도 좀 더 단순했을 겁니다.

        Long userId = 139L;

        given(joinGameService.joinGame(gameId, userId))
                .willThrow(new UserNotFound());

        String accessToken = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/1")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//                .andExpect(MockMvcResultMatchers.content().string(
//                        containsString("102")
//                ))
    }

    @Test
    void registerGameFailedWithFullyParticipants() throws Exception {
        given(joinGameService.joinGame(gameId, userId))
                .willThrow(new GameIsFull(1L));

        mockMvc.perform(MockMvcRequestBuilders.post("/registers/games/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//                .andExpect(MockMvcResultMatchers.content().string(
//                        containsString("103")
//                ))
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
