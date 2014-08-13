package com.meiliwan.emall.sp2.activityrule.base;

import com.meiliwan.emall.sp2.bean.view.ActVO;

/**
 * Created by Sean on 13-12-24.
 *
 */
public abstract class ActivityMultiRule extends ActivityRule {

    public abstract ActVO calculate(ActVO actVO);
}
