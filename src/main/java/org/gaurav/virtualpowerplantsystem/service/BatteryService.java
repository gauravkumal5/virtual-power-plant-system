package org.gaurav.virtualpowerplantsystem.service;

import org.gaurav.virtualpowerplantsystem.model.dto.BatteryDto;
import org.gaurav.virtualpowerplantsystem.model.dto.BatteryGridDto;
import org.gaurav.virtualpowerplantsystem.model.request.BatteriesFilterRequest;
import org.gaurav.virtualpowerplantsystem.model.request.BatteryListRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BatteryService {

    CompletableFuture<List<BatteryDto>> saveBatteries(BatteryListRequest batteryListRequest);

    BatteryGridDto getBatteriesGrid(BatteriesFilterRequest batteriesFilterRequest);
}
