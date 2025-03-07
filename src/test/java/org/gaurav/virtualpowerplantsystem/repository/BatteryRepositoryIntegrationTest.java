package org.gaurav.virtualpowerplantsystem.repository;

import org.gaurav.virtualpowerplantsystem.ContainerDbConnection;
import org.gaurav.virtualpowerplantsystem.model.entity.Battery;
import org.gaurav.virtualpowerplantsystem.model.request.BatteriesFilterRequest;
import org.gaurav.virtualpowerplantsystem.specification.BatterySearchSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BatteryRepositoryIntegrationTest extends ContainerDbConnection {

    @Autowired
    private BatteryRepository batteryRepository;

    @Override
    @BeforeEach
    public void setUp() {
        var batteryOne = Battery.builder().name("Cannington").postCode("1000").capacity(50).build();
        var batteryTwo = Battery.builder().name("Midland").postCode("9000").capacity(75).build();
        batteryRepository.save(batteryOne);
        batteryRepository.save(batteryTwo);
    }

    @Test
    void whenValidFilter_findAllBySpecAndSort_thenReturnsBatteriesWithinSpecifiedRange() {

        var batteriesFilterRequest = BatteriesFilterRequest.builder().maxCapacity(50).maxCapacity(75).build();
        var specification = BatterySearchSpecification.createSpecification(batteriesFilterRequest);
        var sort = Sort.by(Sort.Order.asc("name"));
        var batteries = batteryRepository.findAll(specification, sort);

        assertThat(batteries).hasSize(2);
        assertThat(batteries).extracting(Battery::getName).containsExactlyInAnyOrder("Cannington", "Midland");
    }

    @Test
    void whenNoDataInDatabaseForFilterData_findAllBySpecAndSort_thenReturnsEmptyList() {

        var filterRequest = BatteriesFilterRequest.builder().minCapacity(100).build();
        var spec = BatterySearchSpecification.createSpecification(filterRequest);
        var sort = Sort.by(Sort.Order.asc("name"));
        var batteries = batteryRepository.findAll(spec, sort);
        assertThat(batteries).isEmpty();
    }
}
