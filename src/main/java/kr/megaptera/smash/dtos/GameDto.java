package kr.megaptera.smash.dtos;

import java.util.List;

public class GameDto {
    private final Long id;

    private final Long postId;

    private final String exercise;

    private final String exerciseDate;

    private final String exerciseType;

    private final String exerciseLevel;

    private final String exerciseGender;

    private final Integer cost;

    private final String place;

    private final List<TeamDto> teams;

    private final String userStatus;

    private final Long roleIdOfAccessedUser;

    public GameDto(Long id,
                   Long postId,
                   String exercise,
                   String exerciseDate,
                   String exerciseType,
                   String exerciseLevel,
                   String exerciseGender,
                   Integer cost,
                   String place,
                   List<TeamDto> teams
    ) {
        this.id = id;
        this.postId = postId;
        this.exercise = exercise;
        this.exerciseDate = exerciseDate;
        this.exerciseType = exerciseType;
        this.exerciseLevel = exerciseLevel;
        this.exerciseGender = exerciseGender;
        this.cost = cost;
        this.place = place;
        this.teams = teams;
        this.userStatus = null;
        this.roleIdOfAccessedUser = null;
    }

    public GameDto(Long id,
                   Long postId,
                   String exercise,
                   String exerciseDate,
                   String exerciseType,
                   String exerciseLevel,
                   String exerciseGender,
                   Integer cost,
                   String place,
                   List<TeamDto> teams,
                   String userStatus,
                   Long roleIdOfAccessedUser
    ) {
        this.id = id;
        this.postId = postId;
        this.exercise = exercise;
        this.exerciseDate = exerciseDate;
        this.exerciseType = exerciseType;
        this.exerciseLevel = exerciseLevel;
        this.exerciseGender = exerciseGender;
        this.cost = cost;
        this.place = place;
        this.teams = teams;
        this.userStatus = userStatus;
        this.roleIdOfAccessedUser = roleIdOfAccessedUser;
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public String getExercise() {
        return exercise;
    }

    public String getExerciseDate() {
        return exerciseDate;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public String getExerciseLevel() {
        return exerciseLevel;
    }

    public String getExerciseGender() {
        return exerciseGender;
    }

    public Integer getCost() {
        return cost;
    }

    public String getPlace() {
        return place;
    }

    public List<TeamDto> getTeams() {
        return teams;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public Long getRoleIdOfAccessedUser() {
        return roleIdOfAccessedUser;
    }
}
