package org.gaurav.virtualpowerplantsystem.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryGridDto {

    private List<String> batteryNames;
    private int totalCapacity;
    private double averageCapacity;

}
