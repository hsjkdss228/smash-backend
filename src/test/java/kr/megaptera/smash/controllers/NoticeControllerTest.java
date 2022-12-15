package kr.megaptera.smash.controllers;

import kr.megaptera.smash.config.MockMvcEncoding;
import kr.megaptera.smash.dtos.NoticeDto;
import kr.megaptera.smash.dtos.NoticesDto;
import kr.megaptera.smash.models.Notice;
import kr.megaptera.smash.services.DeleteNoticesService;
import kr.megaptera.smash.services.GetNoticesService;
import kr.megaptera.smash.services.ReadNoticeService;
import kr.megaptera.smash.services.ReadNoticesService;
import kr.megaptera.smash.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
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

    @MockBean
    private ReadNoticesService readNoticesService;

    @MockBean
    private DeleteNoticesService deleteNoticesService;

    @SpyBean
    private JwtUtil jwtUtil;

    @Test
    void notices() throws Exception {
        Long userId = 1L;
        long generationCount = 2;
        List<NoticeDto> noticeDtos = Notice.fakesUnread(generationCount)
            .stream()
            .map(Notice::toDto)
            .toList();
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

    @Test
    void readNotices() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/notices")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"ids\":[1,2]" +
                    "}")
                .param("status", "read"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(readNoticesService).readTargetNotices(anyList());
    }

    @Test
    void deleteNotices() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/notices")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"ids\":[1,2]" +
                    "}")
                .param("status", "deleted"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(deleteNoticesService).deleteTargetNotices(anyList());
    }
}
