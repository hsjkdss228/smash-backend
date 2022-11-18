package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.MemberDetailDto;
import kr.megaptera.smash.dtos.MembersDetailDto;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.services.DeleteGameMemberService;
import kr.megaptera.smash.services.GetMembersService;
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

@WebMvcTest(MemberController.class)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // GET members/gameId
    @MockBean
    private GetMembersService getMembersService;

    private List<MemberDetailDto> memberDetailDtos;
    private MembersDetailDto membersDetailDto;

    // DELETE members/gameId
    @MockBean
    private DeleteGameMemberService deleteGameMemberService;

    // stubs
    @SpyBean
    private JwtUtil jwtUtil;

    private String token;

    private Long userId;
    private Long targetGameId = 1L;

    @BeforeEach
    void setUp() {
        userId = 1L;
        token = jwtUtil.encode(userId);

        // GET members/gameId
        List<Member> members = Member.fakes(5, targetGameId);
        List<User> users = User.fakes(5);
        memberDetailDtos = members.stream()
            .map(member -> {
                User matchedUser = users.stream()
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
    }

    @Test
    void members() throws Exception {
        given(getMembersService.findTargetMembers(targetGameId))
            .willReturn(membersDetailDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/members/games/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("\"phoneNumber\":\"010-0000-0000\"")
            ));
    }

    @Test
    void deleteMember() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/members/games/1")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(deleteGameMemberService).deleteGameMember(userId, targetGameId);
    }
}
