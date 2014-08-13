package com.meiliwan.emall.account.web.vo;

import com.meiliwan.emall.account.bean.AccountBizLog;
import com.meiliwan.emall.commons.util.DateUtil;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-6-28
 * Time: 上午11:49
 */
public class WalletRecordVo {

    private Date time;
    private String tradeNo;
    private String type;
    private String from;
    private String money;
    private String status;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public static WalletRecordVo fromAccountBizLog(AccountBizLog accountBizLog){

        WalletRecordVo walletRecordVo=new WalletRecordVo();
        walletRecordVo.setTime(DateUtil.parseDateTime((String) accountBizLog.getExtraDataMap().get("opTime")));
        walletRecordVo.setMoney((String.valueOf(accountBizLog.getExtraDataMap().get("money"))) );
        String source=String.valueOf(accountBizLog.getExtraDataMap().get("source"));
        walletRecordVo.setFrom(source);

        walletRecordVo.setTradeNo(String.valueOf(accountBizLog.getExtraDataMap().get("sourceNo")));

        if(source.toString().equals("֧美丽湾")){
            walletRecordVo.setType("退款");
        }else{
            walletRecordVo.setType("充值");
        }

        if(String.valueOf(accountBizLog.getExtraDataMap().get("successFlag")).toLowerCase().equals("true")){
            walletRecordVo.setStatus("成功");
        }else{
            walletRecordVo.setStatus("未支付");
        }

        return  walletRecordVo;
    }
}
