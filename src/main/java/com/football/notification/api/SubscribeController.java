package com.football.notification.api;

import com.football.common.constant.Constant;
import com.football.common.exception.CommonException;
import com.football.common.model.device.Device;
import com.football.notification.service.device.DeviceService;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: truongnq
 * @date: 21-Dec-18 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "subscribe")
public class SubscribeController {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.CATEGORY);

    @Autowired
    DeviceService deviceService;

    @RequestMapping(method = POST)
    @ResponseBody
    @ApiOperation(value = "API subscribe/un-subscribe", notes = "API dang ky online/offline de server biet thiet bi nao se nhan notification")
    public ResponseEntity<?> subscribe(@Valid @RequestBody Device device
    ) throws Exception {
        try {
            return new ResponseEntity<Device>(deviceService.subscribe(device), HttpStatus.CREATED);
        } catch (CommonException e) {
            return new ResponseEntity<>(e.toString(), e.getResponse().getStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
