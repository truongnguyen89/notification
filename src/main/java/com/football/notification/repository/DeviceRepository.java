package com.football.notification.repository;

import com.football.common.model.device.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: truongnq
 * Date: 2018-12-19
 * Time: 21:29
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {
    List<Device> findByStatus(int status);
}
