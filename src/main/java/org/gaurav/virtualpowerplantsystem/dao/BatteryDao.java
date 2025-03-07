package org.gaurav.virtualpowerplantsystem.dao;

import org.gaurav.virtualpowerplantsystem.model.entity.Battery;

import java.util.List;

public interface BatteryDao {

    List<Battery> batchInsert(List<Battery> batteries, int batchSize);
}
