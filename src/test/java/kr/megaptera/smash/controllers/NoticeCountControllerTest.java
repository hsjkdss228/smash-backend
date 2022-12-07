package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.UnreadNoticeCountDto;
import kr.megaptera.smash.services.GetUnreadNoticeCountService;
import kr.megaptera.smash.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(NoticeCountController.class)
class NoticeCountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetUnreadNoticeCountService getUnreadNoticeCountService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void unreadNoticeCount() throws Exception {
        Long currentUserId = 1L;
        Long unreadNoticeCount = 5L;
        UnreadNoticeCountDto unreadNoticeCountDto
            = new UnreadNoticeCountDto(unreadNoticeCount);

        String token = jwtUtil.encode(currentUserId);

        given(getUnreadNoticeCountService.countUnreadNotices(currentUserId))
            .willReturn(unreadNoticeCountDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/notice-count")
                .header("Authorization", "Bearer " + token)
                .param("status", "unread"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(
                containsString("5")
            ));

        verify(getUnreadNoticeCountService).countUnreadNotices(currentUserId);
    }
}
