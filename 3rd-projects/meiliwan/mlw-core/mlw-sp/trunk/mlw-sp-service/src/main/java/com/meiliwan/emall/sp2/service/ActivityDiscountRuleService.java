
package com.meiliwan.emall.sp2.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.service.BaseService;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import com.meiliwan.emall.sp2.bean.ActivityDiscountRuleBean;
import com.meiliwan.emall.sp2.bean.view.UserProVO;
import com.meiliwan.emall.sp2.dao.ActivityDiscountRuleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 折扣规则 server
 *
 * @author yiyou.luo
 *         2013-12-24
*/
@Service
public class ActivityDiscountRuleService extends DefaultBaseServiceImpl implements
        BaseService{
    @Autowired
    private ActivityDiscountRuleDao activityDiscountRuleDao;

    public void getCashierCart(JsonObject resultObj, List<UserProVO> userProVOList) {

    }

    /**
     * 根据活动ID获得活动对象
     *
     * @param actId
     * @param resultObj
     */
    @IceServiceMethod
    public void getSpActivityDiscountRuleById(JsonObject resultObj, int actId) {
            ActivityDiscountRuleBean entry = activityDiscountRuleDao.getEntityById(actId);
            addToResult(entry, resultObj);
    }

    /**
     * 添加活动
     *
     * @param entry
     * @param resultObj
     */
    @IceServiceMethod
    public void saveSpActivityDiscountRule(JsonObject resultObj, ActivityDiscountRuleBean entry) {
        if (entry != null) {
            entry.setCreateTime(new Date());
            activityDiscountRuleDao.insert(entry);
            addToResult(entry.getActId()>0?entry.getActId():-1, resultObj);
        }else {
            addToResult(-1, resultObj);
        }

    }

    /**
     * 修改活动
     *
     * @param entry
     * @param resultObj
     */
    @IceServiceMethod
    public void updateSpActivityDiscountRule(JsonObject resultObj,ActivityDiscountRuleBean entry) {
        if (entry != null) {
            addToResult(activityDiscountRuleDao.update(entry)>0?true:false, resultObj);
        }
    }

    /**
     * 根据活动id 获取活动规则list
     * @param resultObj
     * @param actId
     */
    public void getDiscountRuleByActId(JsonObject resultObj,Integer actId){
        ActivityDiscountRuleBean entry = new ActivityDiscountRuleBean();
        entry.setActId(actId);
        List<ActivityDiscountRuleBean> list = activityDiscountRuleDao.getListByObj(entry);
        addToResult(list, resultObj);
    }

}




