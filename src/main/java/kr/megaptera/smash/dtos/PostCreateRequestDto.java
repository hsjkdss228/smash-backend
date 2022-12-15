package kr.megaptera.smash.dtos;

import javax.validation.Valid;

public class PostCreateRequestDto {
    @Valid
    private PostForPostCreateRequestDto post;

    @Valid
    private GameForPostCreateRequestDto game;

    @Valid
    private ExerciseForPostCreateRequestDto exercise;

    @Valid
    private PlaceForPostCreateRequestDto place;

    public PostCreateRequestDto() {

    }

    public PostCreateRequestDto(PostForPostCreateRequestDto post,
                                GameForPostCreateRequestDto game,
                                ExerciseForPostCreateRequestDto exercise,
                                PlaceForPostCreateRequestDto place
    ) {
        this.post = post;
        this.game = game;
        this.exercise = exercise;
        this.place = place;
    }

    public PostForPostCreateRequestDto getPost() {
        return post;
    }

    public GameForPostCreateRequestDto getGame() {
        return game;
    }

    public ExerciseForPostCreateRequestDto getExercise() {
        return exercise;
    }

    public PlaceForPostCreateRequestDto getPlace() {
        return place;
    }
}
