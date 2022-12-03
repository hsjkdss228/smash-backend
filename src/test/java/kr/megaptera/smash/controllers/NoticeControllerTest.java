package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.NoticeListDto;
import kr.megaptera.smash.dtos.NoticesDto;
import kr.megaptera.smash.services.GetNoticesService;
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

import static org.mockito.BDDMockito.given;

@MockMvcEncoding
@WebMvcTest(NoticeController.class)
class NoticeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetNoticesService getNoticesService;

    @SpyBean
    private JwtUtil jwtUtil;

    private NoticesDto noticesDto;

    // TODO: 모델을 정의하고 fake와 toDto 메서드를 정의하고 난 뒤에는
    //    fake().toDto() 형태로 대체하기

    @BeforeEach
    void setUp() {
        List<NoticeListDto> noticeListDtos = List.of(
            new NoticeListDto(
                1L,
                "3시간 전",
                "내가 신청한 운동 모집 게시글의 작성자가 신청을 수락했습니다."
            ),
            new NoticeListDto(
                2L,
                "6시간 전",
                "내가 작성한 운동 모집 게시글에 새로운 참가 신청이 있습니다."
            )
        );
        noticesDto = new NoticesDto(noticeListDtos);
    }

    @Test
    void notices() throws Exception {
        Long userId = 1L;
        given(getNoticesService.findAllNoticesOfUser(userId))
            .willReturn(noticesDto);

        String token = jwtUtil.encode(userId);

        mockMvc.perform(MockMvcRequestBuilders.get("/notices")
                .header("Authorization", "Bearer " + token))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
