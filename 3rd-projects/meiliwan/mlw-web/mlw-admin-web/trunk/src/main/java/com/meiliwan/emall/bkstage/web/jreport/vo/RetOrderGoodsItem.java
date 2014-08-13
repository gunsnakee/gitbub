package com.meiliwan.emall.bkstage.web.jreport.vo;

/**
 * Created with IntelliJ IDEA.
 * User: xiongyu
 * Date: 13-10-3
 * Time: 下午12:03
 * To change this template use File | Settings | File Templates.
 */
public class RetOrderGoodsItem {

    private int proId;
    //条形码
    private String proSn;

    private String proName;

    private double proSaleAmount;

    private int buyNum;

    private int retNum;

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public String getProSn() {
        return proSn;
    }

    public void setProSn(String proSn) {
        this.proSn = proSn;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getRetNum() {
        return retNum;
    }

    public void setRetNum(int retNum) {
        this.retNum = retNum;
    }

    public double getProSaleAmount() {
        return proSaleAmount;
    }

    public void setProSaleAmount(double proSaleAmount) {
        this.proSaleAmount = proSaleAmount;
    }
}
