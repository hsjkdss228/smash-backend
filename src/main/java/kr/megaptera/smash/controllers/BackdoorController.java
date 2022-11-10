package kr.megaptera.smash.controllers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/backdoor")
@Transactional
public class BackdoorController {
    private final JdbcTemplate jdbcTemplate;

    public BackdoorController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("setup-posts")
    public String setupPosts() {
        resetDatabaseForPosts();

        jdbcTemplate.update(
            "insert into POST(" +
                "ID, HITS, CREATED_AT, UPDATED_AT) " +
                "values(?, ?, ?, ?)",
            1L, 123L, LocalDateTime.now(), LocalDateTime.now()
        );
        jdbcTemplate.update(
            "insert into POST(" +
                "ID, HITS, CREATED_AT, UPDATED_AT) " +
                "values(?, ?, ?, ?)",
            2L, 5593L, LocalDateTime.now(), LocalDateTime.now()
        );

        jdbcTemplate.update(
            "insert into GAME(" +
                "ID, POST_ID, TYPE, DATE, PLACE, TARGET_MEMBER_COUNT) " +
                "values(?, ?, ?, ?, ?, ?)",
            1L, 1L, "축구", "2022년 11월 13일 15:00~17:00", "잠실종합운동장", 24
        );
        jdbcTemplate.update(
            "insert into GAME(" +
                "ID, POST_ID, TYPE, DATE, PLACE, TARGET_MEMBER_COUNT) " +
                "values(?, ?, ?, ?, ?, ?)",
            2L, 2L, "배구", "2022년 11월 14일 15:00~17:00", "장충체육관", 12
        );
        jdbcTemplate.update(
            "insert into MEMBER(" +
                "ID, GAME_ID, NAME) " +
                "values(?, ?, ?)",
            1L, 1L, "운동 1 참가자 1"
        );
        jdbcTemplate.update(
            "insert into MEMBER(" +
                "ID, GAME_ID, NAME) " +
                "values(?, ?, ?)",
            2L, 1L, "운동 1 참가자 2"
        );
        jdbcTemplate.update(
            "insert into MEMBER(" +
                "ID, GAME_ID, NAME) " +
                "values(?, ?, ?)",
            3L, 1L, "운동 1 참가자 3"
        );
        jdbcTemplate.update(
            "insert into MEMBER(" +
                "ID, GAME_ID, NAME) " +
                "values(?, ?, ?)",
            4L, 2L, "운동 2 참가자 1"
        );

        return "게시물 목록 조회 백도어 세팅이 완료되었습니다.";
    }

    private void resetDatabaseForPosts() {
        jdbcTemplate.execute("delete from POST");
        jdbcTemplate.execute("delete from GAME");
        jdbcTemplate.execute("delete from MEMBER");
    }
}
