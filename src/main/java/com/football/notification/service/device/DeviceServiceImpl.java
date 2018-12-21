package com.football.notification.service.device;

import com.football.common.constant.Constant;
import com.football.common.model.device.Device;
import com.football.common.model.notification.NotificationQueue;
import com.football.notification.repository.DeviceRepository;
import com.football.notification.repository.NotificationQueueRepository;
import com.football.notification.service.notification.NotificationService;
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
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    NotificationQueueRepository notificationQueueRepository;

    @Autowired
    NotificationService notificationService;

    @Override
    public Device subscribe(Device device) throws Exception {
        //Neu la dang ky thi huy dang ky o cac thiet bi khac
        if (device.getStatus() == Constant.STATUS_OBJECT.ACTIVE)
            unSubscribeOther(device.getUserId(), device.getOnesignalDeviceId());
        //Check devide da ton tai
        Device deviceOld = deviceRepository.findFirstByUserIdAndOnesignalAppIdAndOnesignalDeviceId(
                device.getUserId(), device.getOnesignalAppId(), device.getOnesignalDeviceId());
        if (deviceOld == null) {
            device = deviceRepository.save(device);
            createNotificationWait(device.getId(), device.getUserId());
            return device;
        } else {
            deviceOld.setStatus(device.getStatus());
            deviceOld = deviceRepository.save(deviceOld);
            createNotificationWait(deviceOld.getId(), deviceOld.getUserId());
            return deviceOld;
        }
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

    public void unSubscribeOther(long userId, String onesignalDeviceId) throws Exception {
        Iterable<Device> devices = deviceRepository.findFirstByUserIdAndStatusAndOnesignalDeviceIdIsNotIn(userId, Constant.STATUS_OBJECT.ACTIVE, onesignalDeviceId);
        devices.forEach(device -> {
            device.setStatus(Constant.STATUS_OBJECT.INACTIVE);
            notificationService.createNotification("Thong bao login thiet bi khac",
                    "Tai khoan cua ban da duoc login tren thiet bi co id = " + onesignalDeviceId,
                    1,
                    userId);
        });
        deviceRepository.save(devices);
    }

    public void createNotificationWait(long deviceId, long userId) {
        Iterable<NotificationQueue> notificationQueues = notificationQueueRepository.findNotificationWait(userId, Constant.NOTIFICATION_QUEUE.STATUS.WAIT);
        notificationQueues.forEach(notificationQueue -> {
            notificationQueue.setDeviceId(deviceId);
            notificationQueue.setStatus(Constant.NOTIFICATION_QUEUE.STATUS.NEW);
        });
        notificationQueueRepository.save(notificationQueues);
    }
}
