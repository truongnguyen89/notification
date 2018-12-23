package com.football.notification.service.init;

import com.football.common.cache.Cache;
import com.football.common.constant.Constant;
import com.football.common.model.param.Param;
import com.football.common.util.ArrayListCommon;
import com.football.notification.service.BaseService;
import com.football.notification.service.param.ParamService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 19-Jun-18
 * Time: 11:32 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class InitServiceImpl extends BaseService implements InitService {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.APPLICATION);

    @Autowired
    ParamService paramService;

    @Override
    public void initCache() throws Exception {
        long id = System.currentTimeMillis();
        LOGGER.info("[B]---------------------------------INIT---------------------------------");
        //Load param cache
        List<Param> paramList = paramService.findByStatus(Constant.STATUS_OBJECT.ACTIVE);
        if (ArrayListCommon.isNullOrEmpty(paramList))
            LOGGER.error("Param active not found");
        else
            LOGGER.info("Response when load param cache : " + Cache.setParamCache(paramList).toString());
        LOGGER.info("[E][Duration = " + (System.currentTimeMillis() - id) + "]---------------------------------INIT---------------------------------");
    }
}
