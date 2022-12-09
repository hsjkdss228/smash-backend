package kr.megaptera.smash.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class PlaceAddress {
    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "jibun_address")
    private String jibunAddress;

    private PlaceAddress() {

    }

    public PlaceAddress(String roadAddress,
                        String jibunAddress) {
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
    }

    public String roadAddress() {
        return roadAddress;
    }

    public String jibunAddress() {
        return jibunAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlaceAddress that = (PlaceAddress) o;
        return Objects.equals(roadAddress, that.roadAddress)
            && Objects.equals(jibunAddress, that.jibunAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roadAddress, jibunAddress);
    }

    @Override
    public String toString() {
        return "PlaceAddress{" +
            "roadAddress='" + roadAddress + '\'' +
            ", jibunAddress='" + jibunAddress + '\'' +
            '}';
    }
}
