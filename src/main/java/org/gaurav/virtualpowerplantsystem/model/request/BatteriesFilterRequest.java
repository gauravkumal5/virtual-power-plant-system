package org.gaurav.virtualpowerplantsystem.model.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteriesFilterRequest {

    @Pattern(regexp = "\\d+", message = "Post code validation doesnt match")
    private String startPostCode;

    @Pattern(regexp = "\\d+", message = "Post code validation doesnt match")
    private String endPostCode;

    @Positive(message = "Min capacity must be greater than 0.")
    private Integer minCapacity;

    @Positive(message = "Max capacity must be greater than 0.")
    private Integer maxCapacity;

}
