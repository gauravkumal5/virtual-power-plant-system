package org.gaurav.virtualpowerplantsystem.specification;

import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.gaurav.virtualpowerplantsystem.model.entity.Battery;
import org.gaurav.virtualpowerplantsystem.model.request.BatteriesFilterRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

@UtilityClass
public class BatterySearchSpecification {

    public static Specification<Battery> createSpecification(BatteriesFilterRequest batteriesFilterRequest) {

        return (root, query, builder) -> {
            var predicates = new ArrayList<>();

            if (batteriesFilterRequest.getStartPostCode() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("postCode"), batteriesFilterRequest.getStartPostCode()));

            }
            if (batteriesFilterRequest.getEndPostCode() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("postCode"), batteriesFilterRequest.getEndPostCode()));

            }

            if (batteriesFilterRequest.getMinCapacity() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("capacity"), batteriesFilterRequest.getMinCapacity()));

            }
            if (batteriesFilterRequest.getMaxCapacity() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("capacity"), batteriesFilterRequest.getMaxCapacity()));

            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
