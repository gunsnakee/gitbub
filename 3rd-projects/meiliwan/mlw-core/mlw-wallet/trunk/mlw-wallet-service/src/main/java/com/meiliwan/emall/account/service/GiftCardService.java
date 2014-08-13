package com.meiliwan.emall.account.service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.account.bean.AccountCard;
import com.meiliwan.emall.account.bean.AccountWallet;
import com.meiliwan.emall.account.bean.CardOptRecord;
import com.meiliwan.emall.account.bean.WalletDto;
import com.meiliwan.emall.account.bean.WalletOptRecord;
import com.meiliwan.emall.account.bo.AccountBizLogOp;
import com.meiliwan.emall.account.bo.AccountCommon;
import com.meiliwan.emall.account.constants.WalletAccountConstant;
import com.meiliwan.emall.account.dao.AccountCardDao;
import com.meiliwan.emall.account.dao.AccountWalletDao;
import com.meiliwan.emall.account.dao.CardOptRecordDao;
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
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.EncryptTools;
import com.meiliwan.emall.commons.util.MlwEncryptTools;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

@Service
public class GiftCardService extends DefaultBaseServiceImpl {

    private final static MLWLogger logger= MLWLoggerFactory.getLogger(AccountCard.class);
    @Autowired
    private AccountCardDao accountCardDao;

    @Autowired
    private CardOptRecordDao cardOptRecordDao;
    
    @Autowired
    private WalletOptRecordDao walletOptRecordDao ;
    
    @Autowired
    private AccountWalletDao accountWalletDao;

    public void add(JsonObject resultObj,AccountCard card){
    	card.setStatus((short)1);
        int insert = accountCardDao.insert(card);
        addToResult(insert,resultObj);
    }


    public void update(JsonObject resultObj,AccountCard wall){
        int update = accountCardDao.update(wall);
        addToResult(update,resultObj);
    }

    public void getAccountCardByUid(JsonObject resultObj,Integer uid){
        AccountCard entityById = accountCardDao.getEntityById(uid);
        addToResult(entityById,resultObj);
    }

    /**
     *  增加一条状态为未支付的记录
     * @param object
     * @param cardOptRecord
     */
    public void addOptRecord(JsonObject object,CardOptRecord cardOptRecord){
        AccountCard accountCard = accountCardDao.getEntityById(cardOptRecord.getUid(),true);
        if(accountCard == null){
            addToResult(AccountCommon.OPT_FAILURE,object);
            return;
        }
        BigDecimal currentMoney=cardOptRecord.getMoney().add(accountCard.getCardCoin());

        cardOptRecord.setCardCoin(currentMoney);
        cardOptRecord.setSuccessFlag(-1);
        int result= cardOptRecordDao.insert(cardOptRecord);
        addToResult(result,object);
    }

    
    /**
     * 激活 - 充值
     * @param resultObj
     * @param uid        用户id
     * @param money      充值金额
     * @param cardNum    礼品卡卡号 唯一
     * @param payCode    充值渠道
     * @param ip         客户机器ip
     */
    public  void addMoneyWithIp(JsonObject resultObj,Integer uid,double money,String source,String cardNum,String ip){

        // 检查礼品卡账户是否已经建立
        AccountCard wall = accountCardDao.getEntityById(uid,true);

        // 没建立则先建立礼品卡账户
        if(wall==null){
            wall = new AccountCard();
            wall.setUid(uid);
            wall.setCardCoin(new BigDecimal(0));
            wall.setStatus((short)1);
            accountCardDao.insert(wall);
        }

        // 账号被封锁了
        if(wall.getStatus() == -1){
            addToResult(AccountCommon.STATE_LOCK,resultObj);
            return ;
        }
        //今天激活超过20次
        Date beginDate = DateUtil.parseDate(DateUtil.getDateStr(new Date()));
        int addCounts = cardOptRecordDao.getTodayAddCounts(uid, beginDate);
        if(addCounts >= 20){
        	addToResult(-20,resultObj);
            return ;
        }

        BigDecimal addMoney=new BigDecimal(money);
        BigDecimal currentMoney=wall.getCardCoin().add(addMoney);

        //超过最大可存储金额
        if(currentMoney.doubleValue() > WalletAccountConstant.WALLET_MAX_MONEY){
            //记录入库
            CardOptRecord cardOptRecord = new CardOptRecord();
            cardOptRecord.setUid(uid);
            cardOptRecord.setCardCoin(wall.getCardCoin());
            cardOptRecord.setMoney(addMoney);
            cardOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
            cardOptRecord.setOptTime(new Date());
            cardOptRecord.setOptType(AccountBizLogOp.TOO_LARGER.toString());
            cardOptRecord.setSource(source);
            cardOptRecord.setSuccessFlag(1);
            cardOptRecordDao.insert(cardOptRecord);

            //计入logger
            logger.warn("充值后总金额超过上限",cardOptRecord,"");

            //返回状态码
            addToResult(AccountCommon.MONEY_TOO_LARGER,resultObj);
            return ;
        }



        AccountCard addWall = new AccountCard();
        addWall.setUid(uid);
        addWall.setCardCoin(currentMoney);

        int rows = accountCardDao.update(addWall);
        //构造业务日志记录
        CardOptRecord cardOptRecord=new CardOptRecord();
        cardOptRecord.setInnerNum(cardNum);
        cardOptRecord.setCardNum(cardNum);
        cardOptRecord.setMoney(addMoney);
        cardOptRecord.setUid(uid);
        cardOptRecord.setCardCoin(currentMoney);
        cardOptRecord.setSource(source);
        cardOptRecord.setOptType(AccountBizLogOp.ADD_MONEY.toString());
        cardOptRecord.setOptTime(new Date());
        cardOptRecord.setClientIp(ip == null?"":ip);

        if(rows > 0){
            //成功
            cardOptRecord.setSuccessFlag(1);
        }else{
            //失败
            cardOptRecord.setSuccessFlag(-1);
        }
        cardOptRecordDao.insert(cardOptRecord);

        if(rows > 0){
            addToResult(currentMoney, resultObj);
            return;
        }
        addToResult(AccountCommon.OPT_FAILURE,resultObj);
    }
    
    
    
    /**
     * 消费 暂时没地方用，可以提供给controller参考
     * @param resultObj
     * @param uid
     * @param money
     * @param password
     * @param innerNum   内部流水号，唯一，由pay调用方生成
     */
	public void subMoney(JsonObject resultObj,String orderId, Integer uid,double money,String password,String innerNum){

        // 检查钱包账户是否已经建立
        AccountCard card = accountCardDao.getEntityById(uid,true);
        AccountWallet wall = accountWalletDao.getEntityById(uid,true);

        // 没建立则先建立账户，并返回
        if(card==null){
        	card = new AccountCard();
        	card.setUid(uid);
        	card.setCardCoin(new BigDecimal(0));
            accountCardDao.insert(card);
            addToResult(AccountCommon.NOT_ENOUGH,resultObj);
            return ;
        }

        // 账号被封锁了
        if(card.getStatus()==-1){
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
        BigDecimal nowMoney = card.getCardCoin();
        if(NumberUtil.compareTo(nowMoney.doubleValue(), money) < 0)
        {
            logger.info("校验余额是否够支付","innerNum:" + innerNum + ",availMoney:" + nowMoney.doubleValue() + ",payMoney:" + money,"");
            addToResult(AccountCommon.NOT_ENOUGH,resultObj);
            return;
        }

        //成功后返回余额
        BigDecimal subMoney=new BigDecimal(money);
        BigDecimal currentMoney = new BigDecimal(NumberUtil.subtract(nowMoney.doubleValue(),money,2));
        AccountCard subWall = new AccountCard();
        subWall.setUid(uid);
        subWall.setCardCoin(currentMoney);
        int rows = accountCardDao.update(subWall);

        //构造业务日志记录
        CardOptRecord cardOptRecord=new CardOptRecord();
        cardOptRecord.setInnerNum(innerNum);
        cardOptRecord.setOrderId(orderId);
        cardOptRecord.setMoney(subMoney);
        cardOptRecord.setUid(uid);
        cardOptRecord.setCardCoin(currentMoney);
        cardOptRecord.setOptType(AccountBizLogOp.SUB_MONEY.toString());
        cardOptRecord.setOptTime(new Date());

        if(rows > 0){
            //成功
            cardOptRecord.setSuccessFlag(1);
        }else{
            //失败
            cardOptRecord.setSuccessFlag(-1);
        }

        cardOptRecordDao.insert(cardOptRecord);

        if(rows > 0)
        {
            addToResult(currentMoney.doubleValue(), resultObj);
            return;
        }
        addToResult(AccountCommon.OPT_FAILURE,resultObj);
    }


    /**
     * 冻结
     * @param resultObj
     * @param walletDto
     */
    public void updateFreezeMoney(JsonObject resultObj,WalletDto walletDto){
        AccountCard queryWallet= accountCardDao.getEntityById(walletDto.getuId(), true);
        double currentMoney;
        if( queryWallet != null){
            currentMoney = queryWallet.getCardCoin().doubleValue();

            //判断钱币是否够冻结
            if(NumberUtil.compareTo(currentMoney, walletDto.getMoney()) < 0) {
                addToResult(AccountCommon.NOT_ENOUGH,resultObj);
                return;
            }
            //扣减 美丽币 到  冻结款中
            BigDecimal resultMoney = new BigDecimal(NumberUtil.subtract(currentMoney,walletDto.getMoney(),2));

            int rows = accountCardDao.freezeMoney(walletDto.getuId(),walletDto.getMoney());

            //记录操作日志
            CardOptRecord cardOptRecord = new CardOptRecord();
            cardOptRecord.setUid(walletDto.getuId());
            cardOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
            cardOptRecord.setOptTime(new Date());
            cardOptRecord.setCardCoin(resultMoney);
            cardOptRecord.setMoney(new BigDecimal(walletDto.getMoney()));
            cardOptRecord.setOrderId(walletDto.getOrderId());
            cardOptRecord.setOptType(AccountBizLogOp.FREEZE_MONEY.toString());
            cardOptRecord.setClientIp(walletDto.getClientIp()== null?"":walletDto.getClientIp());
            cardOptRecord.setAdminId(walletDto.getAdminId());
            if(rows > 0){
                cardOptRecord.setSuccessFlag(1);
            }else{
                cardOptRecord.setSuccessFlag(-1);
            }

            cardOptRecordDao.insert(cardOptRecord);
            if(rows > 0){
                addToResult(resultMoney.doubleValue(),resultObj);
                return;
            }
        }

        addToResult(AccountCommon.OPT_FAILURE,resultObj);
    }

    /**
     * 获取指定订单的冻结记录 gift card
     * @param orderId
     * @param isMainDB 主从库选择
     * @return
     */
    private CardOptRecord getFreezeRecord(String orderId,boolean isMainDB) {
        CardOptRecord queryRecord = new CardOptRecord();
        queryRecord.setOrderId(orderId);
        queryRecord.setOptType(AccountBizLogOp.FREEZE_MONEY.toString());
        queryRecord.setSuccessFlag(1);
        return cardOptRecordDao.getEntityByObj(queryRecord,isMainDB);
    }
    
    /**
     * 获取指定订单的冻结记录 wallet
     * @param orderId
     * @param isMainDB 主从库选择
     * @return
     */
    private WalletOptRecord getWalletFreezeRecord(String orderId,boolean isMainDB) {
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
        CardOptRecord queryRecord = getFreezeRecord(walletDto.getOrderId(),true);
        if( queryRecord != null && NumberUtil.compareTo(queryRecord.getMoney().doubleValue(),walletDto.getMoney()) == 0){
            AccountCard queryWallet = accountCardDao.getEntityById(walletDto.getuId(),true);
            double currentFreezeAmount;
            if(queryWallet != null){
                currentFreezeAmount = queryWallet.getFreezeAmount().doubleValue();

                //2.扣减冻结款
                BigDecimal resultFreezeAmount = new BigDecimal(NumberUtil.subtract(currentFreezeAmount,walletDto.getMoney()));
                AccountCard accountWallet = new AccountCard();
                accountWallet.setUid(walletDto.getuId());
                accountWallet.setFreezeAmount(resultFreezeAmount);
                int rows =  accountCardDao.update(accountWallet);

                //3.更改操作日志
                queryRecord.setSource(PayCode.MLW_C.getDesc());
                queryRecord.setOptTime(new Date());
                queryRecord.setOptType(AccountBizLogOp.SUB_MONEY.toString());
                if(rows > 0){
                    queryRecord.setSuccessFlag(1);
                }else{
                    queryRecord.setSuccessFlag(-1);
                }
                cardOptRecordDao.update(queryRecord);

               if( rows > 0){
                   addToResult(queryWallet.getCardCoin().doubleValue(),resultObj);
                   return;
               }
            }
            addToResult(AccountCommon.OPT_FAILURE,resultObj);
            return;
        }
        addToResult(AccountCommon.PARAM_IN_VALID,resultObj);
    }


    /** 供支付调用，进行退款，礼品卡账户消费订单退款
     * gateway
     * @param resultObj
     * @param walletDto
     */
    public void updateRefundFromGateway(JsonObject resultObj,WalletDto walletDto){
        //1.获取消费记录
        String orderId = walletDto.getOrderId().split("-")[0];
        String retOrderId = walletDto.getOrderId().split("-")[1];
        boolean flag = orderId.equals(retOrderId)?true:false;

        //2.退款到美丽钱包
        AccountCard queryWallet = accountCardDao.getEntityById(walletDto.getuId(),true);
        double currentMoney;
        if( queryWallet != null ){
            currentMoney = queryWallet.getCardCoin().doubleValue();
            BigDecimal resultMoney = new BigDecimal(NumberUtil.add(currentMoney,walletDto.getMoney()));
            String optType = AccountBizLogOp.REFUND_FROM_GATEWAY.name();
            int successFlag = 1 ;
            Serializable returnStr = AccountCommon.OPT_SUCCESS ;
            
            CardOptRecord cardOptRecord = new CardOptRecord();
            //超过最大可存储金额
            if(resultMoney.doubleValue() > WalletAccountConstant.WALLET_MAX_MONEY){
            	resultMoney = BigDecimal.valueOf(currentMoney) ;
            	optType = AccountBizLogOp.TOO_LARGER.name();
            	returnStr = AccountCommon.MONEY_TOO_LARGER ;
            	logger.warn("礼品卡账户余额超过最大可存储金额", "resultMoney:"+resultMoney, null);
            }
            
            double subMoney = cardOptRecordDao.getSubMoneyByOrderId(orderId, "1");
            //该订单不存在礼品卡消费记录
            if(subMoney == 0.00){
            	resultMoney = BigDecimal.valueOf(currentMoney) ;
            	successFlag = -1;
            	logger.warn("当前订单不存在礼品卡消费记录", "orderId:"+orderId, null);
            }
            double allRefundMoney = cardOptRecordDao.getAllRefundMoneyByOrderId(orderId, "1");
            double refundMoney = (double)(Math.round(allRefundMoney*100) + Math.round(currentMoney*100))/100 ;
            
            //累计退款金额已超过该笔订单礼品卡部分所消费的金额
            if(refundMoney > subMoney){
            	resultMoney = BigDecimal.valueOf(currentMoney) ;
            	successFlag = -1;
            	logger.warn("累计退款金额已超过该笔订单礼品卡部分所消费的金额", "subMoney:"+subMoney+", allRefundMoney:"+refundMoney, null);
            }
            
            cardOptRecord.setUid(walletDto.getuId());
            cardOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
            //充值的时候为礼品卡号，消费的交易中为订单id
            cardOptRecord.setCardNum(orderId);
            cardOptRecord.setOptTime(new Date());
            cardOptRecord.setCardCoin(resultMoney);
            cardOptRecord.setMoney(new BigDecimal(walletDto.getMoney()));
            cardOptRecord.setOrderId(retOrderId);
            cardOptRecord.setSource(PayCode.MLW_W.getDesc());
            cardOptRecord.setClientIp(walletDto.getClientIp()== null?"":walletDto.getClientIp());
            cardOptRecord.setAdminId(walletDto.getAdminId());
            cardOptRecord.setOptType(optType);
            cardOptRecord.setSuccessFlag(successFlag);
            
            int rows = 0 ;
            if(resultMoney != BigDecimal.valueOf(currentMoney)){
            	AccountCard accountWallet = new AccountCard();
                accountWallet.setUid(walletDto.getuId());
                accountWallet.setCardCoin( resultMoney);
                rows =  accountCardDao.update(accountWallet);
            }
            
            if(flag){
                cardOptRecord.setOptType(AccountBizLogOp.REFUND_FROM_FREEZE.toString());
            }else{
                cardOptRecord.setOptType(AccountBizLogOp.REFUND_FROM_GATEWAY.toString());
            }
            if(rows <= 0){
                cardOptRecord.setSuccessFlag(1);
            }

            cardOptRecordDao.insert(cardOptRecord);

            if( rows > 0 && returnStr == null){
                addToResult(resultMoney.doubleValue(),resultObj);
                
            }else{
            	addToResult(returnStr,resultObj);
            	return;
            }
        }
        addToResult(AccountCommon.OPT_FAILURE,resultObj);
    }

    /**
     * 礼品卡账户消费订单冻结回退，并记录 ip 和 admin Id
     * @param resultObj
     * @param orderId
     * @param ip
     * @param adminId
     */
    public void updateRefundFromFreezeWithIp(JsonObject resultObj,String orderId,String ip,Integer adminId){
        //1.校验记录是否符合冻结回退条件
        CardOptRecord queryRecord = getFreezeRecord(orderId,true);
        WalletOptRecord queryWalletRecord = getWalletFreezeRecord(orderId,true);
        
        if(queryRecord == null && queryWalletRecord == null){
            addToResult(AccountCommon.RECORD_NOT_EXIT,resultObj);
            return;
        }

        BigDecimal resultMoney = new BigDecimal(0) ;
        if(queryWalletRecord != null){
	        AccountWallet accountWallet = accountWalletDao.getEntityById(queryWalletRecord.getUid(),true);
	        if( accountWallet != null){
	            double  currentMoney = accountWallet.getMlwCoin().doubleValue();
	
	            BigDecimal resultMoneyW = new BigDecimal(NumberUtil.add(currentMoney,queryWalletRecord.getMoney().doubleValue()));
	
	            int rows = accountWalletDao.unFreezeMoney(queryWalletRecord.getUid(),queryWalletRecord.getMoney().doubleValue());
	            //4.记录操作日志
	            WalletOptRecord walletOptRecord = new WalletOptRecord();
	            walletOptRecord.setUid(queryWalletRecord.getUid());
	            walletOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
	            walletOptRecord.setOptTime(new Date());
	            walletOptRecord.setMlwCoin(resultMoneyW);
	            walletOptRecord.setMoney(queryWalletRecord.getMoney());
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
	            	resultMoney = resultMoney.add(resultMoneyW) ;
//	                addToResult(resultMoney.doubleValue(),resultObj);
//	                return;
	            }
	
	        }
        }
        
        if(queryRecord != null){
	        AccountCard accountCard = accountCardDao.getEntityById(queryRecord.getUid(),true);
	        if( accountCard != null){
	            double  currentMoney = accountCard.getCardCoin().doubleValue();
	
	            BigDecimal resultMoneyC = new BigDecimal(NumberUtil.add(currentMoney,queryRecord.getMoney().doubleValue()));
	
	            int rows = accountCardDao.unFreezeMoney(queryRecord.getUid(),queryRecord.getMoney().doubleValue());
	            //4.记录操作日志
	            CardOptRecord cardOptRecord = new CardOptRecord();
	            cardOptRecord.setUid(queryRecord.getUid());
	            cardOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
	            cardOptRecord.setOptTime(new Date());
	            cardOptRecord.setCardCoin(resultMoneyC);
	            cardOptRecord.setMoney(queryRecord.getMoney());
	            cardOptRecord.setOrderId(orderId);
	            cardOptRecord.setOptType(AccountBizLogOp.REFUND_FROM_FREEZE.toString());
	            cardOptRecord.setSource(PayCode.MLW_C.getDesc());
	            cardOptRecord.setClientIp(ip);
	            cardOptRecord.setAdminId(adminId);
	            if(rows > 0){
	                cardOptRecord.setSuccessFlag(1);
	            }else{
	                cardOptRecord.setSuccessFlag(-1);
	            }
	
	            cardOptRecordDao.insert(cardOptRecord);
	            if(rows > 0){
	            	resultMoney = resultMoney.add(resultMoneyC) ;
//	                addToResult(resultMoney.doubleValue(),resultObj);
//	                return;
	            }
	
	        }
        }
        
        if(resultMoney.doubleValue() > 0){
        	addToResult(resultMoney.doubleValue(),resultObj);
        }else{
        	addToResult(AccountCommon.OPT_FAILURE,resultObj);
        }

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
        if(errorCount >= 3){
            addToResult(flag,resultObj);
            return;
        }
        
        AccountCard accountCard = accountCardDao.getEntityById(uId);
        AccountWallet wall = accountWalletDao.getEntityById(uId,true);
        if(accountCard != null && MlwEncryptTools.encryptAccountPwd(payPassword).equals(wall.getPayPasswd())){
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
       if(errorCount >= 3){
           addToResult(1,resultObj);
       }else{
           addToResult(0,resultObj);
       }
   }

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
      if(errorCount >= 3){
          addToResult(AccountCommon.STATE_FREEZE,resultObj);
          return;
      }

      //钱包密码错误
      AccountCard accountCard = accountCardDao.getEntityById(uId);
      AccountWallet wall = accountWalletDao.getEntityById(uId,true);
      if(accountCard != null && StringUtils.isNotEmpty(wall.getPayPasswd()) && !MlwEncryptTools.encryptAccountPwd(paypwd).equals(wall.getPayPasswd())){
         addToResult(AccountCommon.PASSWORD_ERROR,resultObj);
         return;
      }

      //返回余额
      addToResult(accountCard.getCardCoin(),resultObj);
   }

    /**
     * 分页查询记录
     * @param jsonObject
     * @param pageInfo
     * @param accountWallet
     * @param whereSql
     */
    public void getRecordByPage(JsonObject jsonObject,PageInfo pageInfo,AccountCard accountWallet,String whereSql){
        pageInfo.setOrderDirection("desc");
        pageInfo.setOrderField("opt_time");

        PagerControl<AccountCard> pc=accountCardDao.getPagerByObj(accountWallet,pageInfo,whereSql);
        JSONTool.addToResult(pc, jsonObject);
    }
    
    /**
     * 分页查询记录
     * @param jsonObject
     * @param pageInfo
     * @param cardOptRecord
     * @param whereSql
     */
    public void getOptRecordByPage(JsonObject jsonObject,PageInfo pageInfo,CardOptRecord cardOptRecord,String whereSql){
        pageInfo.setOrderDirection("desc");
        pageInfo.setOrderField("opt_time");

        PagerControl<CardOptRecord> pc=cardOptRecordDao.getPagerByObj(cardOptRecord,pageInfo,whereSql);
        JSONTool.addToResult(pc,jsonObject);
    }
    
    /**
     * 组合退款方法 
     * @param resultObj
     * @param list        多个WalletDto对象
     * @return 
     *     退款成功则返回 AccountCommon.OPT_SUCCESS
     *     否则         AccountCommon.异常
     */
    public void updateRefundForMore(JsonObject resultObj,WalletDto[] wds){
    	if(wds == null || wds.length <= 0){
    		logger.warn("组合退款对象为空", "List<WalletDto>:null", null);
    		addToResult(AccountCommon.OPT_FAILURE,resultObj);
    		return ;
    	}
    	
    	// 如果某种支付方式执行失败，则跳出循环
    	int sucessRows = 0 ;
    	for(int i=0; i<wds.length; i++){
    		WalletDto walletDto = wds[i];
    		if(walletDto == null){
    			logger.warn("有为空的退款入口", "PayCode:null", null);
    			addToResult(AccountCommon.OPT_FAILURE,resultObj);
    			return;
    		}
            //1.获取消费记录
            String orderId = walletDto.getOrderId().split("-")[0];
            String retOrderId = walletDto.getOrderId().split("-")[1];
            boolean flag = orderId.equals(retOrderId)?true:false;

    		//钱包退款
    		if(walletDto != null && walletDto.getPayCode().equals(PayCode.MLW_W)){

    	        //2.退款到美丽钱包
                AccountWallet queryWallet = accountWalletDao.getEntityById(walletDto.getuId(),true);
                if(queryWallet == null){
                	logger.warn("美丽钱包不存在", "uid:"+walletDto.getuId(), null);
                	addToResult(AccountCommon.OPT_FAILURE,resultObj);
        	        return ;
                }
                double currentMoney;
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
                	sucessRows++ ;
                }
    			
    		
            //礼品卡退款
    		}else if(walletDto != null && walletDto.getPayCode().equals(PayCode.MLW_C)){

    	        //2.退款到礼品卡账户
    	        AccountCard queryWallet = accountCardDao.getEntityById(walletDto.getuId(),true);
    	        if(queryWallet == null){
    	        	logger.warn("礼品卡账户不存在", "uid:"+walletDto.getuId(), null);
    	        	addToResult(AccountCommon.OPT_FAILURE,resultObj);
        	        return ;
    	        }
    	        
    	        double currentMoney;
	            currentMoney = queryWallet.getCardCoin().doubleValue();
	            BigDecimal resultMoney = new BigDecimal(NumberUtil.add(currentMoney,walletDto.getMoney()));
	            String optType = AccountBizLogOp.REFUND_FROM_GATEWAY.name();
	            int successFlag = 1 ;
	            Serializable returnStr = null ;
	            
	            CardOptRecord cardOptRecord = new CardOptRecord();
	            //超过最大可存储金额
	            if(resultMoney.doubleValue() > WalletAccountConstant.WALLET_MAX_MONEY){
	            	resultMoney = BigDecimal.valueOf(currentMoney) ;
	            	optType = AccountBizLogOp.TOO_LARGER.name();
	            	returnStr = AccountCommon.MONEY_TOO_LARGER ;
	            	logger.warn("礼品卡账户余额超过最大可存储金额", "resultMoney:"+resultMoney, null);
	            }
	            
	            double subMoney = cardOptRecordDao.getSubMoneyByOrderId(orderId, "1");
	            //该订单不存在礼品卡消费记录
	            if(subMoney == 0.00){
	            	resultMoney = BigDecimal.valueOf(currentMoney) ;
	            	successFlag = -1;
	            	logger.warn("当前订单不存在礼品卡消费记录", "orderId:"+orderId, null);
	            }
	            double allRefundMoney = cardOptRecordDao.getAllRefundMoneyByOrderId(orderId, "1");
	            double refundMoney = (double)(Math.round(allRefundMoney*100) + Math.round(walletDto.getMoney()*100))/100 ;
	            
	            //累计退款金额已超过该笔订单礼品卡部分所消费的金额
	            if(refundMoney > subMoney){
	            	resultMoney = BigDecimal.valueOf(currentMoney) ;
	            	successFlag = -1;
	            	logger.warn("累计退款金额已超过该笔订单礼品卡部分所消费的金额", "subMoney:"+subMoney+", allRefundMoney:"+refundMoney, null);
	            }
	            
	            cardOptRecord.setUid(walletDto.getuId());
	            cardOptRecord.setInnerNum(WalletUtill.createWalletInnerNum());
	            //充值的时候为礼品卡号，消费的交易中为订单id
	            cardOptRecord.setCardNum(orderId);
	            cardOptRecord.setOptTime(new Date());
	            cardOptRecord.setCardCoin(BigDecimal.valueOf(currentMoney));
	            cardOptRecord.setMoney(new BigDecimal(walletDto.getMoney()));
	            cardOptRecord.setOrderId(retOrderId);
	            cardOptRecord.setSource(PayCode.MLW_C.getDesc());
	            cardOptRecord.setClientIp(walletDto.getClientIp()== null?"":walletDto.getClientIp());
	            cardOptRecord.setAdminId(walletDto.getAdminId());
	            cardOptRecord.setOptType(optType);
	            cardOptRecord.setSuccessFlag(successFlag);
	            
	            int rows = 0 ;
	            if(resultMoney != BigDecimal.valueOf(currentMoney)){
	            	AccountCard accountWallet = new AccountCard();
	                accountWallet.setUid(walletDto.getuId());
	                accountWallet.setCardCoin( resultMoney);
	                rows =  accountCardDao.update(accountWallet);
	            }
	            
	            if(flag){
	                cardOptRecord.setOptType(AccountBizLogOp.REFUND_FROM_FREEZE.toString());
	            }else{
	                cardOptRecord.setOptType(AccountBizLogOp.REFUND_FROM_GATEWAY.toString());
	            }
	            if(rows <= 0){
	                cardOptRecord.setSuccessFlag(-1);
	            }

	            cardOptRecordDao.insert(cardOptRecord);

	            if( rows > 0 && returnStr == null){
	            	sucessRows++ ;
	                
	            }else{
	            	addToResult(returnStr,resultObj);
	            	return ;
	            }
    	        
    			
    		//存在异常的退款入口
    		}else{
    			logger.warn("有不存在的退款入口", "PayCode:"+walletDto.getPayCode().name(), null);
    			addToResult(AccountCommon.OPT_FAILURE,resultObj);
    			return;
    		}
    	}

        if(sucessRows > 0){
            addToResult(AccountCommon.OPT_SUCCESS,resultObj);
        }else{
            addToResult(AccountCommon.OPT_FAILURE,resultObj);
        }
        return;
    }
    
    /**
     * 检查用户一天内是否已经激活满20次
     * @param resultObj
     * @param uid
     */
    public void isCardAddMax(JsonObject resultObj, Integer uid){
    	//今天激活超过20次
        Date beginDate = DateUtil.parseDate(DateUtil.getDateStr(new Date()));
        int addCounts = cardOptRecordDao.getTodayAddCounts(uid, beginDate);
        if(addCounts >= 20){
        	addToResult(-20,resultObj);
            return ;
        }else{
        	addToResult(20 - addCounts,resultObj);
            return ;
        }
        
        
    }

}
