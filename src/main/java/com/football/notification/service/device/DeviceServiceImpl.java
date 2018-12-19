package com.football.notification.service.device;

import com.football.common.model.device.Device;
import com.football.notification.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DeviceServiceImpl implements DeivceService {
    @Autowired
    DeviceRepository deviceRepository;

    @Override
    public Device subscribe(Device device) throws Exception {
        return deviceRepository.save(device);
    }

    @Override
    public Device update(long id, Device device) throws Exception {
        device.setId(id);
        return deviceRepository.save(device);
    }

    @Override
    public Device findById(long id) throws Exception {
        return deviceRepository.findOne(id);
    }

    @Override
    public List<Device> findByStatus(int status) throws Exception {
        return deviceRepository.findByStatus(status);
    }
}
