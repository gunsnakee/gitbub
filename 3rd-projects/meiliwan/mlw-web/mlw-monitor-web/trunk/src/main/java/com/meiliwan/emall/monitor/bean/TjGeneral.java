package com.meiliwan.emall.monitor.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: yyluo
 * Date: 14-4-9
 * Time: 下午5:50
 * To change this template use File | Settings | File Templates.
 */
public class TjGeneral implements Serializable {
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    private String statTime;  //开始时间
    private String statDay;  //日期
    private String host;  //来源
    private Integer pv;
    private Integer uv;
    private Integer login;    //登录数
    private Integer register;  //注册
    private Integer ord;    //订单数
    private Integer ordSaleAmount;     // 下单金额

    public String getStatTime() {
        return statTime;
    }

    public void setStatTime(String statTime) {
        this.statTime = statTime;
    }

    public String getStatDay() {
        return statDay;
    }

    public void setStatDay(String statDay) {
        this.statDay = statDay;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Integer getLogin() {
        return login;
    }

    public void setLogin(Integer login) {
        this.login = login;
    }

    public Integer getRegister() {
        return register;
    }

    public void setRegister(Integer register) {
        this.register = register;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    public Integer getOrdSaleAmount() {
        return ordSaleAmount;
    }

    public void setOrdSaleAmount(Integer ordSaleAmount) {
        this.ordSaleAmount = ordSaleAmount;
    }

    public TjGeneral( Integer pv, Integer uv, Integer login, Integer register, Integer ord, Integer ordSaleAmount) {
        this.statDay = statDay;
        this.host = host;
        this.pv = pv;
        this.uv = uv;
        this.login = login;
        this.register = register;
        this.ord = ord;
        this.ordSaleAmount = ordSaleAmount;
    }

    public TjGeneral() {
    }



    //获取下单转化率： ord/uv
    public double getOrdConversionRate(){
        if(ord>0 && uv>0){
            return (double)ord*1000/(double)uv;
        }
        return 0;
    }
    //获取下单金额（元）
    public int getOrdSaleAmountYuan(){
        if(ordSaleAmount>0){
            return ordSaleAmount/100;
        }
        return 0;
    }


    //获取客单价（元）
    public int getPerOrdPrice(){
        if(ordSaleAmount>0 && ord>0){
            return ordSaleAmount/ord/100;
        }
        return 0;
    }

}
