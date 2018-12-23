package com.football.notification.conf;

import com.football.notification.service.init.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 06-Dec-18
 * Time: 2:00 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ApplicationConstruct {
    @Autowired
    InitService initService;

    @PostConstruct
    public void init() throws Exception {
        initService.initCache();
    }
}
