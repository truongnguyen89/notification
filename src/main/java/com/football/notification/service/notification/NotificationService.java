package com.football.notification.service.notification;

import com.football.common.model.notification.NotificationQueue;
import com.football.common.response.Response;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : truongnq
 * @Date time: 2018-12-19 21:45
 * To change this template use File | Settings | File Templates.
 */
@Service
public interface NotificationService {
    Response createNotification(String title, String content, int type, long userId);
}
