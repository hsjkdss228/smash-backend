package kr.megaptera.smash.backdoor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("backdoor")
@Transactional
public class BackdoorController {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public BackdoorController(JdbcTemplate jdbcTemplate,
                              PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    private void insertUser(Long id,
                            String username,
                            String password,
                            String name,
                            String gender,
                            String phoneNumber,
                            Double mannerScore,
                            String profileImageUrl) {
        jdbcTemplate.update(
            "INSERT INTO users(" +
                "id, username, password, " +
                "name, gender, phone_number, manner_score, " +
                "profile_image_url) " +
                "VALUES(" +
                "?, ?, ?," +
                "?, ?, ?, ?, " +
                "?)",
            id, username, passwordEncoder.encode(password),
            name, gender, phoneNumber, mannerScore,
            profileImageUrl
        );
    }

    private void insertPost(Long id,
                            Long userId,
                            Long hits,
                            LocalDateTime createdAt,
                            LocalDateTime updatedAt,
                            String detail) {
        jdbcTemplate.update(
            "INSERT INTO posts(" +
                "id, user_id, hits, created_at, updated_at, detail) " +
                "VALUES(" +
                "?, ?, ?, ?, ?, ?)",
            id, userId, hits, createdAt, updatedAt, detail
        );
    }

    private void insertPostImage(Long postId,
                                 String imageUrl,
                                 Boolean isThumbnailImage) {
        jdbcTemplate.update(
            "INSERT INTO post_images(" +
                "post_id, image_url, is_thumbnail_image) " +
                "VALUES(" +
                "?, ?, ?)",
            postId, imageUrl, isThumbnailImage
        );
    }

    private void insertGame(Long id,
                            Long postId,
                            Long placeId,
                            String exerciseName,
                            LocalDate date,
                            LocalTime startTime,
                            LocalTime endTime,
                            Integer targetMemberCount) {
        jdbcTemplate.update(
            "INSERT INTO games(" +
                "id, post_id, place_id, exercise_name, " +
                "date, start_time, end_time, target_member_count) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
            id, postId, placeId, exerciseName,
            date, startTime, endTime, targetMemberCount
        );
    }

    private void insertPlace(Long id,
                             String name,
                             String contactNumber,
                             String exerciseName,
                             String roadAddress,
                             String jibunAddress,
                             String registrationStatus) {
        jdbcTemplate.update(
            "INSERT INTO places(" +
                "id, name, contact_number, exercise_name, " +
                "road_address, jibun_address, registration_status) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)",
            id, name, contactNumber, exerciseName,
            roadAddress, jibunAddress, registrationStatus
        );
    }

    private void insertRegister(Long id,
                                Long userId,
                                Long gameId,
                                String status) {
        jdbcTemplate.update(
            "INSERT INTO registers(" +
                "id, user_id, game_id, status) " +
                "VALUES(?, ?, ?, ?)",
            id, userId, gameId, status
        );
    }

    private void insertNotice(Long id,
                              Long userId,
                              String title,
                              String detail,
                              String status,
                              LocalDateTime createdAt) {
        jdbcTemplate.update(
            "INSERT INTO notices(" +
                "id, user_id, title, detail, status, created_at) " +
                "VALUES(?, ?, ?, ?, ?, ?)",
            id, userId, title, detail, status, createdAt
        );
    }

    @GetMapping("clear-databases")
    public String clearDatabases() {
        jdbcTemplate.execute("DELETE FROM notices");
        jdbcTemplate.execute("DELETE FROM registers");
        jdbcTemplate.execute("DELETE FROM places");
        jdbcTemplate.execute("DELETE FROM games");
        jdbcTemplate.execute("DELETE FROM post_images");
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM users");

        return "Database가 초기화 되었습니다.";
    }

    public void insertPostsDataNotRelatedToUser() {
        Long post1Id = 1L;
        Long post2Id = 2L;
        Long game1Id = 1L;
        Long game2Id = 2L;
        Long place1Id = 1L;
        Long place2Id = 2L;

        insertPostImage(
            post1Id, "https://sports.seoul.go.kr/file/view/FID00000360/2.do", true);
        insertPostImage(
            post1Id, "https://sports.seoul.go.kr/file/view/FID00000360/2.do", false);
        insertPostImage(
            post2Id, "https://sports.seoul.go.kr/file/view/FID00000360/2.do", true);
        insertPostImage(
            post2Id, "https://sports.seoul.go.kr/file/view/FID00000360/2.do", false);
        insertGame(
            game1Id, post1Id, place1Id, "축구",
            LocalDate.of(2022, 11, 13), LocalTime.of(15, 0), LocalTime.of(18, 0), 28);
        insertGame(
            game2Id, post2Id, place2Id, "야구",
            LocalDate.of(2022, 11, 14), LocalTime.of(12, 0), LocalTime.of(17, 0), 30);
        insertPlace(
            place1Id, "상암월드컵경기장", "010-1234-5678", "축구",
            "서울 마포구 월드컵로 240", "서울 마포구 성산동 515", "REGISTERED");
        insertPlace(
            place2Id, "잠실야구장", "010-8765-4321", "야구",
            "서울 송파구 올림픽로 25", "서울 송파구 잠실동 10-1", "REGISTERED");
    }

    @GetMapping("setup-posts-author")
    public String setupPostsAuthor() {
        clearDatabases();

        Long post1Id = 1L;
        Long post2Id = 2L;
        Long user1Id = 1L;
        Long authorId = 2L;
        Long game1Id = 1L;
        Long game2Id = 2L;
        Long register1Id = 1L;
        Long register2Id = 2L;

        insertUser(
            user1Id, "user1234", "Password!1",
            "사용자", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            authorId, "author12", "Password!1",
            "작성자", "남성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            post1Id, user1Id, 100L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용 1");
        insertPost(
            post2Id, authorId, 200L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용 2");

        insertPostsDataNotRelatedToUser();

        insertRegister(register1Id, user1Id, game1Id, "accepted");
        insertRegister(register2Id, authorId, game2Id, "accepted");

        return "게시글 목록 보기: 등록된 게시물이 존재하는 경우 (작성자 관점)" +
            "테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-posts-not-applicant")
    public String setupPostsNotApplicant() {
        clearDatabases();

        Long post1Id = 1L;
        Long post2Id = 2L;
        Long user1Id = 1L;
        Long authorId = 2L;
        Long game1Id = 1L;
        Long game2Id = 2L;
        Long register1Id = 1L;
        Long register2Id = 2L;

        insertUser(
            user1Id, "user1234", "Password!1",
            "사용자", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            authorId, "author12", "Password!1",
            "작성자", "남성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            post1Id, authorId, 100L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용 1");
        insertPost(
            post2Id, authorId, 200L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용 2");

        insertPostsDataNotRelatedToUser();

        insertRegister(register1Id, authorId, game1Id, "accepted");
        insertRegister(register2Id, authorId, game2Id, "accepted");

        return "게시글 목록 보기: 등록된 게시물이 존재하는 경우 (미신청자 관점)" +
            "테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-posts-applicant")
    public String setupPostsApplicant() {
        clearDatabases();

        Long post1Id = 1L;
        Long post2Id = 2L;
        Long user1Id = 1L;
        Long authorId = 2L;
        Long game1Id = 1L;
        Long game2Id = 2L;
        Long register1Id = 1L;
        Long register2Id = 2L;
        Long register3Id = 3L;

        insertUser(
            user1Id, "user1234", "Password!1",
            "사용자", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            authorId, "author12", "Password!1",
            "작성자", "남성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            post1Id, authorId, 100L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용 1");
        insertPost(
            post2Id, authorId, 200L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용 2");

        insertPostsDataNotRelatedToUser();

        insertRegister(register1Id, authorId, game1Id, "accepted");
        insertRegister(register2Id, authorId, game2Id, "accepted");
        insertRegister(register3Id, user1Id, game2Id, "processing");

        return "게시글 목록 보기: 등록된 게시물이 존재하는 경우 (신청자 관점)" +
            "테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-posts-member")
    public String setupPostsMember() {
        clearDatabases();

        Long post1Id = 1L;
        Long post2Id = 2L;
        Long user1Id = 1L;
        Long authorId = 2L;
        Long game1Id = 1L;
        Long game2Id = 2L;
        Long register1Id = 1L;
        Long register2Id = 2L;
        Long register3Id = 3L;

        insertUser(
            user1Id, "user1234", "Password!1",
            "사용자", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            authorId, "author12", "Password!1",
            "작성자", "남성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            post1Id, authorId, 100L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용 1");
        insertPost(
            post2Id, authorId, 200L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용 2");

        insertPostsDataNotRelatedToUser();

        insertRegister(register1Id, authorId, game1Id, "accepted");
        insertRegister(register2Id, authorId, game2Id, "accepted");
        insertRegister(register3Id, user1Id, game2Id, "accepted");

        return "게시글 목록 보기: 등록된 게시물이 존재하는 경우 (참가자 관점)" +
            "테스트 케이스 데이터를 세팅했습니다.";
    }

    public void insertPostDataNotRelatedToUser() {
        Long postId = 1L;
        Long gameId = 1L;
        Long placeId = 1L;

        insertPostImage(
            postId, "https://sports.seoul.go.kr/file/view/FID00000360/2.do", true);
        insertGame(
            gameId, postId, placeId, "축구",
            LocalDate.of(2022, 11, 13), LocalTime.of(15, 0), LocalTime.of(18, 0), 4);
        insertPlace(
            placeId, "상암월드컵경기장", "02-8765-4321", "축구",
            "서울 마포구 월드컵로 240", "서울 마포구 성산동 515", "REGISTERED");
    }

    @GetMapping("setup-post-not-logged-in-and-not-full")
    public String setupPostNotLoggedInAndNotFull() {
        clearDatabases();

        Long postId = 1L;
        Long user1Id = 1L;
        Long user2Id = 2L;
        Long gameId = 1L;
        Long register1Id = 1L;
        Long register2Id = 2L;

        insertUser(
            user1Id, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user2Id, "user5678", "Password!1",
            "사용자 2", "남성", "010-2345-6789", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            postId, user1Id, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(register1Id, user1Id, gameId, "accepted");
        insertRegister(register2Id, user2Id, gameId, "accepted");

        return "운동 모집 게시글 상세 정보 보기 (신청자 관점): " +
            "비로그인 상태에서 게시글 상세 정보 보기 (여석이 있는 경우) " +
            "테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-post-not-logged-in-and-full")
    public String setupPostNotLoggedInAndFull() {
        clearDatabases();

        Long postId = 1L;
        Long user1Id = 1L;
        Long user2Id = 2L;
        Long user3Id = 3L;
        Long user4Id = 4L;
        Long gameId = 1L;
        Long register1Id = 1L;
        Long register2Id = 2L;
        Long register3Id = 3L;
        Long register4Id = 4L;

        insertUser(
            user1Id, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user2Id, "user5678", "Password!1",
            "사용자 2", "남성", "010-2345-6789", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user3Id, "user1357", "Password!1",
            "사용자 3", "남성", "010-2345-6789", 9.8,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user4Id, "user2468", "Password!1",
            "사용자 4", "남성", "010-3456-7890", 9.8,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            postId, user1Id, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(register1Id, user1Id, gameId, "accepted");
        insertRegister(register2Id, user2Id, gameId, "accepted");
        insertRegister(register3Id, user3Id, gameId, "accepted");
        insertRegister(register4Id, user4Id, gameId, "accepted");

        return "운동 모집 게시글 상세 정보 보기 (신청자 관점): " +
            "비로그인 상태에서 게시글 상세 정보 보기 (여석이 없는 경우) " +
            "테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-post-not-applicant-and-not-full")
    public String setupPostNotApplicantAndNotFull() {
        clearDatabases();

        Long postId = 1L;
        Long user1Id = 1L;
        Long user2Id = 2L;
        Long gameId = 1L;
        Long register1Id = 1L;

        insertUser(
            user1Id, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user2Id, "user5678", "Password!1",
            "사용자 2", "남성", "010-2345-6789", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            postId, user2Id, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(register1Id, user2Id, gameId, "accepted");

        return "운동 모집 게시글 상세 정보 보기 (신청자 관점): " +
            "참가 신청하지 않은 상태에서 게시글 상세 정보 보기 (여석이 있는 경우) " +
            "테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-post-not-applicant-and-full")
    public String setupPostNotApplicantAndFull() {
        clearDatabases();

        Long postId = 1L;
        Long user1Id = 1L;
        Long user2Id = 2L;
        Long user3Id = 3L;
        Long user4Id = 4L;
        Long user5Id = 5L;
        Long gameId = 1L;
        Long register1Id = 1L;
        Long register2Id = 2L;
        Long register3Id = 3L;
        Long register4Id = 4L;

        insertUser(
            user1Id, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user2Id, "user5678", "Password!1",
            "사용자 2", "남성", "010-2345-6789", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user3Id, "user1357", "Password!1",
            "사용자 3", "남성", "010-1111-1111", 1.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user4Id, "user2468", "Password!1",
            "사용자 4", "남성", "010-2222-2222", 3.6,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user5Id, "user3579", "Password!1",
            "사용자 5", "남성", "010-3333-3333", 3.6,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            postId, user2Id, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(register1Id, user2Id, gameId, "accepted");
        insertRegister(register2Id, user3Id, gameId, "accepted");
        insertRegister(register3Id, user4Id, gameId, "accepted");
        insertRegister(register4Id, user5Id, gameId, "accepted");

        return "운동 모집 게시글 상세 정보 보기 (신청자 관점): " +
            "참가 신청하지 않은 상태에서 게시글 상세 정보 보기 (여석이 없는 경우) " +
            "테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-post-applicant")
    public String setupPostApplicant() {
        clearDatabases();

        Long postId = 1L;
        Long user1Id = 1L;
        Long user2Id = 2L;
        Long user3Id = 3L;
        Long user4Id = 4L;
        Long gameId = 1L;
        Long register1Id = 1L;
        Long register2Id = 2L;
        Long register3Id = 3L;
        Long register4Id = 4L;

        insertUser(
            user1Id, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user2Id, "user5678", "Password!1",
            "사용자 2", "남성", "010-2345-6789", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user3Id, "user1357", "Password!1",
            "사용자 3", "남성", "010-1111-1111", 1.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            postId, user2Id, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(register1Id, user1Id, gameId, "processing");
        insertRegister(register2Id, user2Id, gameId, "accepted");
        insertRegister(register3Id, user3Id, gameId, "accepted");

        return "운동 모집 게시글 상세 정보 보기 (신청자 관점): " +
            "참가 신청한 게시글 상세 정보 보기 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-post-member")
    public String setupPostMember() {
        clearDatabases();

        Long postId = 1L;
        Long user1Id = 1L;
        Long user2Id = 2L;
        Long user3Id = 3L;
        Long gameId = 1L;
        Long register1Id = 1L;
        Long register2Id = 2L;
        Long register3Id = 3L;

        insertUser(
            user1Id, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user2Id, "user5678", "Password!1",
            "사용자 2", "남성", "010-2345-6789", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user3Id, "user1357", "Password!1",
            "사용자 3", "남성", "010-1111-1111", 1.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            postId, user2Id, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(register1Id, user1Id, gameId, "accepted");
        insertRegister(register2Id, user2Id, gameId, "accepted");
        insertRegister(register3Id, user3Id, gameId, "accepted");

        return "운동 모집 게시글 상세 정보 보기 (신청자 관점): " +
            "참가 예정인 게시글 상세 정보 보기 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-post-author-with-applicants")
    public String setupPostAuthorWithApplicants() {
        clearDatabases();

        Long postId = 1L;
        Long gameId = 1L;
        Long authorId = 1L;
        Long authorRegisterId = 1L;

        insertUser(
            authorId, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        for (long userId = 2; userId <= 6; userId += 1) {
            insertUser(
                userId, "user000" + userId, "Password!1",
                "사용자 " + userId, "남성", "010-0000-000" + userId, 5.0,
                "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        }
        insertPost(
            postId, authorId, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(authorRegisterId, authorId, gameId, "accepted");
        for (long userId = 2; userId <= 3; userId += 1) {
            Long registerId = userId;
            insertRegister(registerId, userId, gameId, "accepted");
        }
        for (long userId = 4; userId <= 6; userId += 1) {
            Long registerId = userId;
            insertRegister(registerId, userId, gameId, "processing");
        }

        return "운동 모집 게시글 상세 정보 보기 (작성자 관점): " +
            "신청자가 있는 경우 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-post-author-without-applicants")
    public String setupPostAuthorWithoutApplicants() {
        clearDatabases();

        Long postId = 1L;
        Long gameId = 1L;
        Long authorId = 1L;
        Long authorRegisterId = 1L;

        insertUser(
            authorId, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        for (long userId = 2; userId <= 3; userId += 1) {
            insertUser(
                userId, "user000" + userId, "Password!1",
                "사용자 " + userId, "남성", "010-0000-000" + userId, 5.0,
                "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        }
        insertPost(
            postId, authorId, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(authorRegisterId, authorId, gameId, "accepted");
        for (long userId = 2; userId <= 3; userId += 1) {
            Long registerId = userId;
            insertRegister(registerId, userId, gameId, "accepted");
        }

        return "운동 모집 게시글 상세 정보 보기 (작성자 관점): " +
            "신청자가 없는 경우 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-registrable-case")
    public String setupRegistrableCase() {
        clearDatabases();

        Long postId = 1L;
        Long user1Id = 1L;
        Long authorId = 2L;
        Long gameId = 1L;
        Long registerId = 1L;

        insertUser(
            user1Id, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            authorId, "author12", "Password!1",
            "작성자", "남성", "010-2345-6789", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            postId, authorId, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(registerId, authorId, gameId, "accepted");

        return "운동 참가 신청: " +
            "정상적으로 운동 참가를 신청하는 경우 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-cancellation-participation-case")
    public String setupCancellationParticipationCase() {
        clearDatabases();

        Long postId = 1L;
        Long authorId = 1L;
        Long userId = 2L;
        Long gameId = 1L;
        Long authorRegisterId = 1L;
        Long userRegisterId = 2L;

        insertUser(
            userId, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            authorId, "author12", "Password!1",
            "작성자", "남성", "010-2345-6789", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            postId, authorId, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(authorRegisterId, authorId, gameId, "accepted");
        insertRegister(userRegisterId, userId, gameId, "accepted");

        return "운동 참가 취소: " +
            "정상적으로 운동 참가를 취소하는 경우 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-cancellation-registration-case")
    public String setupCancellationRegistrationCase() {
        clearDatabases();

        Long postId = 1L;
        Long authorId = 1L;
        Long userId = 2L;
        Long gameId = 1L;
        Long authorRegisterId = 1L;
        Long userRegisterId = 2L;

        insertUser(
            userId, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            authorId, "author12", "Password!1",
            "작성자", "남성", "010-2345-6789", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            postId, authorId, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(authorRegisterId, authorId, gameId, "accepted");
        insertRegister(userRegisterId, userId, gameId, "processing");

        return "운동 참가 신청 취소: " +
            "정상적으로 운동 참가 신청을 취소하는 경우 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-accepting-registration-case")
    public String setupAcceptingRegistrationCase() {
        clearDatabases();

        Long postId = 1L;
        Long authorId = 1L;
        Long userId = 2L;
        Long gameId = 1L;
        Long authorRegisterId = 1L;
        Long userRegisterId = 2L;

        insertUser(
            userId, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            authorId, "author12", "Password!1",
            "작성자", "남성", "010-2345-6789", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            postId, authorId, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(authorRegisterId, authorId, gameId, "accepted");
        insertRegister(userRegisterId, userId, gameId, "processing");

        return "운동 참가 신청 수락: " +
            "정상적으로 운동 참가 신청을 수락하는 경우 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-cannot-acceptable-registration-case")
    public String setupCannotAcceptableRegistrationCase() {
        clearDatabases();

        Long postId = 1L;
        Long authorId = 1L;
        Long gameId = 1L;
        Long authorRegisterId = 1L;

        insertUser(
            authorId, "author12", "Password!1",
            "작성자", "남성", "010-2345-6789", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        for (long userId = 2; userId <= 4; userId += 1) {
            insertUser(
                userId, "user000" + userId, "Password!1",
                "사용자 " + userId, "남성", "010-0000-000" + userId, 5.0,
                "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        }
        insertPost(
            postId, authorId, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(authorRegisterId, authorId, gameId, "accepted");
        for (long userId = 2; userId <= 4; userId += 1) {
            Long registerId = userId;
            insertRegister(registerId, userId, gameId, "accepted");
        }

        Long applicantId = 5L;
        Long applicantRegisterId = 5L;
        insertUser(
            applicantId, "applicant12", "Password!1",
            "작성자", "남성", "010-9292-9292", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertRegister(applicantRegisterId, applicantId, gameId, "processing");

        return "운동 참가 신청 수락: " +
            "운동 참가 신청 수락이 불가능한 경우 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-rejecting-registration-case")
    public String setupRejectingRegistrationCase() {
        clearDatabases();

        Long postId = 1L;
        Long authorId = 1L;
        Long userId = 2L;
        Long gameId = 1L;
        Long authorRegisterId = 1L;
        Long userRegisterId = 2L;

        insertUser(
            userId, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            authorId, "author12", "Password!1",
            "작성자", "남성", "010-2345-6789", 9.9,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPost(
            postId, authorId, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");

        insertPostDataNotRelatedToUser();

        insertRegister(authorRegisterId, authorId, gameId, "accepted");
        insertRegister(userRegisterId, userId, gameId, "processing");

        return "운동 참가 신청 거절: " +
            "정상적으로 운동 참가 신청을 거절하는 경우 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-places-to-write-post")
    public String setupPlacesToWritePost() {
        clearDatabases();

        Long userId = 1L;
        Long place1Id = 1L;
        Long place2Id = 2L;
        Long place3Id = 3L;

        insertUser(
            userId, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPlace(
            place1Id, "대전 월드컵경기장", "042-000-0000", "축구",
            "대전 유성구 월드컵대로 32", "", "REGISTERED");
        insertPlace(
            place2Id, "대전 한밭종합운동장", "042-000-0001", "축구",
            "대전 중구 대종로 373", "", "REGISTERED");
        insertPlace(
            place3Id, "대전 한남대학교 종합운동장", "042-000-0002", "축구",
            "대전 대덕구 한남로 70", "", "REGISTERED");

        return "운동 모집 게시글 작성: " +
            "장소 입력 (등록된 장소를 입력하는 경우) 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-writing-post-case")
    public String setupWritingPostCase() {
        clearDatabases();

        Long userId = 1L;
        Long placeId = 1L;

        insertUser(
            userId, "user1234", "Password!1",
            "사용자 1", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertPlace(
            placeId, "김천종합운동장", "054-000-0000", "축구",
            "", "경북 김천시 삼락동 488-1", "REGISTERED");

        return "운동 모집 게시글 작성: " +
            "정상적으로 게시글을 등록하는 경우 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-deleting-post-case")
    public String setupDeletingPostCase() {
        clearDatabases();

        Long authorId = 1L;
        Long user1Id = 2L;
        Long user2Id = 3L;
        Long postId = 1L;
        Long gameId = 1L;
        Long placeId = 1L;
        Long authorRegisterId = 1L;
        Long user1RegisterId = 2L;
        Long user2RegisterId = 3L;

        insertUser(
            authorId, "author12", "Password!1",
            "작성자", "여성", "010-1234-5678", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user1Id, "user1234", "Password!1",
            "사용자 1", "여성", "010-2345-6789", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        insertUser(
            user2Id, "user5678", "Password!1",
            "사용자 1", "여성", "010-3456-7890", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");

        insertPost(
            postId, authorId, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 내용");
        insertPostImage(
            postId, "https://sports.seoul.go.kr/file/view/FID00000360/2.do", true);
        insertGame(
            gameId, postId, placeId, "축구",
            LocalDate.of(2022, 11, 13), LocalTime.of(15, 0), LocalTime.of(18, 0), 3);
        insertPlace(
            placeId, "김천종합운동장", "054-000-0000", "축구",
            "", "경북 김천시 삼락동 488-1", "REGISTERED");

        insertRegister(authorRegisterId, authorId, gameId, "accepted");
        insertRegister(user1RegisterId, user1Id, gameId, "accepted");
        insertRegister(user2RegisterId, user2Id, gameId, "processing");

        return "운동 모집 게시글 삭제: " +
            "정상적으로 게시글을 삭제하는 경우 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-login-required-case")
    public String setupLoginRequiredCase() {
        clearDatabases();

        return "로그인: " +
            "비로그인 시 특정 기능 이용 제한 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-login-case")
    public String setupLoginCase() {
        clearDatabases();

        Long userId = 1L;
        Long postId = 1L;
        Long gameId = 1L;
        Long placeId = 1L;
        Long registerId = 1L;

        insertUser(
            userId, "user1234", "Password!1",
            "사용자 1", "여성", "010-2345-6789", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");

        insertPost(
            postId, userId, 99L, LocalDateTime.now(), LocalDateTime.now(),
            "게시글 상세 정보라는데 아무 정보가 없다니...");
        insertPostImage(
            postId, "https://sports.seoul.go.kr/file/view/FID00000360/2.do", true);
        insertGame(
            gameId, postId, placeId, "축구",
            LocalDate.of(2022, 11, 13), LocalTime.of(15, 0), LocalTime.of(18, 0), 3);
        insertPlace(
            placeId, "김천종합운동장", "054-000-0000", "축구",
            "", "경북 김천시 삼락동 488-1", "REGISTERED");

        insertRegister(registerId, userId, gameId, "accepted");

        return "로그인: " +
            "로그인 관련 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-sign-up-case")
    public String setupSignUpCase() {
        clearDatabases();

        Long userId = 1L;

        insertUser(
            userId, "hsjkdss228", "Password!1",
            "사용자 1", "여성", "010-2345-6789", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");

        return "회원가입: " +
            "회원가입 관련 테스트 케이스 데이터를 세팅했습니다.";
    }

    @GetMapping("setup-notices")
    public String setupNotices() {
        clearDatabases();

        Long userId = 1L;

        insertUser(
            userId, "user1234", "Password!1",
            "사용자 1", "여성", "010-2345-6789", 10.0,
            "https://i1.sndcdn.com/avatars-mHalH2yXGeayCWfR-fsdm0w-t240x240.jpg");
        for (long noticeId = 6; noticeId >= 4; noticeId -= 1) {
            insertNotice(
                noticeId, userId,
                "알림 제목 " + noticeId, "알림 내용 " + noticeId, "unread",
                LocalDateTime.of(LocalDate.of(2023, 1, 1),
                    LocalTime.of((int) noticeId, 0, 0)
                )
            );
        }
        for (long noticeId = 3; noticeId >= 1; noticeId -= 1) {
            insertNotice(
                noticeId, userId,
                "알림 제목 " + noticeId, "알림 내용 " + noticeId, "read",
                LocalDateTime.of(LocalDate.of(2023, 1, 1),
                    LocalTime.of((int) noticeId, 0, 0)
                )
            );
        }

        return "알림: " +
            "알림 관련 테스트 케이스 데이터를 세팅했습니다.";
    }
}
