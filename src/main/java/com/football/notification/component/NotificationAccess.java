package com.football.notification.component;

import com.football.common.cache.Cache;
import com.football.common.constant.Constant;
import com.football.common.message.MessageCommon;
import com.football.common.model.device.Device;
import com.football.common.model.notification.NotificationQueue;
import com.football.notification.repository.DeviceRepository;
import com.football.notification.repository.NotificationQueueRepository;
import com.football.notification.service.notification.NotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NotificationAccess {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.DB);

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    NotificationQueueRepository notificationQueueRepository;

    @Autowired
    NotificationService notificationService;

    ExecutorService executorService;
    private int numThreads = 10;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(numThreads);
    }

    /**
     * Khi user online vao 1 thiet bi, tat lang nghe nhan thong bao cua cac thiet bi khac da login truoc do
     * @param userId
     * @param onesignalDeviceId
     */
    public void unSubscribeOther(long userId, String onesignalDeviceId) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Iterable<Device> devices = deviceRepository.findFirstByUserIdAndStatusAndOnesignalDeviceIdIsNotIn(userId, Constant.STATUS_OBJECT.ACTIVE, onesignalDeviceId);
                    devices.forEach(device -> {
                        device.setStatus(Constant.STATUS_OBJECT.INACTIVE);
                        try {
                            String title = Cache.getValueParamFromCache(Constant.PARAMS.TYPE.NOTIFICATION_UNSUBSCRIBE_OTHER, Constant.PARAMS.CODE.TITLE);
                            String content = Cache.getValueParamFromCache(Constant.PARAMS.TYPE.NOTIFICATION_UNSUBSCRIBE_OTHER, Constant.PARAMS.CODE.CONTENT);
                            content = MessageCommon.getMessage(content, onesignalDeviceId);
                            notificationService.createNotification(title,
                                    content,
                                    Constant.NOTIFICATION.TYPE.NORMAL,
                                    userId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    deviceRepository.save(devices);
                } catch (Exception e) {
                    LOGGER.error("Exception unSubscribeOther ", e);
                }
            }
        });
    }

    /**
     *
     * @param deviceId
     * @param userId
     * Tao thong bao dung cho khi chua co user online
     */
    public void createNotificationWait(long deviceId, long userId) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Iterable<NotificationQueue> notificationQueues = notificationQueueRepository.findNotificationWait(userId, Constant.NOTIFICATION_QUEUE.STATUS.WAIT);
                    notificationQueues.forEach(notificationQueue -> {
                        notificationQueue.setDeviceId(deviceId);
                        notificationQueue.setStatus(Constant.NOTIFICATION_QUEUE.STATUS.NEW);
                    });
                    notificationQueueRepository.save(notificationQueues);
                } catch (Exception e) {
                    LOGGER.error("Exception createNotificationWait ", e);
                }
            }
        });
    }
}
