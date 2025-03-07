package org.gaurav.virtualpowerplantsystem.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.gaurav.virtualpowerplantsystem.model.entity.Battery;
import org.gaurav.virtualpowerplantsystem.dao.BatteryDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BatteryDaoImpl implements BatteryDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public List<Battery> batchInsert(List<Battery> batteries, int batchSize) {
        for (int i = 0; i < batteries.size(); i++) {
            entityManager.persist(batteries.get(i));

            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
            }
        }

        entityManager.flush();
        return batteries;
    }
}
