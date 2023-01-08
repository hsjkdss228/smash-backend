package kr.megaptera.smash.specifications;

import kr.megaptera.smash.models.place.Place;
import org.springframework.data.jpa.domain.Specification;

public class PlaceSpecification {
    public static Specification<Place> likeName(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
            root.get("information")
                .get("name"),
            "%" + keyword + "%");
    }
}
