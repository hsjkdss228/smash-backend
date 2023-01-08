package kr.megaptera.smash.models;

public enum PlaceRegistrationStatus {
    REGISTERED("등록"),
    UNREGISTERED("미등록");

    private String registrationStatus;

    PlaceRegistrationStatus() {

    }

    PlaceRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String value() {
        return registrationStatus;
    }
}
