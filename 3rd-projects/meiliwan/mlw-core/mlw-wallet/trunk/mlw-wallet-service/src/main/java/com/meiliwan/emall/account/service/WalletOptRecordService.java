package com.meiliwan.emall.account.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.account.bean.WalletOptRecord;
import com.meiliwan.emall.account.dao.WalletOptRecordDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-7-10
 * Time: 下午8:43
 */
@Service
public class WalletOptRecordService extends DefaultBaseServiceImpl{
     @Autowired
     private WalletOptRecordDao walletOptRecordDao;

    /**
     * 插入一条记录
     * @param jsonObject
     * @param walletOptRecord
     */
     public void addRecord(JsonObject jsonObject,WalletOptRecord walletOptRecord){
         int result=walletOptRecordDao.insert(walletOptRecord);
         JSONTool.addToResult(result,jsonObject);
     }

    /**
     * 根据用户Id,内部流水号 获取日志记录
     * @param jsonObject
     * @param uId
     * @param innerNum
     */
    public void getRecord(JsonObject jsonObject,Integer uId,String innerNum){
        WalletOptRecord walletOptRecord=new WalletOptRecord();
        walletOptRecord.setUid(uId);
        walletOptRecord.setInnerNum(innerNum);

        WalletOptRecord result=walletOptRecordDao.getEntityByObj(walletOptRecord);
        JSONTool.addToResult(result,jsonObject);
    }

    /**
     * 分页查询记录
     * @param jsonObject
     * @param pageInfo
     * @param walletOptRecord
     * @param whereSql
     */
    public void getRecordByPage(JsonObject jsonObject,PageInfo pageInfo,WalletOptRecord walletOptRecord,String whereSql){
        pageInfo.setOrderDirection("desc");
        pageInfo.setOrderField("opt_time");

        PagerControl<WalletOptRecord> pc=walletOptRecordDao.getPagerByObj(walletOptRecord,pageInfo,whereSql);
        JSONTool.addToResult(pc,jsonObject);
    }

    /**
     * 查询记录状态
     * @param jsonObject
     */
    public void isRecordSucess(JsonObject jsonObject,String payId){
        boolean flag = false;
        WalletOptRecord walletOptRecord = new WalletOptRecord();
        walletOptRecord.setInnerNum(payId);
        WalletOptRecord result = walletOptRecordDao.getEntityByObj(walletOptRecord);
        if(result != null && result.getSuccessFlag() == 1){
            flag =true;
        }
        JSONTool.addToResult(flag,jsonObject);
    }

}
