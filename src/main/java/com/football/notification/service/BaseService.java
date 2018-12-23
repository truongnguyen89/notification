package com.football.notification.service;

import com.football.common.constant.Constant;
import com.football.common.exception.CommonException;
import com.football.common.message.MessageCommon;
import com.football.common.response.Response;
import com.football.common.util.Resource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 04-Dec-18
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseService {
    // validate the input
    protected ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    private Validator getValidator() {
        return validator;
    }

    public void validate(Object o) {
        if (o == null)
            throw new CommonException(Response.NOT_FOUND, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND), o.getClass().getName()));
        getValidator().validate(o).stream().findAny().ifPresent(t -> {
            throw new CommonException(Response.NOT_FOUND, t.getMessage());
        });
    }
}
