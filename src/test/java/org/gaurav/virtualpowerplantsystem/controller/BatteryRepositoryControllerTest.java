package org.gaurav.virtualpowerplantsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gaurav.virtualpowerplantsystem.model.dto.BatteryDto;
import org.gaurav.virtualpowerplantsystem.model.dto.BatteryGridDto;
import org.gaurav.virtualpowerplantsystem.model.request.BatteriesFilterRequest;
import org.gaurav.virtualpowerplantsystem.model.request.BatteryListRequest;
import org.gaurav.virtualpowerplantsystem.model.request.BatteryRequest;
import org.gaurav.virtualpowerplantsystem.service.BatteryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(BatteryController.class)
class BatteryRepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BatteryService batteryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenValidBatteries_addedBatteries_thenReturnSuccess() throws Exception {
        BatteryRequest batteryRequest = BatteryRequest.builder().name("Cannington").postcode("12345").capacity(100).build();
        List<BatteryRequest> batteryRequestList = List.of(batteryRequest);

        BatteryListRequest batteryListRequest = BatteryListRequest.builder().batteryRequestList(batteryRequestList).build();
        List<BatteryDto> batteryDtoList = new ArrayList<>();

        when(batteryService.saveBatteries(any(BatteryListRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(batteryDtoList));
        mockMvc.perform(post("/battery/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batteryListRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void whenInvalidBatteries_addedBatteries_thenReturnInvalidBatteryResponse() throws Exception {
        BatteryListRequest batteryListRequest = BatteryListRequest.builder().batteryRequestList(List.of(new BatteryRequest())).build();

        mockMvc.perform(post("/battery/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batteryListRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenEmptyList_addedBatteries_thenReturnInvalidListResponse() throws Exception {
        BatteryListRequest batteryListRequest = BatteryListRequest.builder().batteryRequestList(Collections.emptyList()).build();

        mockMvc.perform(post("/battery/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batteryListRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenExceptionOccurred_addedBatteries_thenReturnHandledResponse() throws Exception {
        BatteryRequest batteryRequest = BatteryRequest.builder().name("Cannington").postcode("12345").capacity(100).build();
        List<BatteryRequest> batteryRequestList = List.of(batteryRequest);

        BatteryListRequest batteryListRequest = new BatteryListRequest();
        batteryListRequest.setBatteryRequestList(batteryRequestList);

        when(batteryService.saveBatteries(any())).thenThrow(new RuntimeException("Service failure"));
        mockMvc.perform(post("/battery/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batteryListRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenValidFilter_getBatteriesGrid_thenReturnsBatteryGridResponse() throws Exception {
        BatteriesFilterRequest filterRequest = BatteriesFilterRequest.builder().startPostCode("12345").build();
        BatteryGridDto gridResponse = BatteryGridDto.builder().totalCapacity(5000).build();

        when(batteryService.getBatteriesGrid(any())).thenReturn(gridResponse);
        mockMvc.perform(post("/battery/grid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalCapacity").value(5000));
    }

    @Test
    void whenInvalidFilter_getBatteriesGrid_thenReturnInvalidFilterResponse() throws Exception {
        BatteriesFilterRequest filterRequest = BatteriesFilterRequest.builder().startPostCode("12345S").build();
        mockMvc.perform(post("/battery/grid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenExceptionOccurred_getBatteriesGrid_thenReturnResponse() throws Exception {
        BatteriesFilterRequest filterRequest = new BatteriesFilterRequest();
        when(batteryService.getBatteriesGrid(any())).thenThrow(new RuntimeException("Service failure"));

        mockMvc.perform(post("/battery/grid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterRequest)))
                .andExpect(status().isInternalServerError());
    }


}