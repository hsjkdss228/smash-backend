package kr.megaptera.smash.dtos;

public class PlaceDto {
    private final Long id;

    private final String name;

    private final String exercise;

    private final String address;

    private final String contactNumber;

    public PlaceDto(Long id,
                    String name,
                    String exercise,
                    String address,
                    String contactNumber
    ) {
        this.id = id;
        this.name = name;
        this.exercise = exercise;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExercise() {
        return exercise;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}
