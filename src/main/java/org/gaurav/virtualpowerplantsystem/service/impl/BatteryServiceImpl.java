package org.gaurav.virtualpowerplantsystem.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gaurav.virtualpowerplantsystem.builder.BatteryBuilder;
import org.gaurav.virtualpowerplantsystem.config.BatchConfig;
import org.gaurav.virtualpowerplantsystem.model.dto.BatteryDto;
import org.gaurav.virtualpowerplantsystem.model.dto.BatteryGridDto;
import org.gaurav.virtualpowerplantsystem.model.entity.Battery;
import org.gaurav.virtualpowerplantsystem.model.request.BatteriesFilterRequest;
import org.gaurav.virtualpowerplantsystem.model.request.BatteryListRequest;
import org.gaurav.virtualpowerplantsystem.model.request.BatteryRequest;
import org.gaurav.virtualpowerplantsystem.repository.BatteryRepository;
import org.gaurav.virtualpowerplantsystem.service.BatteryService;
import org.gaurav.virtualpowerplantsystem.specification.BatterySearchSpecification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@Slf4j
public class BatteryServiceImpl implements BatteryService {

    private final BatteryRepository batteryRepository;

    private final Executor executor;

    private final BatchConfig batchConfig;

    public BatteryServiceImpl(BatteryRepository batteryRepository, @Qualifier("batteryTaskExecutor") Executor executor, BatchConfig batchConfig) {
        this.batteryRepository = batteryRepository;
        this.executor = executor;
        this.batchConfig = batchConfig;
    }

    @Override
    public CompletableFuture<List<BatteryDto>> saveBatteries(BatteryListRequest batteryListRequest) {

        List<BatteryRequest> batteryRequestList = batteryListRequest.getBatteryRequestList();
        int batchSize = batchConfig.getSize();

        List<Battery> batteries = batteryRequestList.parallelStream().map(BatteryBuilder::buildBattery).toList();
        CompletableFuture<List<Battery>> listCompletableFuture =
                CompletableFuture.supplyAsync(() -> batteryRepository.batchInsert(batteries, batchSize), executor);
        return listCompletableFuture.thenApply(v -> v.parallelStream().map(BatteryBuilder::buildBatteryDto).toList());
    }

    @Override
    public BatteryGridDto getBatteriesGrid(BatteriesFilterRequest batteriesFilterRequest) {

        Specification<Battery> spec = BatterySearchSpecification.createSpecification(batteriesFilterRequest);
        Sort sort = Sort.by(Sort.Order.asc("name"));
        List<Battery> batteries = batteryRepository.findAll(spec, sort);
        List<String> names = batteries.stream().map(Battery::getName).toList();

        if (names.isEmpty()) {
            return null;
        }
        int totalCapacity = batteries.stream().mapToInt(Battery::getCapacity).sum();
        double averageCapacity = totalCapacity / (double) batteries.size();
        return BatteryBuilder.buildBatteryGridDto(names, totalCapacity, averageCapacity);

    }
}
