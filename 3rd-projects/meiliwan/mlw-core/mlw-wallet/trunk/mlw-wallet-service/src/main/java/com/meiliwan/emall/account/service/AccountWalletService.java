package com.meiliwan.emall.account.service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

import java.math.BigDecimal;
import java.util.Date;

import com.meiliwan.emall.account.bo.RechargeType;
import com.meiliwan.emall.base.client.IdGenClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.account.bean.AccountWallet;
import com.meiliwan.emall.account.bean.WalletDto;
import com.meiliwan.emall.account.bean.WalletOptRecord;
import com.meiliwan.emall.account.bo.AccountBizLogOp;
import com.meiliwan.emall.account.bo.AccountCommon;
import com.meiliwan.emall.account.constants.WalletAccountConstant;
import com.meiliwan.emall.account.dao.AccountWalletDao;
import com.meiliwan.emall.account.dao.WalletOptRecordDao;
import com.meiliwan.emall.account.util.WalletUtill;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.EncryptTools;
import com.meiliwan.emall.commons.util.MlwEncryptTools;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

@Service
public class AccountWalletService extends DefaultBaseServiceImpl {

    private final static MLWLogger logger= MLWLoggerFactory.getLogger(AccountWallet.class);
    @Autowired
    private AccountWalletDao accountWalletDao;

    @Autowired
    private WalletOptRecordDao walletOptRecordDao;





    public void add(JsonObject resultObj,AccountWallet wall){
        wall.setState((short)1);
        int insert = accountWalletDao.insert(wall);
        addToResult(insert,resultObj);
    }


    public void update(JsonObject resultObj,AccountWallet wall){
        int update = accountWalletDao.update(wall);
        addToResult(update,resultObj);
    }

    public void getAccountWalletByUid(JsonObject resultObj,Integer uid){
        AccountWallet entityById = accountWalletDao.getEntityById(uid);
        addToResult(entityById,resultObj);
    }

    /**
     *  增加一条状态为未支付的记录
     * @param object
     * @param walletOptRecord
     */
    public void addOptRecord(JsonObject object,WalletOptRecord walletOptRecord){
        AccountWallet accountWallet = accountWalletDao.getEntityById(walletOptRecord.getUid(),true);
        if(accountWallet == null){
            addToResult(AccountCommon.OPT_FAILURE,object);
            return;
        }
        BigDecimal currentMoney=walletOptRecord.getMoney().add(accountWallet.getMlwCoin());

        walletOptRecord.setMlwCoin(currentMoney);
        walletOptRecord.setSuccessFlag(-1);
        int result= walletOptRecordDao.insert(walletOptRecord);
        addToResult(result,object);

    }



    /**
     * @deprecated 请使用 addMoneyWithIp
     * 充值
     * @param resultObj
     * @param uid
     * @param money
     * @param innerNum   内部流水号，唯一，由pay调用方生成
     * @param payCode  充值渠道
     */
    @Deprecated
    public  void addMoney(JsonObject resultObj,Integer uid,double money,String innerNum,PayCode payCode,String outNum){

        // 检查钱包账户是否已经建立
        AccountWallet wall = accountWalletDao.getEntityById(uid,true);

        // 没建立则先建立账户
        if(wall==null){
            wall = new AccountWallet();
            wall.setUid(uid);
            wall.setMlwCoin(new BigDecimal(0));
            wall.setState((short)1);
            accountWalletDao.insert(wall);
        }

        // 账号被封锁了
        if(wall.getState()==-1){
            addToResult(AccountCommon.STATE_LOCK,resultObj);
            return ;
        }

        BigDecimal addMoney=new BigDecimal(money);
        BigDecimal currentMoney=wall.getMlwCoin().add(addMoney);

        //超过最大可存储金额
        if(currentMoney.doubleValue() > WalletAccountConstant.WALLET_MAX_MONEY){
            //记录入库
            WalletOptRecord walletOptRecord = new WalletOptRecord();
            walletOptRecord.setUid(uid);
            walletOptRecord.setMlwCoin(wall.getMlwCoin());
            walletOptRecord.setMoney(addMoney);
            walletOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
            walletOptRecord.setOptTime(new Date());
            walletOptRecord.setOptType(AccountBizLogOp.TOO_LARGER.toString());
            walletOptRecord.setSource(payCode.getDesc());
            walletOptRecord.setSuccessFlag(1);
            walletOptRecordDao.insert(walletOptRecord);

            //计入logger
            logger.warn("充值后总金额超过上限",walletOptRecord,"");

            //返回状态码
            addToResult(AccountCommon.MONEY_TOO_LARGER,resultObj);
            return ;
        }



        AccountWallet addWall = new AccountWallet();
        addWall.setUid(uid);
        addWall.setMlwCoin(currentMoney);

        int rows = accountWalletDao.update(addWall);
        //构造业务日志记录
        WalletOptRecord walletOptRecord=new WalletOptRecord();
        walletOptRecord.setInnerNum(innerNum);
        walletOptRecord.setOutNum(outNum == null ?innerNum:outNum);
        walletOptRecord.setMoney(addMoney);
        walletOptRecord.setUid(uid);
        walletOptRecord.setMlwCoin(currentMoney);
        walletOptRecord.setSource(payCode.getDesc());
        walletOptRecord.setOptType(AccountBizLogOp.ADD_MONEY.toString());
        walletOptRecord.setOptTime(new Date());


        if(rows > 0){
            //成功
            walletOptRecord.setSuccessFlag(1);
        }else{
            //失败
            walletOptRecord.setSuccessFlag(-1);
        }
        walletOptRecordDao.update(walletOptRecord);

        if(rows > 0){
            addToResult(currentMoney, resultObj);
            return;
        }
        addToResult(AccountCommon.OPT_FAILURE,resultObj);
    }

    /**
     * 消费
     * @param resultObj
     * @param uid
     * @param money
     * @param password
     * @param innerNum   内部流水号，唯一，由pay调用方生成
     */
    public void subMoney(JsonObject resultObj,String orderId, Integer uid,double money,String password,String innerNum){

        // 检查钱包账户是否已经建立
        AccountWallet wall = accountWalletDao.getEntityById(uid,true);

        // 没建立则先建立账户，并返回
        if(wall==null){
            wall = new AccountWallet();
            wall.setUid(uid);
            wall.setMlwCoin(new BigDecimal(0));
            accountWalletDao.insert(wall);
            addToResult(AccountCommon.NOT_ENOUGH,resultObj);
            return ;
        }

        // 账号被封锁了
        if(wall.getState()==-1){
            addToResult(AccountCommon.STATE_LOCK,resultObj);
            return ;
        }

        if(StringUtils.isNotEmpty(wall.getPayPasswd())){
            //先校验密码
            if(StringUtils.isEmpty(password) || !wall.getPayPasswd().equals(MlwEncryptTools.encryptAccountPwd(password)))
            {
                addToResult(AccountCommon.PASSWORD_ERROR,resultObj);
                return;
            }
        }

        //判断余额是否能够支付
        BigDecimal nowMoney = wall.getMlwCoin();
        if(NumberUtil.compareTo(nowMoney.doubleValue(), money) < 0)
        {
            logger.info("校验余额是否够支付","innerNum:" + innerNum + ",availMoney:" + nowMoney.doubleValue() + ",payMoney:" + money,null);
            addToResult(AccountCommon.NOT_ENOUGH,resultObj);
            return;
        }

        //成功后返回余额
        BigDecimal subMoney=new BigDecimal(money);
        BigDecimal currentMoney = new BigDecimal(NumberUtil.subtract(nowMoney.doubleValue(),money,2));
        AccountWallet subWall = new AccountWallet();
        subWall.setUid(uid);
        subWall.setMlwCoin(currentMoney);
        int rows = accountWalletDao.update(subWall);

        //构造业务日志记录
        WalletOptRecord walletOptRecord=new WalletOptRecord();
        walletOptRecord.setInnerNum(innerNum);
        walletOptRecord.setOrderId(orderId);
        walletOptRecord.setMoney(subMoney);
        walletOptRecord.setUid(uid);
        walletOptRecord.setMlwCoin(currentMoney);
        walletOptRecord.setOptType(AccountBizLogOp.SUB_MONEY.toString());
        walletOptRecord.setOptTime(new Date());

        if(rows > 0){
            //成功
            walletOptRecord.setSuccessFlag(1);
        }else{
            //失败
            walletOptRecord.setSuccessFlag(-1);
        }

        walletOptRecordDao.insert(walletOptRecord);

        if(rows > 0)
        {
            addToResult(currentMoney.doubleValue(), resultObj);
            return;
        }
        addToResult(AccountCommon.OPT_FAILURE,resultObj);
    }

    /**
     * 对业务日志的回滚,只回滚消费记录，使用场景 消费失败的情况下才使用
     * @param object
     * @param uid
     * @param innerNum
     * @param money
     * @param password
     */
    public void updateRollback(JsonObject object,Integer uid,String subInnerNum,String innerNum,double money,String password){
        //获取业务日志记录
        WalletOptRecord queryRecord=new WalletOptRecord();
        queryRecord.setUid(uid);
        queryRecord.setInnerNum(subInnerNum);
        WalletOptRecord result=walletOptRecordDao.getEntityByObj(queryRecord,true);
        if(result == null || !result.getOptType().equals(AccountBizLogOp.SUB_MONEY.toString()) || result.getMoney().compareTo(new BigDecimal(money))!= 0 ){
            addToResult(AccountCommon.ROLL_BACK_IN_VALID,object);
            return;
        }
        //验证密码
        AccountWallet accountWallet=accountWalletDao.getEntityById(uid,true);
        if(accountWallet ==null || !accountWallet.getPayPasswd().equals(EncryptTools.encrypt(password))){
            addToResult(AccountCommon.PASSWORD_ERROR,object);
            return;
        }
        //回滚
        BigDecimal rollbackMoney=new BigDecimal(money);
        BigDecimal currentMoney=accountWallet.getMlwCoin().add(rollbackMoney);
        AccountWallet newWallet=new AccountWallet();
        newWallet.setUid(uid);
        newWallet.setMlwCoin(currentMoney);
        int rows=accountWalletDao.update(newWallet);

        //业务日志记录
        WalletOptRecord record=new WalletOptRecord();
        record.setInnerNum(innerNum);
        record.setMoney(rollbackMoney);
        record.setUid(uid);
        record.setMlwCoin(currentMoney);
        record.setSource(PayCode.MLW_W.getDesc());
        record.setOptType(AccountBizLogOp.ROLL_BACK.toString());
        record.setOptTime(new Date());

        if(rows > 0){
            //成功
            record.setSuccessFlag(1);
        }else{
            //失败
            record.setSuccessFlag(-1);
        }

        walletOptRecordDao.insert(record);

        if(rows > 0) {
            addToResult(currentMoney, object);
            return;
        }
        addToResult(AccountCommon.OPT_FAILURE,object);
    }

    /**
     * 针对订单退款到钱包的场景,
     * @param object
     * @param uid
     * @param innerNum
     * @param money
     * @param payCode 目前只需要设置为 PayCode.MLW_ACCOUNT_WALLET
     */
    public void updateRefund(JsonObject object,Integer uid,String innerNum,double money,PayCode payCode){

        AccountWallet accountWallet= accountWalletDao.getEntityById(uid,true);
        if(accountWallet == null){
            addToResult(AccountCommon.OPT_FAILURE,object);
            return;
        }
        //回滚
        BigDecimal rollbackMoney=new BigDecimal(money);
        BigDecimal currentMoney=accountWallet.getMlwCoin().add(rollbackMoney);
        AccountWallet newWallet=new AccountWallet();
        newWallet.setUid(uid);
        newWallet.setMlwCoin(currentMoney);
        int rows=accountWalletDao.update(newWallet);

        //业务日志记录
        WalletOptRecord record=new WalletOptRecord();
        record.setInnerNum(innerNum);
        record.setMoney(rollbackMoney);
        record.setUid(uid);
        record.setMlwCoin(currentMoney);
        record.setSource(payCode.getDesc());
        record.setOptType(AccountBizLogOp.ROLL_BACK.toString());
        record.setOptTime(new Date());

        if(rows > 0){
            //成功
            record.setSuccessFlag(1);
        }else{
            //失败
            record.setSuccessFlag(-1);
        }

        walletOptRecordDao.insert(record);

        if(rows > 0) {
            addToResult(currentMoney, object);
            return;
        }
        addToResult(AccountCommon.OPT_FAILURE,object);
    }


    /************************ 增加冻结概念后新增的方法 ************************/

    /**
     * 冻结
     * @param resultObj
     * @param walletDto
     */
    public void updateFreezeMoney(JsonObject resultObj,WalletDto walletDto){
        AccountWallet queryWallet= accountWalletDao.getEntityById(walletDto.getuId(), true);
        double currentMoney;
       // double currentFreezeAmount;
        if( queryWallet != null){
            currentMoney = queryWallet.getMlwCoin().doubleValue();

            //判断钱币是否够冻结
            if(NumberUtil.compareTo(currentMoney, walletDto.getMoney()) < 0) {
                addToResult(AccountCommon.NOT_ENOUGH,resultObj);
                return;
            }
            //扣减 美丽币 到  冻结款中
            BigDecimal resultMoney = new BigDecimal(NumberUtil.subtract(currentMoney,walletDto.getMoney(),2));

            int rows = accountWalletDao.freezeMoney(walletDto.getuId(),walletDto.getMoney());

            //记录操作日志
            WalletOptRecord walletOptRecord = new WalletOptRecord();
            walletOptRecord.setUid(walletDto.getuId());
            walletOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
            walletOptRecord.setOptTime(new Date());
            walletOptRecord.setMlwCoin(resultMoney);
            walletOptRecord.setMoney(new BigDecimal(walletDto.getMoney()));
            walletOptRecord.setOrderId(walletDto.getOrderId());
            walletOptRecord.setOptType(AccountBizLogOp.FREEZE_MONEY.toString());
            walletOptRecord.setClientIp(walletDto.getClientIp()== null?"":walletDto.getClientIp());
            walletOptRecord.setAdminId(walletDto.getAdminId());
            if(rows > 0){
                walletOptRecord.setSuccessFlag(1);
            }else{
                walletOptRecord.setSuccessFlag(-1);
            }

            walletOptRecordDao.insert(walletOptRecord);
            if(rows > 0){
                addToResult(resultMoney.doubleValue(),resultObj);
                return;
            }
        }

        addToResult(AccountCommon.OPT_FAILURE,resultObj);
    }

    @Deprecated
    public void updateRefundFromFreeze(JsonObject resultObj,String orderId){
        //1.校验记录是否符合冻结回退条件
        WalletOptRecord queryRecord = getFreezeRecord(orderId,true);
        if(queryRecord == null){
            addToResult(AccountCommon.RECORD_NOT_EXIT,resultObj);
            return;
        }

        AccountWallet accountWallet = accountWalletDao.getEntityById(queryRecord.getUid(),true);
        if( accountWallet != null){
            double  currentMoney = accountWallet.getMlwCoin().doubleValue();

            BigDecimal resultMoney = new BigDecimal(NumberUtil.add(currentMoney,queryRecord.getMoney().doubleValue()));

            int rows = accountWalletDao.unFreezeMoney(queryRecord.getUid(),queryRecord.getMoney().doubleValue());
            //4.记录操作日志
            WalletOptRecord walletOptRecord = new WalletOptRecord();
            walletOptRecord.setUid(queryRecord.getUid());
            walletOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
            walletOptRecord.setOptTime(new Date());
            walletOptRecord.setMlwCoin(resultMoney);
            walletOptRecord.setMoney(queryRecord.getMoney());
            walletOptRecord.setOrderId(orderId);
            walletOptRecord.setOptType(AccountBizLogOp.REFUND_FROM_FREEZE.toString());
            walletOptRecord.setSource(PayCode.MLW_W.getDesc());
            if(rows > 0){
                walletOptRecord.setSuccessFlag(1);
            }else{
                walletOptRecord.setSuccessFlag(-1);
            }

            walletOptRecordDao.insert(walletOptRecord);
            if(rows > 0){
                addToResult(resultMoney.doubleValue(),resultObj);
                return;
            }

        }
        addToResult(AccountCommon.OPT_FAILURE,resultObj);

    }
    /**
     * 获取指定订单的冻结记录
     * @param orderId
     * @param isMainDB 主从库选择
     * @return
     */
    private WalletOptRecord getFreezeRecord(String orderId,boolean isMainDB) {
        WalletOptRecord queryRecord = new WalletOptRecord();
        queryRecord.setOrderId(orderId);
        queryRecord.setOptType(AccountBizLogOp.FREEZE_MONEY.toString());
        queryRecord.setSuccessFlag(1);
        return walletOptRecordDao.getEntityByObj(queryRecord,isMainDB);
    }


    /**
     * 在冻结的基础上进行消费支付
     * @param resultObj
     * @param walletDto
     */
    public void payMoney(JsonObject resultObj,WalletDto walletDto){

        //1.获取冻结记录,检验合法性
        WalletOptRecord queryRecord = getFreezeRecord(walletDto.getOrderId(),true);
        if( queryRecord != null && NumberUtil.compareTo(queryRecord.getMoney().doubleValue(),walletDto.getMoney()) == 0){
            AccountWallet queryWallet = accountWalletDao.getEntityById(walletDto.getuId(),true);
            double currentFreezeAmount;
            if(queryWallet != null){
                currentFreezeAmount = queryWallet.getFreezeAmount().doubleValue();

                //2.扣减冻结款
                BigDecimal resultFreezeAmount = new BigDecimal(NumberUtil.subtract(currentFreezeAmount,walletDto.getMoney()));
                AccountWallet accountWallet = new AccountWallet();
                accountWallet.setUid(walletDto.getuId());
                accountWallet.setFreezeAmount(resultFreezeAmount);
                int rows =  accountWalletDao.update(accountWallet);

                //3.更改操作日志
                queryRecord.setSource(PayCode.MLW_W.getDesc());
                queryRecord.setOptTime(new Date());
                queryRecord.setOptType(AccountBizLogOp.SUB_MONEY.toString());
                if(rows > 0){
                    queryRecord.setSuccessFlag(1);
                }else{
                    queryRecord.setSuccessFlag(-1);
                }
                walletOptRecordDao.update(queryRecord);

               if( rows > 0){
                   addToResult(queryWallet.getMlwCoin().doubleValue(),resultObj);
                   return;
               }
            }
            addToResult(AccountCommon.OPT_FAILURE,resultObj);
            return;
        }
        addToResult(AccountCommon.PARAM_IN_VALID,resultObj);
    }


    /** 供支付调用，进行退款
     * gateway
     * @param resultObj
     * @param walletDto
     */
    public void updateRefundFromGateway(JsonObject resultObj,WalletDto walletDto){
        //1.获取消费记录
        String orderId = walletDto.getOrderId().split("-")[0];
        String retOrderId = walletDto.getOrderId().split("-")[1];
        boolean flag = orderId.equals(retOrderId)?true:false;

        /**取消校验**/
//        WalletOptRecord queryRecord = new WalletOptRecord();
//        queryRecord.setOrderId(orderId);
//        queryRecord.setOptType(AccountBizLogOp.SUB_MONEY.toString());
//        WalletOptRecord resultRecord = walletOptRecordDao.getEntityByObj(queryRecord,true);



//        if(resultRecord != null ){
            //2.退款到美丽钱包
            AccountWallet queryWallet = accountWalletDao.getEntityById(walletDto.getuId(),true);
            double currentMoney;
            if( queryWallet != null ){
                currentMoney = queryWallet.getMlwCoin().doubleValue();
                BigDecimal resultMoney = new BigDecimal(NumberUtil.add(currentMoney,walletDto.getMoney()));

                //超过最大可存储金额
                if(resultMoney.doubleValue() > WalletAccountConstant.WALLET_MAX_MONEY){
                    //记录入库
                    WalletOptRecord walletOptRecord = new WalletOptRecord();
                    walletOptRecord.setUid(walletDto.getuId());
                    walletOptRecord.setMlwCoin(new BigDecimal(currentMoney));
                    walletOptRecord.setMoney(new BigDecimal(walletDto.getMoney()));
                    walletOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
                    walletOptRecord.setOptTime(new Date());
                    walletOptRecord.setOptType(AccountBizLogOp.TOO_LARGER.toString());
                    walletOptRecord.setSource(PayCode.MLW_W.getDesc());
                    walletOptRecord.setSuccessFlag(1);
                    walletOptRecord.setClientIp(walletDto.getClientIp()== null?"":walletDto.getClientIp());
                    walletOptRecord.setAdminId(walletDto.getAdminId());
                    walletOptRecordDao.insert(walletOptRecord);

                    //计入logger
                    logger.warn("退款后总金额超过上限", walletOptRecord, "");

                    //返回状态码
                    addToResult(AccountCommon.MONEY_TOO_LARGER,resultObj);
                    return ;
                }


                AccountWallet accountWallet = new AccountWallet();
                accountWallet.setUid(walletDto.getuId());
                accountWallet.setMlwCoin( resultMoney);
                int rows =  accountWalletDao.update(accountWallet);


                //3.写记录
                WalletOptRecord walletOptRecord = new WalletOptRecord();
                walletOptRecord.setUid(walletDto.getuId());
                walletOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
                walletOptRecord.setOptTime(new Date());
                walletOptRecord.setMlwCoin(resultMoney);
                walletOptRecord.setMoney(new BigDecimal(walletDto.getMoney()));
                walletOptRecord.setOrderId(retOrderId);
                walletOptRecord.setClientIp(walletDto.getClientIp()== null?"":walletDto.getClientIp());
                walletOptRecord.setAdminId(walletDto.getAdminId());
                if(flag){
                    walletOptRecord.setOptType(AccountBizLogOp.REFUND_FROM_FREEZE.toString());
                }else{
                    walletOptRecord.setOptType(AccountBizLogOp.REFUND_FROM_GATEWAY.toString());
                }
                walletOptRecord.setSource(PayCode.MLW_W.getDesc());
                if(rows > 0){
                    walletOptRecord.setSuccessFlag(1);
                }else{
                    walletOptRecord.setSuccessFlag(-1);
                }

                walletOptRecordDao.insert(walletOptRecord);

                if( rows > 0){
                    addToResult(resultMoney.doubleValue(),resultObj);
                    return;
                }
            }

            addToResult(AccountCommon.OPT_FAILURE,resultObj);
//            return;
//        }
//        addToResult(AccountCommon.PARAM_IN_VALID,resultObj);
    }


    /**
     * 客户端密码密码是否正确
     * @param resultObj
     * @param uId
     * @param payPassword
     */
    public void isPayPasswordRight(JsonObject resultObj,Integer uId,String payPassword){
        boolean flag = false;
        int errorCount = 0;
        try {
            String errorCountStr = ShardJedisTool.getInstance().get(JedisKey.account$payerrorcount,uId);
            if(!StringUtils.isEmpty(errorCountStr))errorCount = Integer.valueOf(errorCountStr);

        } catch (JedisClientException e) {
            logger.error(e,"uid:" +uId + ",payPassword:" +payPassword,resultObj.get(CLIENT_IP_STR).getAsString());
            errorCount = 0;
        }
        if(errorCount >= 4){
            addToResult(flag,resultObj);
            return;
        }
        AccountWallet accountWallet = accountWalletDao.getEntityById(uId);
        if(accountWallet != null && MlwEncryptTools.encryptAccountPwd(payPassword).equals(accountWallet.getPayPasswd())){
                flag =true;
        }
        addToResult(flag,resultObj);
    }


   public void isPayPwdFreeze(JsonObject resultObj,Integer uId){
       int errorCount = 0;
       try {
           String errorCountStr = ShardJedisTool.getInstance().get(JedisKey.account$payerrorcount,uId);
           if(!StringUtils.isEmpty(errorCountStr))errorCount = Integer.valueOf(errorCountStr);

       } catch (JedisClientException e) {
           logger.error(e,"[支付密码冻结读取Redis 异常 uid:]"+uId,"");
           errorCount = 0;
       }
       if(errorCount >= 4){
           addToResult(1,resultObj);
       }else{
           addToResult(0,resultObj);
       }
   }

  /******************* 5.1.2 优化 *************************/

    /**
     * 描述：根据用户id 和 字符密码 进行支付密码的校验和钱包金额的获取
     * 返回：支付密码错误，返回支付密码错误状态；支付密码冻结返回支付密码冻结状态；支付密码正确，返回钱包金额；所有的状态信息@see AccountCommon
     * @param resultObj
     * @param paypwd
     * @param uId
     */
  public void validatePwdAndGetMoney(JsonObject resultObj,String paypwd,Integer uId){

      int errorCount = 0;
      try {
          String errorCountStr = ShardJedisTool.getInstance().get(JedisKey.account$payerrorcount,uId);
          if(!StringUtils.isEmpty(errorCountStr))errorCount = Integer.valueOf(errorCountStr);

      } catch (JedisClientException e) {
          logger.error(e,"uid:" +uId + ",payPassword:" +paypwd,resultObj.get(CLIENT_IP_STR).getAsString());
          errorCount = 0;
      }

      //钱包被冻结
      if(errorCount >= 4){
          addToResult(AccountCommon.STATE_FREEZE,resultObj);
          return;
      }

      //钱包密码错误
      AccountWallet accountWallet = accountWalletDao.getEntityById(uId);
      //放开密码校验
      if(accountWallet != null && !StringUtils.isEmpty(accountWallet.getPayPasswd()) &&!MlwEncryptTools.encryptAccountPwd(paypwd).equals(accountWallet.getPayPasswd())){
         addToResult(AccountCommon.PASSWORD_ERROR,resultObj);
         return;
      }

      //返回余额
      addToResult(accountWallet.getMlwCoin(),resultObj);
  }


    /**
     * 充值
     * @param resultObj
     * @param uid
     * @param money
     * @param innerNum   内部流水号，唯一，由pay调用方生成
     * @param payCode  充值渠道
     * @param ip 客户机器ip
     */
    public  void addMoneyWithIp(JsonObject resultObj,Integer uid,double money,String innerNum,PayCode payCode,String outNum,String ip){

        // 检查钱包账户是否已经建立
        AccountWallet wall = accountWalletDao.getEntityById(uid,true);

        // 没建立则先建立账户
        if(wall==null){
            wall = new AccountWallet();
            wall.setUid(uid);
            wall.setMlwCoin(new BigDecimal(0));
            wall.setState((short)1);
            accountWalletDao.insert(wall);
        }

        // 账号被封锁了
        if(wall.getState()==-1){
            addToResult(AccountCommon.STATE_LOCK,resultObj);
            return ;
        }

        BigDecimal addMoney=new BigDecimal(money);
        BigDecimal currentMoney=wall.getMlwCoin().add(addMoney);

        //超过最大可存储金额
        if(currentMoney.doubleValue() > WalletAccountConstant.WALLET_MAX_MONEY){
            //记录入库
            WalletOptRecord walletOptRecord = new WalletOptRecord();
            walletOptRecord.setUid(uid);
            walletOptRecord.setMlwCoin(wall.getMlwCoin());
            walletOptRecord.setMoney(addMoney);
            walletOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
            walletOptRecord.setOptTime(new Date());
            walletOptRecord.setOptType(AccountBizLogOp.TOO_LARGER.toString());
            walletOptRecord.setSource(payCode.getDesc());
            walletOptRecord.setSuccessFlag(1);
            walletOptRecordDao.insert(walletOptRecord);

            //计入logger
            logger.warn("充值后总金额超过上限",walletOptRecord,"");

            //返回状态码
            addToResult(AccountCommon.MONEY_TOO_LARGER,resultObj);
            return ;
        }



        AccountWallet addWall = new AccountWallet();
        addWall.setUid(uid);
        addWall.setMlwCoin(currentMoney);

        int rows = accountWalletDao.update(addWall);
        //构造业务日志记录
        WalletOptRecord walletOptRecord=new WalletOptRecord();
        walletOptRecord.setInnerNum(innerNum);
        walletOptRecord.setOutNum(outNum == null ?innerNum:outNum);
        walletOptRecord.setMoney(addMoney);
        walletOptRecord.setUid(uid);
        walletOptRecord.setMlwCoin(currentMoney);
        walletOptRecord.setSource(payCode.getDesc());
        walletOptRecord.setOptType(AccountBizLogOp.ADD_MONEY.toString());
        walletOptRecord.setOptTime(new Date());
        walletOptRecord.setClientIp(ip == null?"":ip);


        if(rows > 0){
            //成功
            walletOptRecord.setSuccessFlag(1);
        }else{
            //失败
            walletOptRecord.setSuccessFlag(-1);
        }
        walletOptRecordDao.update(walletOptRecord);

        if(rows > 0){
            addToResult(currentMoney, resultObj);
            return;
        }
        addToResult(AccountCommon.OPT_FAILURE,resultObj);
    }

    /**
     * 新增方法，记录 ip 和 admin Id
     * @param resultObj
     * @param orderId
     * @param ip
     * @param adminId
     */
    public void updateRefundFromFreezeWithIp(JsonObject resultObj,String orderId,String ip,Integer adminId){
        //1.校验记录是否符合冻结回退条件
        WalletOptRecord queryRecord = getFreezeRecord(orderId,true);
        if(queryRecord == null){
            addToResult(AccountCommon.RECORD_NOT_EXIT,resultObj);
            return;
        }

        AccountWallet accountWallet = accountWalletDao.getEntityById(queryRecord.getUid(),true);
        if( accountWallet != null){
            double  currentMoney = accountWallet.getMlwCoin().doubleValue();

            BigDecimal resultMoney = new BigDecimal(NumberUtil.add(currentMoney,queryRecord.getMoney().doubleValue()));

            int rows = accountWalletDao.unFreezeMoney(queryRecord.getUid(),queryRecord.getMoney().doubleValue());
            //4.记录操作日志
            WalletOptRecord walletOptRecord = new WalletOptRecord();
            walletOptRecord.setUid(queryRecord.getUid());
            walletOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
            walletOptRecord.setOptTime(new Date());
            walletOptRecord.setMlwCoin(resultMoney);
            walletOptRecord.setMoney(queryRecord.getMoney());
            walletOptRecord.setOrderId(orderId);
            walletOptRecord.setOptType(AccountBizLogOp.REFUND_FROM_FREEZE.toString());
            walletOptRecord.setSource(PayCode.MLW_W.getDesc());
            walletOptRecord.setClientIp(ip);
            walletOptRecord.setAdminId(adminId);
            if(rows > 0){
                walletOptRecord.setSuccessFlag(1);
            }else{
                walletOptRecord.setSuccessFlag(-1);
            }

            walletOptRecordDao.insert(walletOptRecord);
            if(rows > 0){
                addToResult(resultMoney.doubleValue(),resultObj);
                return;
            }

        }
        addToResult(AccountCommon.OPT_FAILURE,resultObj);

    }



    /**
     * 分页查询记录
     * @param jsonObject
     * @param pageInfo
     * @param accountWallet
     * @param whereSql
     */
    public void getRecordByPage(JsonObject jsonObject,PageInfo pageInfo,AccountWallet accountWallet,String whereSql){
        pageInfo.setOrderDirection("desc");
        pageInfo.setOrderField("opt_time");

        PagerControl<AccountWallet> pc=accountWalletDao.getPagerByObj(accountWallet,pageInfo,whereSql);
        JSONTool.addToResult(pc, jsonObject);
    }



    /**
     * 充值,区分不同的充值类型
     * @param resultObj
     * @param uid
     * @param money
     * @param ip 客户机器ip
     * @param rechargeType 充值子类型
     */
    public  void addMoney4Type(JsonObject resultObj,Integer uid,double money,String ip,RechargeType rechargeType){

        // 检查钱包账户是否已经建立
        AccountWallet wall = accountWalletDao.getEntityById(uid,true);

        // 没建立则先建立账户
        if(wall==null){
            wall = new AccountWallet();
            wall.setUid(uid);
            wall.setMlwCoin(new BigDecimal(0));
            wall.setState((short)1);
            accountWalletDao.insert(wall);
        }

        // 账号被封锁了
        if(wall.getState()==-1){
            addToResult(AccountCommon.STATE_LOCK,resultObj);
            return ;
        }


        BigDecimal addMoney=new BigDecimal(money);
        BigDecimal currentMoney=wall.getMlwCoin().add(addMoney);

        //超过最大可存储金额
        if(currentMoney.doubleValue() > WalletAccountConstant.WALLET_MAX_MONEY){
            //记录入库
            WalletOptRecord walletOptRecord = new WalletOptRecord();
            walletOptRecord.setUid(uid);
            walletOptRecord.setMlwCoin(wall.getMlwCoin());
            walletOptRecord.setMoney(addMoney);
            walletOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
            walletOptRecord.setOptTime(new Date());
            walletOptRecord.setOptType(AccountBizLogOp.TOO_LARGER.toString());
            walletOptRecord.setSource(PayCode.MLW_W.getDesc());
            walletOptRecord.setSuccessFlag(1);
            walletOptRecordDao.insert(walletOptRecord);

            //计入logger
            logger.warn("充值后总金额超过上限",walletOptRecord,"");

            //返回状态码
            addToResult(AccountCommon.MONEY_TOO_LARGER,resultObj);
            return ;
        }



        AccountWallet addWall = new AccountWallet();
        addWall.setUid(uid);
        addWall.setMlwCoin(currentMoney);

        int rows = accountWalletDao.update(addWall);
        //构造业务日志记录
        String innerNum = IdGenClient.getPayId() ;

        WalletOptRecord walletOptRecord=new WalletOptRecord();
        walletOptRecord.setInnerNum(innerNum);
        walletOptRecord.setOutNum(innerNum);
        walletOptRecord.setMoney(addMoney);
        walletOptRecord.setUid(uid);
        walletOptRecord.setMlwCoin(currentMoney);
        walletOptRecord.setSource(PayCode.MLW_W.getDesc());
        walletOptRecord.setOptType(AccountBizLogOp.ADD_MONEY.toString());
        walletOptRecord.setOptTime(new Date());
        walletOptRecord.setClientIp(ip == null?"":ip);
        walletOptRecord.setChildType(rechargeType.toString());


        if(rows > 0){
            //成功
            walletOptRecord.setSuccessFlag(1);
        }else{
            //失败
            walletOptRecord.setSuccessFlag(-1);
        }
        walletOptRecordDao.insert(walletOptRecord);

        if(rows > 0){
            addToResult(currentMoney, resultObj);
            return;
        }
        addToResult(AccountCommon.OPT_FAILURE,resultObj);
    }




}
