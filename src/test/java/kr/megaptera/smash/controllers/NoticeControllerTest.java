package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.NoticeDto;
import kr.megaptera.smash.dtos.NoticesDto;
import kr.megaptera.smash.dtos.UnreadNoticeCountDto;
import kr.megaptera.smash.services.GetNoticesService;
import kr.megaptera.smash.services.GetUnreadNoticeCountService;
import kr.megaptera.smash.services.ReadNoticeService;
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
@WebMvcTest(NoticeController.class)
class NoticeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetNoticesService getNoticesService;

    @MockBean
    private ReadNoticeService readNoticeService;

    @SpyBean
    private JwtUtil jwtUtil;

    // TODO: 모델을 정의하고 fake와 toDto 메서드를 정의하고 난 뒤에는
    //    fake().toDto() 형태로 대체하기

    @Test
    void notices() throws Exception {
        Long userId = 1L;
        List<NoticeDto> noticeDtos = List.of(
            new NoticeDto(
                1L,
                "read",
                "3시간 전",
                "내가 신청한 운동 모집 게시글의 작성자가 신청을 수락했습니다.",
                "신청한 게임 시간"
            ),
            new NoticeDto(
                2L,
                "unread",
                "6시간 전",
                "내가 작성한 운동 모집 게시글에 새로운 참가 신청이 있습니다.",
                "등록한 신청자: 신청자 이름"
            )
        );
        NoticesDto noticesDto = new NoticesDto(noticeDtos);

        given(getNoticesService.findAllNoticesOfUser(userId))
            .willReturn(noticesDto);

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.get("/notices")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void readNotice() throws Exception {
        Long targetNoticeId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.patch("/notices/1"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(readNoticeService).readTargetNotice(targetNoticeId);
    }
}
