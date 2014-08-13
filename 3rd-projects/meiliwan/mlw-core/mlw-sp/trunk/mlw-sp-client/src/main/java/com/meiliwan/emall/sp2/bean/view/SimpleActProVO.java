package com.meiliwan.emall.sp2.bean.view;

import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.dto.SimpleProIfStock;
import com.meiliwan.emall.sp2.activityrule.base.ActivityRule;
import com.meiliwan.emall.sp2.constant.PrivilegeType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Sean on 13-12-24.
 */
public class SimpleActProVO extends UserProVO implements Serializable,Comparable {

    private static final long serialVersionUID = 6559576173745630307L;
    /**
     * 商品对象
     */
    private SimpleProIfStock simpleProduct;
    /**
     * 活动规则集合， 非持久化
     */

    private transient SimpleActVO joinMultiAct = null;

    private transient SimpleActVO joinSingleAct = null;

    /**
     * 活动价格  / 或是 商品的价格 都是以这个价格为为准
     */
    private double realPrice;
    /**
     * 活动商品状态 / 或是商品的最终状态 （这个状态就是表示是否加入活动价格计算但中的依据）
     */
    private int proRealStatus;

    /**
     * 定义当前活动是否参与价格计算， 默认: false
     */
    private boolean isJoinCalculatePrice = false;

    /**
     * 小计 购买数量* 参加活动后的价格
     */
    private double curProductTotalPrice = 0d;

    /**
     * 小计 购买数量* 参加活动后的价格
     */
    private double curSaveTotalPrice = 0d;

    /**
     * 表示当前商品拥有的 单品优惠以及 多品优惠
     */
    private Map<PrivilegeType, List<SimpleActVO>> actDiscountGroup;
    private int joinSingleActId;
    private int joinMultiActId;

    /**
     * 商品限购数量
     * -1：不限制
     *  0：限购，已购买过限购数量
     *  1：限购，可购买1件
     */
    private int limitBuyNum = -1;

    private Integer discount;  //直降类型：折扣 如：八五折用 整数85表示

    private BigDecimal discounts;  //直降类型：折扣力度（折扣额）在购物车用于已优惠显示

    //预售商品发货时间
    private long saleTime = 0;

    public boolean isJoinCalculatePrice() {
        return isJoinCalculatePrice;
    }

    public void setJoinCalculatePrice(boolean isJoinCalculatePrice) {
        this.isJoinCalculatePrice = isJoinCalculatePrice;
    }

    public int getLimitBuyNum() {
        return limitBuyNum;
    }

    public void setLimitBuyNum(int limitBuyNum) {
        this.limitBuyNum = limitBuyNum;
    }

    public int getJoinSingleActId() {
        return joinSingleActId;
    }

    public void setJoinSingleActId(int joinSingleActId) {
        this.joinSingleActId = joinSingleActId;
    }

    public int getJoinMultiActId() {
        return joinMultiActId;
    }

    public void setJoinMultiActId(int joinMultiActId) {
        this.joinMultiActId = joinMultiActId;
    }

    @Override
    public String toString() {
        if(this.joinSingleActId>0 && this.joinMultiActId>0){
            return "{" +
                    "productId='" + this.getProductId() + '\'' +
                    ", joinSingleActId='" + joinSingleActId + '\'' +
                    ", joinMultiActId='" + joinMultiActId + '\'' +
                    ", realPrice='" + realPrice + '\'' +
                    ", proRealStatus='" + proRealStatus + '\'' +
                    ", curProductTotalPrice='" + curProductTotalPrice + '\'' +
                    ", curSaveTotalPrice='" + curSaveTotalPrice + '\'' +
                    ", createCartTime='" + createCartTime + '\'' +
                    ", buyCount='" + buyCount + '\'' +
                    "}";
        }
        else if(this.joinSingleActId>0){
            return "{" +
                    "productId='" + this.getProductId() + '\'' +
                    ", joinSingleActId='" + joinSingleActId + '\'' +
                    ", realPrice='" + realPrice + '\'' +
                    ", proRealStatus='" + proRealStatus + '\'' +
                    ", curProductTotalPrice='" + curProductTotalPrice + '\'' +
                    ", curSaveTotalPrice='" + curSaveTotalPrice + '\'' +
                    ", createCartTime='" + createCartTime + '\'' +
                    ", buyCount='" + buyCount + '\'' +
                    "}";
        }
        else if(this.joinMultiActId>0){
            return "{" +
                    "productId='" + this.getProductId() + '\'' +
                    ", joinMultiActId='" + joinMultiActId + '\'' +
                    ", realPrice='" + realPrice + '\'' +
                    ", proRealStatus='" + proRealStatus + '\'' +
                    ", curProductTotalPrice='" + curProductTotalPrice + '\'' +
                    ", curSaveTotalPrice='" + curSaveTotalPrice + '\'' +
                    ", createCartTime='" + createCartTime + '\'' +
                    ", buyCount='" + buyCount + '\'' +
                    "}";
        }else{
            return "{" +
                    "productId='" + this.getProductId() + '\'' +
                    ", realPrice='" + realPrice + '\'' +
                    ", proRealStatus='" + proRealStatus + '\'' +
                    ", curProductTotalPrice='" + curProductTotalPrice + '\'' +
                    ", curSaveTotalPrice='" + curSaveTotalPrice + '\'' +
                    ", createCartTime='" + createCartTime + '\'' +
                    ", buyCount='" + buyCount + '\'' +
                    "}";
        }

    }


    public SimpleActVO getJoinMultiAct() {
        return this.joinMultiAct;
    }

    public void setJoinMultiAct(SimpleActVO joinMultiAct) {
        this.joinMultiAct = joinMultiAct;
        if(joinMultiAct!=null) this.joinMultiActId = joinMultiAct.getActId();
    }

    public SimpleActVO getJoinSingleAct() {
        return this.joinSingleAct;
    }

    public void setJoinSingleAct(SimpleActVO joinSingleAct) {
        this.joinSingleAct = joinSingleAct;
        if(joinSingleAct!=null) this.joinSingleActId = joinSingleAct.getActId();
    }

    public boolean getIsJoinCalculatePrice() {
        return isJoinCalculatePrice;
    }

    public void setIsJoinCalculatePrice(boolean isJoinCalculatePrice) {
        this.isJoinCalculatePrice = isJoinCalculatePrice;
    }

    public SimpleActProVO() {
    }
    public SimpleActProVO(UserProVO userProVO) {
        super(userProVO.getProductId(), userProVO.getBuyCount(), userProVO.getSelectedSingleActId(), userProVO.getSelectedMultiActId(),userProVO.getUserIsSelected(),userProVO.getCreateCartTime());
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public int getProRealStatus() {
        return proRealStatus;
    }

    public void setProRealStatus(int proRealStatus) {
        this.proRealStatus = proRealStatus;
    }

    public double getCurProductTotalPrice() {
        return curProductTotalPrice;
    }

    public void setCurProductTotalPrice(double curProductTotalPrice) {
        this.curProductTotalPrice = curProductTotalPrice;
    }

    public double getCurSaveTotalPrice() {
        return curSaveTotalPrice;
    }

    public void setCurSaveTotalPrice(double curSaveTotalPrice) {
        this.curSaveTotalPrice = curSaveTotalPrice;
    }

    public Map<PrivilegeType, List<SimpleActVO>> getActDiscountGroup() {
        return actDiscountGroup;
    }

    public void setActDiscountGroup(Map<PrivilegeType, List<SimpleActVO>> actDiscountGroup) {
        this.actDiscountGroup = actDiscountGroup;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscounts() {
        return discounts;
    }

    public void setDiscounts(BigDecimal discounts) {
        this.discounts = discounts;
    }

    public SimpleProIfStock getSimpleProduct() {

        return simpleProduct;
    }

    public void setSimpleProduct(SimpleProIfStock simpleProduct) {
        this.simpleProduct = simpleProduct;
    }

    public long getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(long saleTime) {
        this.saleTime = saleTime;
    }
}
