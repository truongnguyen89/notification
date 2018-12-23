package com.football.notification.service.init;

import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 19-Jun-18
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public interface InitService {
    void initCache() throws Exception;
}
