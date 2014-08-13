package com.meiliwan.emall.sp2.bean.view;

import com.meiliwan.emall.sp2.activityrule.base.ActivityRule;
import com.meiliwan.emall.sp2.constant.PrivilegeType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Sean on 13-12-24.
 */
public class ActVO extends SimpleActVO implements Serializable {

    private static final long serialVersionUID = -434212676203272445L;

    public ActVO(SimpleActVO simpleActVO) {
        super(simpleActVO.actRule, simpleActVO.actId, simpleActVO.actName, simpleActVO.actType, simpleActVO.actState, simpleActVO.actDesc, simpleActVO.startTime, simpleActVO.endTime, simpleActVO.createTime);
    }

    public ActVO() {
        super();
    }

    /**
     * 当前活动的商品
     */
    private List<SimpleActProVO> actProVOList;

    private PrivilegeType privilegeType;  //活动的优惠类型
    private double actRealPayAmount;//  实际总价= Pro_total_price - Youhui_price
    private double actSaveAmount;// 优惠价格
    private double actProAmount;// 商品美丽 价总价
    private Date objCreateTime;// 计算创建时间

    @Override
    public String toString() {
        return "{" +
                "actProVOList=" + actProVOList +
                ", actRealPayAmount=" + actRealPayAmount +
                ", actSaveAmount=" + actSaveAmount +
                ", actProAmount=" + actProAmount +
                ", objCreateTime=" + objCreateTime +
                '}';
    }

    public List<SimpleActProVO> getActProVOList() {
        return actProVOList;
    }

    public void setActProVOList(List<SimpleActProVO> actProVOList) {
        this.actProVOList = actProVOList;
    }

    public PrivilegeType getPrivilegeType() {
        return privilegeType;
    }

    public void setPrivilegeType(PrivilegeType privilegeType) {
        this.privilegeType = privilegeType;
    }

    public double getActRealPayAmount() {
        return actRealPayAmount;
    }

    public void setActRealPayAmount(double actRealPayAmount) {
        this.actRealPayAmount = actRealPayAmount;
    }

    public double getActSaveAmount() {
        return actSaveAmount;
    }

    public void setActSaveAmount(double actSaveAmount) {
        this.actSaveAmount = actSaveAmount;
    }

    public double getActProAmount() {
        return actProAmount;
    }

    public void setActProAmount(double actProAmount) {
        this.actProAmount = actProAmount;
    }

    public Date getObjCreateTime() {
        return objCreateTime;
    }

    public void setObjCreateTime(Date objCreateTime) {
        this.objCreateTime = objCreateTime;
    }
}
