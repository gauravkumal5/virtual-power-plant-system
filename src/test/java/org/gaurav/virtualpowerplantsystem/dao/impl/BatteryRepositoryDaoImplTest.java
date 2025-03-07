package org.gaurav.virtualpowerplantsystem.dao.impl;

import jakarta.persistence.EntityManager;
import org.gaurav.virtualpowerplantsystem.model.entity.Battery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatteryRepositoryDaoImplTest {

    @InjectMocks
    private BatteryDaoImpl batteryRepository;

    @Mock
    private EntityManager entityManager;

    private List<Battery> batteryList;

    @BeforeEach
    void setUp() {
        batteryList = Stream.generate(Battery::new).limit(3).toList();

    }

    @Test
    void whenValidBatchInsert_batchInsert_thenPersistInBatchSuccess() {
        List<Battery> result = batteryRepository.batchInsert(batteryList, 2);

        assertEquals(batteryList.size(), result.size());
        verify(entityManager, times(3)).persist(any(Battery.class));
        verify(entityManager, atLeastOnce()).flush();
    }

    @Test
    void whenDatabaseErrorOnBatchInsert_batchInsert_thenNoDataPersists() {
        doThrow(new RuntimeException("Database Error")).when(entityManager).persist(any(Battery.class));

        assertThrows(RuntimeException.class, () ->
                batteryRepository.batchInsert(batteryList, 2));
        verify(entityManager, atLeastOnce()).persist(any(Battery.class));
        verify(entityManager, never()).flush();
    }
}

