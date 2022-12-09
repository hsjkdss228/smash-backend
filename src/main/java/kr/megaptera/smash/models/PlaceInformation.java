package kr.megaptera.smash.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class PlaceInformation {
    @Column(name = "name")
    private String name;

    @Column(name = "contact_number")
    private String contactNumber;

    private PlaceInformation() {

    }

    public PlaceInformation(String name, String contactNumber) {
        this.name = name;
        this.contactNumber = contactNumber;
    }

    public String name() {
        return name;
    }

    public String contactNumber() {
        return contactNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlaceInformation that = (PlaceInformation) o;
        return Objects.equals(name, that.name)
            && Objects.equals(contactNumber, that.contactNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, contactNumber);
    }

    @Override
    public String toString() {
        return "PlaceInformation{" +
            "name='" + name + '\'' +
            ", contactNumber='" + contactNumber + '\'' +
            '}';
    }
}
