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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BatteryController.class)
class BatteryRepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BatteryService batteryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenValidBatteries_callBatterySaveApi_thenReturnSuccess() throws Exception {
        var batteryRequest = BatteryRequest.builder().name("Cannington").postcode("6107").capacity(13500).build();
        var batteryRequestList = List.of(batteryRequest);

        var batteryListRequest = BatteryListRequest.builder().batteryRequestList(batteryRequestList).build();
        List<BatteryDto> batteryDtoList = List.of(BatteryDto.builder().name("Cannington").postCode("6107").capacity(13500).build());

        when(batteryService.saveBatteries(any(BatteryListRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(batteryDtoList));

        MvcResult result = mockMvc.perform(post("/battery/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batteryListRequest)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data[0].name").value("Cannington"))
                .andExpect(jsonPath("$.data[0].postCode").value("6107"))
                .andExpect(jsonPath("$.data[0].capacity").value(13500));
    }

    @Test
    void whenInvalidBatteryName_callBatterySaveApi_thenReturnInvalidBatteryResponse() throws Exception {
        var batteryRequest = BatteryRequest.builder().postcode("6057").capacity(100).build();
        var batteryListRequest = BatteryListRequest.builder().batteryRequestList(List.of(batteryRequest)).build();

        mockMvc.perform(post("/battery/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batteryListRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.data").value("Name cannot be empty."));
    }

    @Test
    void whenEmptyList_callBatterySaveApi_thenReturnInvalidListResponse() throws Exception {
        var batteryListRequest = BatteryListRequest.builder().batteryRequestList(Collections.emptyList()).build();

        mockMvc.perform(post("/battery/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batteryListRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.data").value("Battery list must have at least one battery."));
    }

    @Test
    void whenExceptionOccurred_callBatterySaveApi_thenReturnHandledResponse() throws Exception {
        var batteryRequest = BatteryRequest.builder().name("Cannington").postcode("12345").capacity(100).build();
        var batteryRequestList = List.of(batteryRequest);

        var batteryListRequest = new BatteryListRequest();
        batteryListRequest.setBatteryRequestList(batteryRequestList);

        when(batteryService.saveBatteries(any())).thenThrow(new RuntimeException());
        mockMvc.perform(post("/battery/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batteryListRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Sorry, some problem occurred."));
    }

    @Test
    void whenValidFilter_getBatteriesGrid_thenReturnsBatteryGridResponse() throws Exception {
        var filterRequest = BatteriesFilterRequest.builder().startPostCode("12345").build();
        var gridResponse = BatteryGridDto.builder().batteryNames(List.of("Akunda Bay","Bagot")).totalCapacity(5000).averageCapacity(5000).build();

        when(batteryService.getBatteriesGrid(any())).thenReturn(gridResponse);
        mockMvc.perform(post("/battery/grid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.batteryNames").value(hasItems("Akunda Bay", "Bagot")))
                .andExpect(jsonPath("$.data.totalCapacity").value(5000))
                .andExpect(jsonPath("$.data.averageCapacity").value(5000));
    }

    @Test
    void whenInvalidFilter_getBatteriesGrid_thenReturnInvalidFilterResponse() throws Exception {
        var filterRequest = BatteriesFilterRequest.builder().startPostCode("12345S").build();
        mockMvc.perform(post("/battery/grid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.data").value("Post code validation doesnt match"));
    }

    @Test
    void whenExceptionOccurred_getBatteriesGrid_thenReturnResponse() throws Exception {
        var filterRequest = new BatteriesFilterRequest();
        when(batteryService.getBatteriesGrid(any())).thenThrow(new RuntimeException("Service failure"));

        mockMvc.perform(post("/battery/grid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Sorry, some problem occurred."));
    }
}