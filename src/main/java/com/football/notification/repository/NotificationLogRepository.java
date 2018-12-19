package com.football.notification.repository;

import com.football.common.model.notification.NotificationLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: truongnq
 * Date: 2018-12-19
 * Time: 21:30
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface NotificationLogRepository extends CrudRepository<NotificationLog, Long> {
}
