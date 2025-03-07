package org.gaurav.virtualpowerplantsystem.service.impl;

import org.gaurav.virtualpowerplantsystem.builder.BatteryBuilder;
import org.gaurav.virtualpowerplantsystem.config.BatchConfig;
import org.gaurav.virtualpowerplantsystem.model.dto.BatteryDto;
import org.gaurav.virtualpowerplantsystem.model.dto.BatteryGridDto;
import org.gaurav.virtualpowerplantsystem.model.entity.Battery;
import org.gaurav.virtualpowerplantsystem.model.request.BatteriesFilterRequest;
import org.gaurav.virtualpowerplantsystem.model.request.BatteryListRequest;
import org.gaurav.virtualpowerplantsystem.model.request.BatteryRequest;
import org.gaurav.virtualpowerplantsystem.repository.BatteryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatteryServiceImplTest {

    @Mock
    private BatteryRepository batteryRepository;

    @Mock
    private Executor executor;

    @InjectMocks
    private BatteryServiceImpl batteryService;

    @Mock
    private BatchConfig batchConfig;

    @Test
    void whenBatteriesPersisted_saveBatteries_thenSuccess() {
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(executor).execute(any(Runnable.class));

        List<BatteryRequest> batteryRequests = new ArrayList<>();
        IntStream.range(0, 2500).
                forEach(i -> batteryRequests.add(BatteryRequest.builder().name("Midland").postcode("12345").capacity(100).build()));
        BatteryListRequest batteryListRequest = new BatteryListRequest(batteryRequests);

        List<Battery> savedBatteries = batteryRequests.stream().map(BatteryBuilder::buildBattery).toList();

        when(batchConfig.getSize()).thenReturn(1000);
        when(batteryRepository.batchInsert(anyList(), anyInt())).thenReturn(savedBatteries);
        CompletableFuture<List<BatteryDto>> listCompletableFuture = batteryService.saveBatteries(batteryListRequest);
        List<BatteryDto> batteryDtos = listCompletableFuture.join();

        assertEquals(batteryDtos.size(), savedBatteries.size());
        verify(batteryRepository, times(1)).batchInsert(anyList(), anyInt());
    }

    @Test
    void whenRequestedFilterData_findAll_thenBatteryGridFetchSuccess() {
        BatteriesFilterRequest filterRequest = new BatteriesFilterRequest();
        filterRequest.setStartPostCode("1000");
        filterRequest.setEndPostCode("2000");
        filterRequest.setMinCapacity(50);
        filterRequest.setMaxCapacity(100);

        Battery batteryOne = Battery.builder().name("Cannington").capacity(60).build();
        Battery batteryTwo = Battery.builder().name("Midland").capacity(70).build();
        Battery batteryThree = Battery.builder().name("Hay Street").capacity(80).build();
        List<Battery> batteryList = Arrays.asList(batteryOne, batteryTwo, batteryThree);

        when(batteryRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(batteryList);
        BatteryGridDto batteryGridDto = batteryService.getBatteriesGrid(filterRequest);

        assertEquals(batteryGridDto.getBatteryNames().size(), batteryList.size());
    }

    @Test
    void whenNoDataInDatabaseForFilterData_findAll_thenEmptyBatteryGrid() {
        BatteriesFilterRequest filterRequest = new BatteriesFilterRequest();
        filterRequest.setStartPostCode("1000");
        filterRequest.setEndPostCode("2000");
        filterRequest.setMinCapacity(50);
        filterRequest.setMaxCapacity(100);

        when(batteryRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(Collections.emptyList());
        BatteryGridDto batteryGridDto = batteryService.getBatteriesGrid(filterRequest);
        assertNull(batteryGridDto);

    }

}
