package kr.megaptera.smash.exceptions;

public class PlaceNotFound extends RuntimeException {
    public PlaceNotFound(String placeName) {
        super("주어진 장소 이름에 해당하는 장소를 찾을 수 없습니다: (placeName: " + placeName + ")");
    }

    public PlaceNotFound(Long placeId) {
        super("주어진 장소 번호에 해당하는 장소를 찾을 수 없습니다: (placeId: " + placeId + ")");
    }
}
