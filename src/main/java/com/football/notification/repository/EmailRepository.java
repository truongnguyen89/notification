package com.football.notification.repository;

import com.football.common.model.email.Email;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : truongnq
 * @Date time: 2018-12-19 23:30
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface EmailRepository extends CrudRepository<Email, Long> {
}
