package org.gaurav.virtualpowerplantsystem.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.gaurav.virtualpowerplantsystem.model.dto.BatteryDto;
import org.gaurav.virtualpowerplantsystem.model.dto.BatteryGridDto;
import org.gaurav.virtualpowerplantsystem.model.request.BatteriesFilterRequest;
import org.gaurav.virtualpowerplantsystem.model.request.BatteryListRequest;
import org.gaurav.virtualpowerplantsystem.model.response.ApiResponse;
import org.gaurav.virtualpowerplantsystem.service.BatteryService;
import org.gaurav.virtualpowerplantsystem.utility.ResponseUtility;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/battery")
@RequiredArgsConstructor
public class BatteryController {

    @NonNull
    private final BatteryService batteryService;

    @PostMapping("/save")
    public CompletableFuture<ResponseEntity<ApiResponse<List<BatteryDto>>>> addBatteries(@Valid @RequestBody BatteryListRequest batteryListRequest) {
        CompletableFuture<List<BatteryDto>> listCompletableFuture = batteryService.saveBatteries(batteryListRequest);
        return listCompletableFuture.thenApply(batteryDtos -> ResponseUtility.createdResponse("Batteries added successfully", batteryDtos));
    }

    @PostMapping("/grid")
    public ResponseEntity<ApiResponse<BatteryGridDto>> getBatteriesGrid(@Valid @RequestBody BatteriesFilterRequest batteriesFilterRequest) {
        BatteryGridDto batteryGridDto = batteryService.getBatteriesGrid(batteriesFilterRequest);
        if (batteryGridDto == null) {
            return ResponseUtility.okResponse("Battery grid not found for requested filter.");
        }
        return ResponseUtility.okWithDataResponse("Battery grid fetched successfully", batteryGridDto);
    }

}
