package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceAddressTest {
    @Test
    void equals() {
        assertThat(new PlaceAddress("도로명 주소 1", "지번 주소 1"))
            .isEqualTo(new PlaceAddress("도로명 주소 1", "지번 주소 1"));
        assertThat(new PlaceAddress("도로명 주소 1", "지번 주소 1"))
            .isNotEqualTo(new PlaceAddress("도로명 주소 2", "지번 주소 2"));
        assertThat(new PlaceAddress("도로명 주소", "지번 주소"))
            .isNotEqualTo(null);
    }

    @Test
    void existing() {
        PlaceAddress placeAddress = new PlaceAddress("도로명 주소", "지번 주소");
        assertThat(placeAddress.existing()).isEqualTo("도로명 주소");

        PlaceAddress placeAddressWithoutJibun = new PlaceAddress("도로명 주소", "");
        assertThat(placeAddressWithoutJibun.existing()).isEqualTo("도로명 주소");

        PlaceAddress placeAddressWithoutRoad = new PlaceAddress("", "지번 주소");
        assertThat(placeAddressWithoutRoad.existing()).isEqualTo("지번 주소");
    }
}
