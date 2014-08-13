package com.meiliwan.emall.account.client;

import static com.meiliwan.emall.icetool.IceClientTool.ACCOUNT_ICE_SERVICE;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.account.bean.WalletOptRecord;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.exception.IceServiceRuntimeException;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-7-10
 * Time: 下午9:17
 */
public class WalletOptRecordClient {
    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(WalletOptRecordClient.class);

    private static final String SERVICE_NAME = "walletOptRecordService/";

    /**
     * 插入一条记录
     * @param walletOptRecord
     * @return
     */
    public static  int addRecord(WalletOptRecord walletOptRecord){
        String iceFuncName="addRecord";
        JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName, walletOptRecord));
        return obj.get("resultObj")==null?0:obj.get("resultObj").getAsInt();
    }

    /**
     * 根据用户Id,内部流水号 获取日志记录
     * @param uId
     * @param innerNum
     * @return
     */
    public static WalletOptRecord getRecord(Integer uId,String innerNum){
        String iceFuncName="getRecord";
        JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName, uId,innerNum));
        return new Gson().fromJson(obj.get("resultObj"),WalletOptRecord.class);
    }

    /**
     * 分页查询记录
     * @param pageInfo
     * @param walletOptRecord
     * @param whereSql
     * @return
     */
    public static PagerControl<WalletOptRecord> getRecordByPage(PageInfo pageInfo,WalletOptRecord walletOptRecord,String whereSql){
        String iceFuncName="getRecordByPage";
        JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName,pageInfo,walletOptRecord,whereSql));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<WalletOptRecord>>(){}.getType());
    }

    /**
     * 查询记录是否成功
     * @param payId
     * @return
     */
    public static boolean isRecordSucess(String payId){
       try{
           String iceFuncName="isRecordSucess";
           JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName,payId));
           return obj.get("resultObj")==null?false:obj.get("resultObj").getAsBoolean();
       }catch(IceServiceRuntimeException e){
           LOGGER.error(e,"[钱包查询记录是否成功 ice服务 异常 payId]"+payId,"");
           return false;
       }
    }

    public static void main(String [] args){

        /*for(int i=0;i<10;i++){
            WalletOptRecord walletOptRecord=new WalletOptRecord();
            walletOptRecord.setInnerNum("sl00125"+i);
            walletOptRecord.setMoney(new BigDecimal(50));
            walletOptRecord.setUid(71);
            walletOptRecord.setMlwCoin(new BigDecimal(100));
            walletOptRecord.setOptType(AccountBizLogOp.ADD_MONEY.toString());
            walletOptRecord.setOptTime(new Date());
            walletOptRecord.setSuccessFlag(1);
            WalletOptRecordClient.addRecord(walletOptRecord);
        }

        System.out.println(WalletOptRecordClient.getRecord(71,"sl001251",50.0));

        PageInfo pageInfo=new PageInfo();
        pageInfo.setPage(1);
        pageInfo.setPagesize(5);
        WalletOptRecord query=new WalletOptRecord();
        query.setUid(71);
        PagerControl<WalletOptRecord> pc=WalletOptRecordClient.getRecordByPage(pageInfo,query);
        List<WalletOptRecord> list=pc.getEntityList();
        for(WalletOptRecord item:list){
            System.out.println(item.getInnerNum());
        }*/
        //System.out.println(WalletOptRecordClient.isRecordSucess("0000000043"));
        AccountWalletClient.addMoney(105005036,0.01,"00000106892212", PayCode.ALIPAY,null);
    }
}
