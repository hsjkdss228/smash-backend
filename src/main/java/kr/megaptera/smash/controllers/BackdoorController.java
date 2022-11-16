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

    @GetMapping("clear-posts")
    public String emptyPosts() {
        jdbcTemplate.execute("delete from POSTS");
        jdbcTemplate.execute("delete from GAMES");
        jdbcTemplate.execute("delete from MEMBERS");

        return "게시물 목록 비우기 백도어 세팅이 완료되었습니다.";
    }

    @GetMapping("setup-posts")
    public String setupPosts() {
        resetDatabaseForPosts();

        jdbcTemplate.update(
            "insert into POSTS(" +
                "ID, HITS, CREATED_AT, UPDATED_AT) " +
                "values(?, ?, ?, ?)",
            1L, 123L, LocalDateTime.now(), LocalDateTime.now()
        );
        jdbcTemplate.update(
            "insert into POSTS(" +
                "ID, HITS, CREATED_AT, UPDATED_AT) " +
                "values(?, ?, ?, ?)",
            2L, 5593L, LocalDateTime.now(), LocalDateTime.now()
        );

        jdbcTemplate.update(
            "insert into GAMES(" +
                "ID, POST_ID, EXERCISE_NAME, DATE, PLACE_NAME, TARGET_MEMBER_COUNT) " +
                "values(?, ?, ?, ?, ?, ?)",
            1L, 1L, "축구", "2022년 11월 13일 15:00~17:00", "잠실종합운동장", 24
        );
        jdbcTemplate.update(
            "insert into GAMES(" +
                "ID, POST_ID, EXERCISE_NAME, DATE, PLACE_NAME, TARGET_MEMBER_COUNT) " +
                "values(?, ?, ?, ?, ?, ?)",
            2L, 2L, "배구", "2022년 11월 14일 15:00~17:00", "장충체육관", 12
        );

        jdbcTemplate.update(
            "insert into MEMBERS(" +
                "ID, USER_ID, GAME_ID, NAME) " +
                "values(?, ?, ?, ?)",
            1L, 1L, 1L, "사용자 1"
        );
        jdbcTemplate.update(
            "insert into MEMBERS(" +
                "ID, USER_ID, GAME_ID, NAME) " +
                "values(?, ?, ?, ?)",
            2L, 2L, 1L, "사용자 2"
        );
        jdbcTemplate.update(
            "insert into MEMBERS(" +
                "ID, USER_ID, GAME_ID, NAME) " +
                "values(?, ?, ?, ?)",
            3L, 3L, 1L, "사용자 3"
        );
        jdbcTemplate.update(
            "insert into MEMBERS(" +
                "ID, USER_ID, GAME_ID, NAME) " +
                "values(?, ?, ?, ?)",
            4L, 4L, 2L, "사용자 4"
        );

        jdbcTemplate.update(
            "insert into USERS(" +
                "ID, NAME, GENDER) " +
                "values(?, ?, ?)",
            1L, "사용자 1", "남성"
        );
        jdbcTemplate.update(
            "insert into USERS(" +
                "ID, NAME, GENDER) " +
                "values(?, ?, ?)",
            2L, "사용자 2", "남성"
        );
        jdbcTemplate.update(
            "insert into USERS(" +
                "ID, NAME, GENDER) " +
                "values(?, ?, ?)",
            3L, "사용자 3", "남성"
        );
        jdbcTemplate.update(
            "insert into USERS(" +
                "ID, NAME, GENDER) " +
                "values(?, ?, ?)",
            4L, "사용자 4", "여성"
        );

        return "게시물 목록 조회 백도어 세팅이 완료되었습니다.";
    }

    private void resetDatabaseForPosts() {
        jdbcTemplate.execute("delete from POSTS");
        jdbcTemplate.execute("delete from GAMES");
        jdbcTemplate.execute("delete from MEMBERS");
        jdbcTemplate.execute("delete from USERS");
    }

    @GetMapping("clear-members")
    public String emptyMembers() {
        jdbcTemplate.execute("delete from MEMBERS");

        return "운동 참가 멤버 비우기 백도어 세팅이 완료되었습니다.";
    }

    @GetMapping("setup-members")
    public String setupMembers() {
        jdbcTemplate.update(
            "insert into MEMBERS(" +
                "ID, USER_ID, GAME_ID, NAME) " +
                "values(?, ?, ?, ?)",
            1L, 1L, 1L, "사용자 1"
        );
        jdbcTemplate.update(
            "insert into MEMBERS(" +
                "ID, USER_ID, GAME_ID, NAME) " +
                "values(?, ?, ?, ?)",
            2L, 1L, 2L, "사용자 1"
        );

        return "운동 참가 멤버 백도어 세팅이 완료되었습니다.";
    }
}
