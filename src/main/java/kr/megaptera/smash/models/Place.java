package kr.megaptera.smash.models;

import kr.megaptera.smash.dtos.PlaceDto;
import kr.megaptera.smash.dtos.PlaceInPostListDto;
import kr.megaptera.smash.dtos.PlaceSearchResultDto;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "places")
public class Place {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private PlaceInformation information;

    // TODO: 운동들을 ElementCollection으로 가지거나, 운동이 Entity라면
    //   카테고리처럼 쓸 수 있게 할 수 있을 것 같다.
    //   ex. 장소 한 곳에 풋살장, 농구장, 테니스장 등이 모두 있는 경우
    @Embedded
    private Exercise exercise;

    @Embedded
    private PlaceAddress address;

    @Enumerated(EnumType.STRING)
    private PlaceRegistrationStatus registrationStatus;

    private Place() {

    }

    public Place(PlaceInformation information,
                 Exercise exercise,
                 PlaceAddress address,
                 PlaceRegistrationStatus registrationStatus
    ) {
        this.information = information;
        this.exercise = exercise;
        this.address = address;
        this.registrationStatus = registrationStatus;
    }

    public Place(Long id,
                 PlaceInformation information,
                 Exercise exercise,
                 PlaceAddress address,
                 PlaceRegistrationStatus registrationStatus
    ) {
        this.id = id;
        this.information = information;
        this.exercise = exercise;
        this.address = address;
        this.registrationStatus = registrationStatus;
    }

    // TODO: 일단은 도로명 주소로만 받지만,
    //    도로명 주소로 받을지 지번 주소로 받을지 구분해주는 로직 추가

    public static Place of(
        String placeName,
        String exerciseName,
        String address,
        Boolean isRegisteredPlace
    ) {
        return new Place(
            new PlaceInformation(
                placeName,
                ""
            ),
            new Exercise(exerciseName),
            new PlaceAddress(
                address,
                ""
            ),
            isRegisteredPlace
                ? PlaceRegistrationStatus.REGISTERED
                : PlaceRegistrationStatus.UNREGISTERED
        );
    }

    public Long id() {
        return id;
    }

    public PlaceInformation information() {
        return information;
    }

    public Exercise exercise() {
        return exercise;
    }

    public PlaceAddress address() {
        return address;
    }

    public PlaceRegistrationStatus registrationStatus() {
        return registrationStatus;
    }

    public boolean registered() {
        return registrationStatus.equals(PlaceRegistrationStatus.REGISTERED);
    }

    public PlaceInPostListDto toPlaceInPostListDto() {
        return new PlaceInPostListDto(
            information.name()
        );
    }

    // TODO: 장소 등록 상태는 필요에 따라 구분되는 fake를 만들어야 할 수 있을 듯

    public static Place fake(String placeName) {
        Long placeId = 1L;
        return new Place(
            placeId,
            new PlaceInformation(
                placeName,
                "010-1234-1234"
            ),
            new Exercise("운동 카테고리 이름"),
            new PlaceAddress(
                "운동 장소 도로명주소",
                "운동 장소 지번주소"
            ),
            PlaceRegistrationStatus.REGISTERED
        );
    }

    public static Place fake(String placeName,
                             PlaceRegistrationStatus placeRegisterStatus) {
        Long placeId = 1L;
        return new Place(
            placeId,
            new PlaceInformation(
                placeName,
                "010-1234-1234"
            ),
            new Exercise("운동 카테고리 이름"),
            new PlaceAddress(
                "운동 장소 도로명주소",
                "운동 장소 지번주소"
            ),
            placeRegisterStatus
        );
    }

    public static List<Place> fakes(long generationCount) {
        List<Place> places = new ArrayList<>();
        for (long i = 1; i <= generationCount; i += 1) {
            Long placeId = i;
            Place place = new Place(
                placeId,
                new PlaceInformation(
                    "운동 장소 이름 " + i,
                    "010-1234-567" + i
                ),
                new Exercise("운동 카테고리 이름 " + i),
                new PlaceAddress(
                    "운동 장소 도로명주소 " + i,
                    "운동 장소 지번주소 " + i
                ),
                PlaceRegistrationStatus.REGISTERED
            );
            places.add(place);
        }
        return places;
    }

    public PlaceDto toDto() {
        return new PlaceDto(
            id,
            information.name(),
            exercise.name(),
            address.existing(),
            information.contactNumber()
        );
    }

    public PlaceSearchResultDto toSearchResultDto() {
        return new PlaceSearchResultDto(
            id,
            information.name(),
            address.existing()
        );
    }
}
