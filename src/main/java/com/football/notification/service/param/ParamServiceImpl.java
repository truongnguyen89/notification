package com.football.notification.service.param;

import com.football.notification.repository.ParamRepository;
import com.football.notification.service.BaseService;
import com.football.common.constant.Constant;
import com.football.common.database.ConnectionCommon;
import com.football.common.exception.CommonException;
import com.football.common.message.MessageCommon;
import com.football.common.model.param.Param;
import com.football.common.model.param.ParamKey;
import com.football.common.response.Response;
import com.football.common.util.Resource;
import com.football.common.util.StringCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 17-May-18
 * Time: 12:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ParamServiceImpl extends BaseService implements ParamService {
    @Autowired
    ParamRepository paramRepository;

    @Autowired
    DataSource dataSource;

    @Override
    public Param create(Param param) throws Exception {
        if (param == null
                || param.getParamKey() == null
                || StringCommon.isNullOrBlank(param.getParamKey().getType())
                || StringCommon.isNullOrBlank(param.getParamKey().getCode())
                || StringCommon.isNullOrBlank(param.getValue())
        )
            throw new CommonException(Response.BAD_REQUEST, MessageCommon.getMessage(Resource.getMessageResoudrce("param1.is.null"), Constant.TABLE.PARAM));
        else {
            //Check param exist
            Param oldParam = findById(param.getParamKey().getType(), param.getParamKey().getCode());
            if (oldParam != null)
                throw new CommonException(Response.OBJECT_IS_EXIST, MessageCommon.getMessage(Resource.getMessageResoudrce("param1.is.exists"), Constant.TABLE.PARAM));
            else
                return paramRepository.save(param);
        }
    }

    @Override
    public Param findById(String type, String code) throws Exception {
        return paramRepository.findOne(new ParamKey(type, code));
    }

    @Override
    public List<Param> findByStatus(int status) throws Exception {
        return paramRepository.findByStatus(status);
    }

    @Override
    public Iterable<Param> findAll() throws Exception {
        return paramRepository.findAll();
    }

    @Override
    public Param update(Param param) throws Exception {
        if (param == null
                || param.getParamKey() == null
                || StringCommon.isNullOrBlank(param.getParamKey().getType())
                || StringCommon.isNullOrBlank(param.getParamKey().getCode())
        )
            throw new CommonException(Response.BAD_REQUEST, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.IS_NULL), Constant.TABLE.PARAM));
        else {
            Param paramOld = paramRepository.findOne(param.getParamKey());
            if (paramOld == null)
                throw new CommonException(Response.NOT_FOUND, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND), Constant.TABLE.PARAM));
            else {
                paramOld.setValue(!StringCommon.isNullOrBlank(param.getValue()) ? param.getValue() : paramOld.getValue());
                paramOld.setName(!StringCommon.isNullOrBlank(param.getName()) ? param.getName() : paramOld.getName());
                paramOld.setStatus(param.getStatus());
                return paramRepository.save(paramOld);
            }
        }
    }

    public int countByTypeCode(String type, String code) throws Exception {
        Connection connection = dataSource.getConnection();
        CallableStatement cs = null;
        try {
            cs = connection.prepareCall("{call pro_test (?, ?, ?)}");
            ConnectionCommon.doSetStringParams(cs, 1, type);
            ConnectionCommon.doSetStringParams(cs, 2, code);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.execute();
            return cs.getInt(3);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            ConnectionCommon.close(cs);
            ConnectionCommon.close(connection);
        }
    }
}
