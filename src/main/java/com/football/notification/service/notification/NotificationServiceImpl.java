package com.football.notification.service.notification;

import com.football.common.constant.Constant;
import com.football.common.exception.CommonException;
import com.football.common.message.MessageCommon;
import com.football.common.model.device.Device;
import com.football.common.model.notification.Notification;
import com.football.common.model.notification.NotificationQueue;
import com.football.common.response.Response;
import com.football.common.util.ArrayListCommon;
import com.football.common.util.Resource;
import com.football.notification.repository.DeviceRepository;
import com.football.notification.repository.NotificationLogRepository;
import com.football.notification.repository.NotificationQueueRepository;
import com.football.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : truongnq
 * @Date time: 2018-12-19 21:47
 * To change this template use File | Settings | File Templates.
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    NotificationQueueRepository notificationQueueRepository;

    @Autowired
    NotificationLogRepository notificationLogRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Override
    public Response createNotification(String title, String content, int type, long userId) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setContents(content);
        notification.setType(type);
        notification.setStatus(Constant.STATUS_OBJECT.ACTIVE);
        notification = notificationRepository.save(notification);

        List<Device> deviceList = deviceRepository.findByUserIdAndStatus(userId, Constant.DEVICE.STATUS.ONLINE);
        if (!ArrayListCommon.isNullOrEmpty(deviceList))
            for (Device device : deviceList
            ) {
                NotificationQueue notificationQueue = new NotificationQueue();
                notificationQueue.setNotificationId(notification.getId());
                notificationQueue.setDeviceId(device.getId());
                notificationQueue.setStatus(Constant.NOTIFICATION_QUEUE.STATUS.NEW);
                notificationQueueRepository.save(notificationQueue);
            }
        else {
            Device device = deviceRepository.findFirstByUserId(userId);
            if (device != null) {
                NotificationQueue notificationQueue = new NotificationQueue();
                notificationQueue.setNotificationId(notification.getId());
                notificationQueue.setDeviceId(device.getId());
                notificationQueue.setStatus(Constant.NOTIFICATION_QUEUE.STATUS.WAIT);
                notificationQueueRepository.save(notificationQueue);
            } else {
                throw new CommonException(Response.NOT_FOUND, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND), Constant.TABLE.DEVICE));
            }
        }
        return Response.OK;
    }
}
