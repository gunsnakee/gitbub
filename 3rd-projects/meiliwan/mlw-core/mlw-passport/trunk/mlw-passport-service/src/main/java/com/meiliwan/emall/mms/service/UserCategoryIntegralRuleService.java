package com.meiliwan.emall.mms.service;


import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.mms.bean.UserCategoryIntegralRule;
import com.meiliwan.emall.mms.dao.UserCategoryIntegralRuleDao;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 类目积分规则 service
 *
 * @author yiyou.luo
 *         2013-06-04
 */
@Service
public class UserCategoryIntegralRuleService extends DefaultBaseServiceImpl {
    private static final MLWLogger logger = MLWLoggerFactory.getLogger(UserProExtraIntergralService.class);
    @Autowired
    private UserCategoryIntegralRuleDao userCategoryIntegralRuleDao;

    /**
     * 通过id获取类目规则实例
     *
     * @param resultObj
     * @param id
     */
    @IceServiceMethod
    public void getCategoryIntegralRuleById(JsonObject resultObj, String id) {
        if (id != null && id.matches("^[0-9]*$")) { //过滤空和非数字
            addToResult(userCategoryIntegralRuleDao.getEntityById(Integer.parseInt(id)), resultObj);
        }
    }

    /**
     * 通过类目id获取类目规则实例
     *
     * @param resultObj
     * @param categoryId
     */
    @IceServiceMethod
    public void getCategoryIntegralRuleByCategoryId(JsonObject resultObj, String categoryId) {
        if (categoryId != null && categoryId.matches("^[0-9]*$")) { //过滤空和非数字
            UserCategoryIntegralRule userCategoryIntegralRule = new UserCategoryIntegralRule();
            userCategoryIntegralRule.setCategoryId(Integer.parseInt(categoryId));
            addToResult(userCategoryIntegralRuleDao.getListByObj(userCategoryIntegralRule, ""), resultObj);
        }
    }

    /**
     * 添加类目积分规则
     *
     * @param uCIRule
     * @param resultObj
     */
    @IceServiceMethod
    public void saveCategoryIntegralRule(JsonObject resultObj, UserCategoryIntegralRule uCIRule) {
        if (uCIRule != null) {
            uCIRule.setIsDel((int)GlobalNames.STATE_VALID);
            uCIRule.setCreateTime(new Date());
            uCIRule.setUpdateTime(new Date());
            userCategoryIntegralRuleDao.insert(uCIRule);
            addToResult(uCIRule.getId(), resultObj);
        }
    }

    /**
     * 修改类目积分规则
     *
     * @param uCIRule
     * @param resultObj
     */
    @IceServiceMethod
    public void updateUserCategoryIntegralRule(JsonObject resultObj, UserCategoryIntegralRule uCIRule) {
        try{
            if (uCIRule != null) {
                uCIRule.setSecondCategoryId(null);
                uCIRule.setIsDel((int) GlobalNames.STATE_VALID);
                uCIRule.setUpdateTime(new Date());
                addToResult(userCategoryIntegralRuleDao.update(uCIRule) > 0 ? true : false, resultObj);
            }  else{
                addToResult(false, resultObj);
            }
        }catch (Exception e){
            throw new ServiceException("UserCategoryIntegralRuleService-updateUserCategoryIntegralRule: {}", uCIRule.toString(), e);
        }

    }

    /**
     * 通过类目积分规则 实体参数获取对应的实体列表包含物理分页
     *
     * @param pageInfo
     * @param resultObj
     */
    @IceServiceMethod
    public void getUserCategoryIntegralRulePaperByObj(JsonObject resultObj, UserCategoryIntegralRule userCategoryIntegralRule, PageInfo pageInfo) {
        if (userCategoryIntegralRule != null && pageInfo != null) {
            userCategoryIntegralRule.setIsDel((int) GlobalNames.STATE_VALID);
            addToResult(userCategoryIntegralRuleDao.getPagerByObj(userCategoryIntegralRule, pageInfo, "", " order by create_time  "), resultObj);
        }
    }

    /**
     * 获取 类目积分规则
     *  根据 商品id获取启用类目积分规则，如果没有则返回默认类目积分规则
     * @param categoryId
     * @return
     */
    public UserCategoryIntegralRule getEntityByCategoryId(Integer categoryId) {
        UserCategoryIntegralRule userCategoryIntegralRule = new UserCategoryIntegralRule();
        userCategoryIntegralRule.setCategoryId(categoryId);
        userCategoryIntegralRule.setState(GlobalNames.CATEGORY_INTEGRAL_RULE_STATE_STRAT);
        List<UserCategoryIntegralRule> list = userCategoryIntegralRuleDao.getListByObj(userCategoryIntegralRule, "",true);

        if (list != null && list.size() > 0){
            return  list.get(0);
        } else{
            userCategoryIntegralRule.setCategoryId(0);
            list = userCategoryIntegralRuleDao.getListByObj(userCategoryIntegralRule, "",true);
            if(list != null && list.size()>0){
                return list.get(0);
            }
        }
        return null;

    }




    }
