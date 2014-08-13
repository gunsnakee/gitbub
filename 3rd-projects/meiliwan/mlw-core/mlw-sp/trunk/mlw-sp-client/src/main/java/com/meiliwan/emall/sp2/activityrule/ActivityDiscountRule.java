package com.meiliwan.emall.sp2.activityrule;


import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.sp2.activityrule.base.ActivitySingleRule;
import com.meiliwan.emall.sp2.bean.ActivityProductBean;
import com.meiliwan.emall.sp2.bean.view.SimpleActProVO;

import java.util.HashMap;
import java.util.Map;

/**
 *   折扣规则处理类
 */

public class ActivityDiscountRule extends ActivitySingleRule {
    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 计算单品价格
     * 1、设置单品活动价
     * 2、计算当前单品活动总价： 单品购买数量 * 活动价
     * 3、设置是否参与价格计算为：true
     * @param proVO
     * @return
     */
    @Override
    public SimpleActProVO calculate(SimpleActProVO proVO) {
       ActivityProductBean activityProductBean = (ActivityProductBean) this.getActRuleParam();
       if(proVO !=null && activityProductBean != null){
           try{
               SimpleProduct sPro = proVO.getSimpleProduct().getProduct();
               proVO.setRealPrice(activityProductBean.getActPrice().doubleValue());
               proVO.setCurProductTotalPrice(NumberUtil.multiply(activityProductBean.getActPrice().doubleValue(), proVO.getBuyCount(), 2));
               proVO.setIsJoinCalculatePrice(true);
               proVO.setDiscount(activityProductBean.getDiscount());
               proVO.setDiscounts(activityProductBean.getDiscounts());
           }catch (Exception e){
               Map map = new HashMap();
               map.put("desc","折扣规则处理类-->计算单品价格");
               map.put("proVO",proVO);
               map.put("activityProductBean",activityProductBean);
               logger.error(e,map,null);
           }
           return proVO;
       }
        return null;
    }


    @Override
    public String toString() {
        return "ActivityDiscountRule{" +
                "ruleBean=" + ruleBean +
                "， actRuleParam=" + actRuleParam +
                '}';
    }
}