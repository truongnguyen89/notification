package com.football.notification.service.notification;

import com.football.common.constant.Constant;
import com.football.common.exception.CommonException;
import com.football.common.message.MessageCommon;
import com.football.common.model.device.Device;
import com.football.common.model.email.Email;
import com.football.common.model.notification.Notification;
import com.football.common.model.notification.NotificationQueue;
import com.football.common.model.user.User;
import com.football.common.response.Response;
import com.football.common.util.ArrayListCommon;
import com.football.common.util.Resource;
import com.football.common.util.StringCommon;
import com.football.common.repository.*;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailRepository emailRepository;

    @Override
    public NotificationQueue createNotification(String title, String content, int type, long userId) throws Exception{
        NotificationQueue rs = new NotificationQueue();
        //Validate user
        User user = userRepository.findOne(userId);
        if (user == null)
            throw new CommonException(Response.NOT_FOUND, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND), Constant.TABLE.USER));
        //Luu thong tin noti
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setContents(content);
        notification.setType(type);
        notification.setStatus(Constant.STATUS_OBJECT.ACTIVE);
        notification = notificationRepository.save(notification);

        List<Device> deviceList = deviceRepository.findByUserIdAndStatus(userId, Constant.DEVICE.STATUS.ONLINE);
        //Neu co device online
        if (!ArrayListCommon.isNullOrEmpty(deviceList))
            for (Device device : deviceList
            ) {
                NotificationQueue notificationQueue = new NotificationQueue();
                notificationQueue.setNotificationId(notification.getId());
                notificationQueue.setDeviceId(device.getId());
                notificationQueue.setStatus(Constant.NOTIFICATION_QUEUE.STATUS.NEW);
                rs = notificationQueueRepository.save(notificationQueue);
            }
            //Neu khong co device online
        else {
            Device device = deviceRepository.findFirstByUserId(userId);
            //Da co device tung dang nhap nhung dang offline --> De noti dang cho doi, khi online se gui di
            if (device != null) {
                NotificationQueue notificationQueue = new NotificationQueue();
                notificationQueue.setNotificationId(notification.getId());
                notificationQueue.setDeviceId(device.getId());
                notificationQueue.setStatus(Constant.NOTIFICATION_QUEUE.STATUS.WAIT);
                rs = notificationQueueRepository.save(notificationQueue);
            } else {
                throw new CommonException(Response.NOT_FOUND, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND), Constant.TABLE.DEVICE));
            }
        }

        //Send email neu user co dung email
        if (!StringCommon.isNullOrBlank(user.getEmail()))
            emailRepository.save(new Email(user.getEmail(), title, content));
        return rs;
    }
}
