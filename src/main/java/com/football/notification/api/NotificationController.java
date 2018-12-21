package com.football.notification.api;

import com.football.common.constant.Constant;
import com.football.common.exception.CommonException;
import com.football.common.response.Response;
import com.football.notification.service.notification.NotificationService;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: truongnq
 * @date: 21-Dec-18 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping(value = "notification")
public class NotificationController {
    private static final Logger LOGGER = LogManager.getLogger(Constant.LOG_APPENDER.CATEGORY);

    @Autowired
    NotificationService notificationService;

    @RequestMapping(method = POST)
    @ResponseBody
    @ApiOperation(value = "API gui notification", notes = "Neu nguoi nhan khong online se duoc giu lai notification va gui sau")
    public ResponseEntity<?> create(
            @RequestParam(value = "title", required = true, defaultValue = "Tieu de thong bao") String title,
            @RequestParam(value = "content", required = true, defaultValue = "Noi dung thong bao") String content,
            @RequestParam(value = "type", required = true, defaultValue = "1") int type,
            @RequestParam(value = "userId", required = true, defaultValue = "1") long userId
    ) throws Exception {
        try {
            return new ResponseEntity<Response>(notificationService.createNotification(title, content, type, userId), HttpStatus.CREATED);
        } catch (CommonException e) {
            return new ResponseEntity<>(e.toString(), e.getResponse().getStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
