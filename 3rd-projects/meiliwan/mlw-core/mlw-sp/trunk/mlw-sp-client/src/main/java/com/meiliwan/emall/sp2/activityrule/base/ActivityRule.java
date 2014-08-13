package com.meiliwan.emall.sp2.activityrule.base;

import com.meiliwan.emall.sp2.bean.base.ActivityRuleBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou。luo
 * Date: 13-12-24
 * Time: 上午11:45
 * To change this template use File | Settings | File Templates.
 */
//活动规则基类
public abstract class ActivityRule {

    /**
     * 活动规则计算参数
     *用参与规则计算
     */
    protected ActivityRuleParam actRuleParam;

    public ActivityRuleParam getActRuleParam() {
        return actRuleParam;
    }

    public void setActRuleParam(ActivityRuleParam actRuleParam) {
        this.actRuleParam = actRuleParam;
    }


}
