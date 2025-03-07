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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BatteryRepositoryRepositoryIntegrationTest extends ContainerDbConnection {

    @Autowired
    private BatteryRepository batteryRepository;

    @Override
    @BeforeEach
    public void setUp() {
      Battery batteryOne =Battery.builder().name("Cannington").postCode("1000").capacity(50).build();
      Battery batteryTwo =Battery.builder().name("Midland").postCode("9000").capacity(75).build();
        batteryRepository.save(batteryOne);
        batteryRepository.save(batteryTwo);
    }

    @Test
    void whenValidFilter_findAllBySpecAndSort_thenReturnsBatteriesWithinSpecifiedRange() {

        BatteriesFilterRequest filterRequest = new BatteriesFilterRequest();
        filterRequest.setMinCapacity(50);
        filterRequest.setMaxCapacity(75);

        Specification<Battery> spec = BatterySearchSpecification.createSpecification(filterRequest);
        Sort sort = Sort.by(Sort.Order.asc("name"));
        List<Battery> batteries = batteryRepository.findAll(spec, sort);

        assertThat(batteries).hasSize(2);
        assertThat(batteries).extracting(Battery::getName).containsExactlyInAnyOrder("Cannington", "Midland");
    }

    @Test
    void whenNoDataInDatabaseForFilterData_findAllBySpecAndSort_thenReturnsEmptyList() {

        BatteriesFilterRequest filterRequest = new BatteriesFilterRequest();
        filterRequest.setMinCapacity(100);
        Specification<Battery> spec = BatterySearchSpecification.createSpecification(filterRequest);
        Sort sort = Sort.by(Sort.Order.asc("name"));
        List<Battery> batteries = batteryRepository.findAll(spec, sort);
        assertThat(batteries).isEmpty();
    }
}
