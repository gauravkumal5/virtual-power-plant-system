package org.gaurav.virtualpowerplantsystem.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryRequest {

    @NotBlank(message = "Name cannot be empty.")
    @Pattern(regexp = "^[A-Za-z]+( [A-Za-z]+){0,255}$", message = "Name pattern doesn't match. It must contain alphabets and space in middle only.")
    private String name;

    @NotBlank(message = "Post code cannot be empty.")
    @Pattern(regexp = "\\d+", message = "Post code validation doesn't match. It must contain digits only.")
    private String postcode;

    @NotNull(message = "Capacity cannot be null.")
    private Integer capacity;
}
