package com.meiliwan.emall.sp2.activityrule.base;
import com.meiliwan.emall.sp2.activityrule.base.ActivityRule;
import com.meiliwan.emall.sp2.bean.base.ActivityRuleBean;
import com.meiliwan.emall.sp2.bean.view.SimpleActProVO;

import java.util.List;

/**
 * Created by Sean on 13-12-24.
 */
public abstract class ActivitySingleRule extends ActivityRule {

    protected ActivityRuleBean ruleBean;

    public abstract SimpleActProVO calculate(SimpleActProVO proVO);

    public ActivityRuleBean getRuleBean() {
        return ruleBean;
    }

    public void setRuleBean(ActivityRuleBean ruleBean) {
        this.ruleBean = ruleBean;
    }
}
