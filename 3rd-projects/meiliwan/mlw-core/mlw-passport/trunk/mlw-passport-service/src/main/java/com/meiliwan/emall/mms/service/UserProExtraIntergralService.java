package com.meiliwan.emall.mms.service;


import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.mms.bean.UserCategoryIntegralRule;
import com.meiliwan.emall.mms.bean.UserProExtraIntegral;
import com.meiliwan.emall.mms.dao.UserCategoryIntegralRuleDao;
import com.meiliwan.emall.mms.dao.UserProExtraIntegralDao;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 商品额外赠分 service
 *
 * @author yiyou.luo
 *         2013-06-09
 */
 @Service
public class UserProExtraIntergralService extends DefaultBaseServiceImpl {
    private static final MLWLogger logger = MLWLoggerFactory.getLogger(UserProExtraIntergralService.class);
    @Autowired
    private UserProExtraIntegralDao userProExtraIntegralDao;

    /**
     * 通过id获取商品额外赠分实例
     * @param resultObj
     * @param id
     */
    @IceServiceMethod
    public void getUserProExtraIntegralById(JsonObject resultObj, String id) {
        if (id != null && id.matches("^[0-9]*$")) { //过滤空和非数字
            addToResult(userProExtraIntegralDao.getEntityById(Integer.parseInt(id)) ,resultObj);
        }
    }

    /**
     * 通过商品id获取商品额外赠分实例
     * @param resultObj
     * @param proId
     */
    @IceServiceMethod
    public void getUserProExtraIntegralByProId(JsonObject resultObj, Integer proId) {
        if (proId > 0) { //过滤空和非数字
            UserProExtraIntegral userProExtraIntegral = new UserProExtraIntegral();
            userProExtraIntegral.setProId(proId);
            userProExtraIntegral.setIsDel((int)GlobalNames.STATE_VALID);
            addToResult(userProExtraIntegralDao.getListByObj(userProExtraIntegral, ""), resultObj);
        }
    }

    /**
     * 添加商品额外赠分
     *
     * @param userProExtraIntegral
     * @param resultObj
     */
    @IceServiceMethod
    public void saveUserProExtraIntegral(JsonObject resultObj,UserProExtraIntegral userProExtraIntegral) {
        try{
            if (userProExtraIntegral != null) {
                userProExtraIntegral.setCreateTime(new Date());
                userProExtraIntegralDao.insert(userProExtraIntegral) ;
                addToResult(userProExtraIntegral.getId(),resultObj);
            }
        }catch(Exception e){
            throw new ServiceException("UserProExtraIntergralService-saveUserProExtraIntegral: {}", userProExtraIntegral.toString(), e);
        }
    }

    /**
     * 修改商品额外赠分
     *
     * @param userProExtraIntegral
     * @param resultObj
     */
    @IceServiceMethod
    public void updateUserProExtraIntegral(JsonObject resultObj,UserProExtraIntegral userProExtraIntegral) {
        try{
            if (userProExtraIntegral != null) {
                addToResult(userProExtraIntegralDao.update(userProExtraIntegral), resultObj);
            }
        }catch(Exception e){
            throw new ServiceException("UserProExtraIntergralService-updateUserProExtraIntegral: {}", userProExtraIntegral.toString(), e);
        }
    }

    public UserProExtraIntegral getExtraIntegralByProId(Integer proId) {
        UserProExtraIntegral userProExtraIntegral = new UserProExtraIntegral();
        UserProExtraIntegral queryDto = new UserProExtraIntegral();
        queryDto.setProId(proId);
        queryDto.setIsDel((int)GlobalNames.STATE_VALID);
        List<UserProExtraIntegral> list = userProExtraIntegralDao.getListByObj(queryDto, "",true);
        if(list != null && list.size()>0){
            userProExtraIntegral = list.get(0);
        }
        return userProExtraIntegral;
    }
}
