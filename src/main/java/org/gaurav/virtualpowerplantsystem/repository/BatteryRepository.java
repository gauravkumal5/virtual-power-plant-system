package org.gaurav.virtualpowerplantsystem.repository;

import org.gaurav.virtualpowerplantsystem.dao.BatteryDao;
import org.gaurav.virtualpowerplantsystem.model.entity.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long>, JpaSpecificationExecutor<Battery>, BatteryDao {
}
