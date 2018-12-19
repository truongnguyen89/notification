package com.football.notification.service.device;

import com.football.common.model.device.Device;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: truongnq
 * Date: 2018-12-19
 * Time: 21:35
 * To change this template use File | Settings | File Templates.
 */
@Service
public interface DeivceService {
    Device subscribe(Device device) throws Exception;

    Device update(long id, Device device) throws Exception;

    Device findById(long id) throws Exception;

    List<Device> findByStatus(int status) throws Exception;
}
