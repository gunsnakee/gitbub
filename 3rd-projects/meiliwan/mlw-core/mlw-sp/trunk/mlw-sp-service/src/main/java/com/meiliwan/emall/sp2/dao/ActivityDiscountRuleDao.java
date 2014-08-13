package com.meiliwan.emall.sp2.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.sp2.bean.ActivityDiscountRuleBean;

public interface ActivityDiscountRuleDao extends IDao<Integer, ActivityDiscountRuleBean> {
    /**
     *  通过进活动id 删除 活动规则
     * @param actId
     * @return
     */
    public Integer deleteByActId(Integer actId);

}