package org.gaurav.virtualpowerplantsystem.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryDto {

    private String name;

    private String postCode;

    private Integer capacity;

}
