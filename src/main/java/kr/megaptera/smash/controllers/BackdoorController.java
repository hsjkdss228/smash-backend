package kr.megaptera.smash.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backdoor")
@Transactional
public class BackdoorController {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public BackdoorController(JdbcTemplate jdbcTemplate,
                              PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    private void resetDatabase() {
        jdbcTemplate.execute("delete from POSTS");
        jdbcTemplate.execute("delete from GAMES");
        jdbcTemplate.execute("delete from REGISTERS");
        jdbcTemplate.execute("delete from USERS");
    }

    private void resetRegisters() {
        jdbcTemplate.execute("delete from REGISTERS");
    }

    private void resetUsers() {
        jdbcTemplate.execute("delete from USERS");
    }

    private void setUpUsers(long startId, long endId) {
        for (long id = startId; id <= endId; id += 1) {
            jdbcTemplate.update(
                    "insert into USERS(" +
                            "ID, IDENTIFIER, PASSWORD, NAME, GENDER, PHONE_NUMBER) " +
                            "values(?, ?, ?, ?, ?, ?)",
                    id, "identifier" + id, passwordEncoder.encode("Password!" + id),
                    "사용자 " + id, "남성",
                    "010-0000-000" + id
            );
        }
    }

    @GetMapping("clear-posts")
    public String emptyPosts() {
        jdbcTemplate.execute("delete from POSTS");
        jdbcTemplate.execute("delete from GAMES");
        jdbcTemplate.execute("delete from REGISTERS");

        return "게시물 목록 비우기 백도어 세팅이 완료되었습니다.";
    }

    @GetMapping("setup-posts")
    public String setupPosts() {
        resetDatabase();

        jdbcTemplate.update(
                "insert into POSTS(" +
                        "ID, USER_ID, HITS, CREATED_AT, UPDATED_AT, DETAIL) " +
                        "values(?, ?, ?, ?, ?, ?)",
                1L, 1L, 123L, LocalDateTime.now(), LocalDateTime.now(),
                "축구 인원 모집 게시글입니다."
        );
        jdbcTemplate.update(
                "insert into GAMES(" +
                        "ID, POST_ID, EXERCISE_NAME, DATE, START_TIME, END_TIME, " +
                        "PLACE_NAME, TARGET_MEMBER_COUNT) " +
                        "values(?, ?, ?, ?, ?, ?, ?, ?)",
                1L, 1L, "축구",
                LocalDate.of(2022, 11, 13), LocalTime.of(15, 0), LocalTime.of(17, 0),
                "잠실종합운동장", 4
        );
        jdbcTemplate.update(
                "insert into POSTS(" +
                        "ID, USER_ID, HITS, CREATED_AT, UPDATED_AT, DETAIL) " +
                        "values(?, ?, ?, ?, ?, ?)",
                2L, 1L, 5593L, LocalDateTime.now(), LocalDateTime.now(),
                "배구 인원 모집 게시글입니다."
        );
        jdbcTemplate.update(
                "insert into GAMES(" +
                        "ID, POST_ID, EXERCISE_NAME, DATE, START_TIME, END_TIME, " +
                        "PLACE_NAME, TARGET_MEMBER_COUNT) " +
                        "values(?, ?, ?, ?, ?, ?, ?, ?)",
                2L, 2L, "배구",
                LocalDate.of(2022, 11, 14), LocalTime.of(15, 0), LocalTime.of(17, 0),
                "장충체육관", 4
        );

        return "게시물 목록 백도어 세팅이 완료되었습니다.";
    }

    @GetMapping("setup-members-posts")
    public String setupMembersPosts() {
        resetRegisters();
        resetUsers();

        // set author
        jdbcTemplate.update(
                "insert into USERS(" +
                        "ID, IDENTIFIER, PASSWORD, NAME, GENDER, PHONE_NUMBER) " +
                        "values(?, ?, ?, ?, ?, ?)",
                1L, "identifier1", passwordEncoder.encode("Password!1"),
                "작성자 이름", "남성", "010-0000-0001"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                1L, 1L, 1L, "accepted" // DB는 아주 명확히 잡아주세요.
                //                    코드와 불일치하면 E2E 테스트에서 실패해야 합니다.
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                2L, 1L, 2L, "accepted"
        );

        setUpUsers(2, 6);

        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                3L, 3L, 1L, "accepted"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                4L, 4L, 1L, "processing"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                5L, 5L, 1L, "canceled"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                6L, 6L, 2L, "accepted"
        );

        return "운동 참가 멤버 백도어 세팅이 완료되었습니다.";
    }

    private void setupFullMembersPosts() {

    }

    private void setupOnePost() {
        jdbcTemplate.update(
                "insert into POSTS(" +
                        "ID, USER_ID, HITS, CREATED_AT, UPDATED_AT, DETAIL) " +
                        "values(?, ?, ?, ?, ?, ?)",
                1L, 1L, 123L, LocalDateTime.now(), LocalDateTime.now(),
                "야구 인원 모집 게시글입니다."
        );
        jdbcTemplate.update(
                "insert into USERS(" +
                        "ID, IDENTIFIER, PASSWORD, NAME, GENDER, PHONE_NUMBER) " +
                        "values(?, ?, ?, ?, ?, ?)",
                1L, "identifier1", "Password!1", "작성자 이름", "남성", "010-0000-0001"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                1L, 1L, 1L, "accepted"
        );
        jdbcTemplate.update(
                "insert into GAMES(" +
                        "ID, POST_ID, EXERCISE_NAME, DATE, START_TIME, END_TIME, " +
                        "PLACE_NAME, TARGET_MEMBER_COUNT) " +
                        "values(?, ?, ?, ?, ?, ?, ?, ?)",
                1L, 1L, "배드민턴",
                LocalDate.of(2022, 11, 13), LocalTime.of(14, 0), LocalTime.of(17, 0),
                "올림픽공원", 4
        );
    }

    @GetMapping("setup-members-not-finished")
    public String setupMembersNotFinished() {
        resetDatabase();
        setupOnePost();

        setUpUsers(2, 5);

        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                2L, 2L, 1L, "accepted"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                3L, 3L, 1L, "canceled"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                4L, 4L, 1L, "processing"
        );

        return "운동 게시글 1개 참가자 목록 (잔여석 있음) 백도어 세팅이 완료되었습니다.";
    }

    @GetMapping("setup-members-finished")
    public String setupMembersFinished() {
        resetDatabase();
        setupOnePost();

        setUpUsers(2, 5);

        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                2L, 2L, 1L, "accepted"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                3L, 3L, 1L, "accepted"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                4L, 4L, 1L, "accepted"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                5L, 5L, 1L, "processing"
        );

        return "운동 게시글 1개 참가자 목록 (잔여석 있음) 백도어 세팅이 완료되었습니다.";
    }

    @GetMapping("setup-members-registered")
    public String setupMembersRegistered() {
        resetDatabase();
        setupOnePost();

        setUpUsers(2, 4);

        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                2L, 2L, 1L, "processing"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                3L, 3L, 1L, "accepted"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                4L, 4L, 1L, "accepted"
        );

        return "운동 게시글 1개 참가자 목록 (사용자가 신청) 백도어 세팅이 완료되었습니다.";
    }

    @GetMapping("setup-members-with-applicants")
    public String setupMembersWithApplicants() {
        resetDatabase();
        setupOnePost();

        setUpUsers(2, 4);

        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                2L, 2L, 1L, "processing"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                3L, 3L, 1L, "processing"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                4L, 4L, 1L, "processing"
        );

        return "운동 게시글 1개 신청자 목록 (참가신청 있음) 백도어 세팅이 완료되었습니다.";
    }

    @GetMapping("setup-members-without-applicants")
    public String setupMembersWithoutApplicants() {
        resetDatabase();
        setupOnePost();

        setUpUsers(2, 4);

        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                2L, 2L, 1L, "canceled"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                3L, 3L, 1L, "rejected"
        );
        jdbcTemplate.update(
                "insert into REGISTERS(" +
                        "ID, USER_ID, GAME_ID, STATUS) " +
                        "values(?, ?, ?, ?)",
                4L, 4L, 1L, "accepted"
        );

        return "운동 게시글 1개 신청자 목록 (참가신청 없음) 백도어 세팅이 완료되었습니다.";
    }
}
