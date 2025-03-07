package org.gaurav.virtualpowerplantsystem.builder;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.gaurav.virtualpowerplantsystem.model.entity.Battery;
import org.gaurav.virtualpowerplantsystem.model.dto.BatteryDto;
import org.gaurav.virtualpowerplantsystem.model.dto.BatteryGridDto;
import org.gaurav.virtualpowerplantsystem.model.request.BatteryRequest;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatteryBuilder {

    public static Battery buildBattery(BatteryRequest batteryRequest) {
        return Battery.builder()
                .name(batteryRequest.getName())
                .postCode(batteryRequest.getPostcode())
                .capacity(batteryRequest.getCapacity())
                .build();
    }

    public static BatteryGridDto buildBatteryGridDto(List<String> names, int totalCapacity, double averageCapacity) {
        return BatteryGridDto.builder()
                .batteryNames(names)
                .totalCapacity(totalCapacity)
                .averageCapacity(averageCapacity)
                .build();

    }

    public static BatteryDto buildBatteryDto(Battery battery) {
        return BatteryDto.builder()
                .name(battery.getName())
                .capacity(battery.getCapacity())
                .postCode(battery.getPostCode())
                .build();
    }
}
