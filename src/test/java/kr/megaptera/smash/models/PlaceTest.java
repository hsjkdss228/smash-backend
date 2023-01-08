package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceTest {
    @Test
    void registered() {
        Place registeredPlace = Place.fake(
            "등록된 장소",
            PlaceRegistrationStatus.REGISTERED
        );
        assertThat(registeredPlace.registered()).isTrue();
    }

    @Test
    void unregistered() {
        Place unregisteredPlace = Place.fake(
            "미등록된 장소",
            PlaceRegistrationStatus.UNREGISTERED
        );
        assertThat(unregisteredPlace.registered()).isFalse();
    }
}