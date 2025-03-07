package org.gaurav.virtualpowerplantsystem.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryListRequest {

    @Valid
    @NotNull(message = "Battery list cannot be null")
    @Size(min = 1, message = "Battery list must have at least one battery.")
    private List<BatteryRequest> batteryRequestList;

}
