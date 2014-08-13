 	package com.meiliwan.emall.account.client;


    import static com.meiliwan.emall.icetool.IceClientTool.ACCOUNT_ICE_SERVICE;

    import com.meiliwan.emall.account.bo.RechargeType;
    import org.apache.commons.lang.StringUtils;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.account.bean.AccountWallet;
import com.meiliwan.emall.account.bean.WalletDto;
import com.meiliwan.emall.account.bean.WalletOptRecord;
import com.meiliwan.emall.account.bo.AccountCommon;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.exception.IceServiceRuntimeException;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

public class AccountWalletClient {
	
	private static final String SERVICE_NAME = "accountWalletService/";

	/**
	 * 获取用户钱包
	 * @param uid 用户ID
	 * @return
	 */
	public static AccountWallet getAccountWalletByUid(Integer uid){
		String iceFuncName="getAccountWalletByUid";
		JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName, uid));
		return new Gson().fromJson(obj.get("resultObj"), new TypeToken<AccountWallet>(){}.getType());
	}
	/**
	 * 增加一个用户钱包
	 * @param wall
	 * @return
	 */
	public static int add(AccountWallet wall){
		String iceFuncName="add";
		JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName, wall));
		return new Gson().fromJson(obj.get("resultObj"), Integer.class);
	}
	/**
	 * 更新用户钱包
	 * @param wall
	 * @return
	 */
	public static int update(AccountWallet wall){
		String iceFuncName="update";
		JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName, wall));
		return new Gson().fromJson(obj.get("resultObj"), Integer.class);
	}


    /**
     * @deprecated 使用addMoneyWithIp 替代
     * 充值
     * @param uid
     * @param money
     * @param innerNum   内部流水号，唯一，由pay调用方生成
     * @param payCode  充值渠道
     * @Param outNum
     * @return 失败返回  AccountCommon 中定义的错误码  成功返回钱包金额
     */
    @Deprecated
	public static double addMoney(Integer uid,double money,String innerNum,PayCode payCode,String outNum){
        try{
            if( uid== null || uid <= 0 || money < 0 ||StringUtils.isEmpty(innerNum) || payCode==null ) return Double.valueOf(AccountCommon.PARAM_IN_VALID);
            String iceFuncName="addMoney";
            JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName, uid,money,innerNum,payCode,outNum));
            return new Gson().fromJson(obj.get("resultObj"), Double.class);
        }catch (IceServiceRuntimeException e){
            return AccountCommon.OPT_FAILURE;
        }
	}

    /**
     * 消费
     * @param uid
     * @param money
     * @param password
     * @param innerNum   内部流水号，唯一，由pay调用方生成
     * @return 失败返回  AccountCommon 中定义的错误码  成功返回钱包金额
     */
	public static double subMoney(String orderId, Integer uid,double money,String password,String innerNum){
        try{
            if(uid == null || uid <= 0 || money < 0 || StringUtils.isEmpty(innerNum))return Double.valueOf(AccountCommon.PARAM_IN_VALID);
            String iceFuncName="subMoney";
            JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName, orderId, uid,money,password,innerNum));
            return new Gson().fromJson(obj.get("resultObj"), Double.class);
        }catch (IceServiceRuntimeException e){
            return AccountCommon.OPT_FAILURE;
        }
	}

    /**
     * 目前未使用
     * 对业务日志的回滚,其实就是退款,只回滚消费记录，使用场景 消费失败的情况下才使用
     * @param uid
     * @param subInnerNum 需要回退的记录的内部流水号
     * @param innerNum    回退操作的内部流水号
     * @param money
     * @param password
     * @return 失败返回  AccountCommon 中定义的错误码  成功返回钱包金额
     */
    public static double updateRollback(Integer uid,String subInnerNum,String innerNum,double money,String password){
        try{
            if(uid == null || uid <= 0 || StringUtils.isEmpty(subInnerNum) || money <0 )return Double.valueOf(AccountCommon.PARAM_IN_VALID);
            String iceFuncName="updateRollback";
            JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName, uid,subInnerNum,innerNum,money,password));
            return new Gson().fromJson(obj.get("resultObj"), Double.class);
        }catch (IceServiceRuntimeException e){
            return AccountCommon.OPT_FAILURE;
        }
    }

    /**
     * 针对订单退款到钱包的场景
     * @param uid
     * @param innerNum
     * @param money
     * @param payCode 目前只需要设置为 PayCode.MLW_ACCOUNT_WALLET
     * @return 失败返回  AccountCommon 中定义的错误码  成功返回钱包金额
     */
    public static double updateRefund(Integer uid,String innerNum,double money,PayCode payCode){
        try{
            if(uid == null  || uid <= 0 || StringUtils.isEmpty(innerNum) || money <0 || payCode==null)return Double.valueOf(AccountCommon.PARAM_IN_VALID);
            String iceFuncName="updateRefund";
            JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName, uid,innerNum,money,payCode));
            return new Gson().fromJson(obj.get("resultObj"), Double.class);
        }catch (IceServiceRuntimeException e){
           return AccountCommon.OPT_FAILURE;
        }
    }


    public static int  addOptRecord(WalletOptRecord walletOptRecord){
        try{
             if(walletOptRecord == null || walletOptRecord.getUid()==null || walletOptRecord.getMoney().doubleValue()<0 || StringUtils.isEmpty(walletOptRecord.getInnerNum()))
                 return AccountCommon.PARAM_IN_VALID;
            String iceFuncName="addOptRecord";
            JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName,walletOptRecord));
            return obj.get("resultObj").getAsInt();
        }catch (IceServiceRuntimeException e){
            return AccountCommon.OPT_FAILURE;
        }
    }

   /**************************************** 增加冻结概念后的更改 ************************************************/

    /**
     * 冻结
     * @param walletDto ,必填字段为 orderId ,userId,冻结金额money
     * @return
     */
   public static double updateFreezeMoney(WalletDto walletDto){
      try{

          if(walletDto == null || StringUtils.isEmpty(walletDto.getOrderId()) || walletDto.getMoney() < 0 || walletDto.getuId() <0){
              return Double.valueOf(AccountCommon.PARAM_IN_VALID);
          }
          String iceFuncName="updateFreezeMoney";
          JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName,walletDto));
          return new Gson().fromJson(obj.get("resultObj"), Double.class);
      }catch (IceServiceRuntimeException e){
          return AccountCommon.OPT_FAILURE;
      }
   }


    /**
     * @deprecated 请使用updateRefundFromFreezeWithIp
     * 冻结回退
     * @param orderId
     * @return
     */
    @Deprecated
  public static double updateRefundFromFreeze(String orderId){
      try{

          if(Strings.isNullOrEmpty(orderId)){
              return Double.valueOf(AccountCommon.PARAM_IN_VALID);
          }
          String iceFuncName="updateRefundFromFreeze";
          JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName,orderId));
          return new Gson().fromJson(obj.get("resultObj"), Double.class);
      }catch (IceServiceRuntimeException e){
          return AccountCommon.OPT_FAILURE;
      }
  }

    /**
     * 消费
     * @param walletDto 必须填写的字段 orderId,userId,money
     *                  假如是alipay 支付的话，还要设置 payAccount
     * @return
     */
  public static double payMoney(WalletDto walletDto){
      try{

          if(walletDto == null || StringUtils.isEmpty(walletDto.getOrderId()) || walletDto.getMoney() < 0 || walletDto.getuId() < 0 ){
              return Double.valueOf(AccountCommon.PARAM_IN_VALID);
          }
          String iceFuncName="payMoney";
          JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName,walletDto));
          return new Gson().fromJson(obj.get("resultObj"), Double.class);
      }catch (IceServiceRuntimeException e){
          return AccountCommon.OPT_FAILURE;
      }
  }

    /**
     * 退款，被gateway 调用
     * @param walletDto  必须填写的字段 orderId,userId,money
     *                   orderId 是一个字符串，构成规则是  订单id-退换货id
     * @return
     */
  public static double updateRefundFromGateway(WalletDto walletDto){
      try{

          if(walletDto == null || StringUtils.isEmpty(walletDto.getOrderId()) || walletDto.getOrderId().split("-").length != 2 || walletDto.getMoney() < 0 || walletDto.getuId() < 0 ){
              return Double.valueOf(AccountCommon.PARAM_IN_VALID);
          }
          String iceFuncName="updateRefundFromGateway";
          JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName,walletDto));
          return new Gson().fromJson(obj.get("resultObj"), Double.class);
      }catch (IceServiceRuntimeException e){
          return AccountCommon.OPT_FAILURE;
      }
  }

    /**
     * @deprecated 该方法以及过期，请使用 AccountWalletClient.validatePwdAndGetMoney
     * @param uid
     * @param payPassword
     * @return
     */
    @Deprecated
     public static boolean isPayPasswordRight(Integer uid,String payPassword){
         try{
             if(uid > 0 && ! StringUtils.isEmpty(payPassword)){
                 String iceFuncName="isPayPasswordRight";
                 JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName,uid,payPassword));
                 return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
             }else{
                 return false;
             }
         }catch(IceServiceRuntimeException e){
            return  false;
         }
    }

    /**
     * 判断用户钱包是否被冻结
     * 1 冻结  0 未冻结
     * @param uid
     * @return
     */
    public static int isPayPwdFreeze(Integer uid){
        try{
            if(uid > 0){
                String iceFuncName="isPayPwdFreeze";
                JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName,uid));
                return new Gson().fromJson(obj.get("resultObj"), Integer.class);
            }else{
               return 0;
            }
        }catch(IceServiceRuntimeException e){
            return 0;
        }
    }

   /**************************** v 5.12 优化***********************/
    /**
     * 描述：根据用户id 和 字符密码 进行支付密码的校验和钱包金额的获取
     * 返回：支付密码错误，返回支付密码错误状态；支付密码冻结返回支付密码冻结状态；支付密码正确，返回钱包金额；所有的状态信息@see AccountCommon
     * @param paypwd
     * @param uId
     */
    public static double validatePwdAndGetMoney(String paypwd,Integer uId){
        String iceFuncName="validatePwdAndGetMoney";
        JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName,paypwd,uId));
        return new Gson().fromJson(obj.get("resultObj"), Double.class);
    }


    /**
     * 充值
     * @param uid
     * @param money
     * @param innerNum   内部流水号，唯一，由pay调用方生成
     * @param payCode  充值渠道
     * @Param outNum
     * @param ip 客户端ip
     * @return 失败返回  AccountCommon 中定义的错误码  成功返回钱包金额
     */
    public static double addMoneyWithIp(Integer uid,double money,String innerNum,PayCode payCode,String outNum,String ip){
        try{
            if( uid== null || uid <= 0 || money < 0 ||StringUtils.isEmpty(innerNum) || payCode==null ) return Double.valueOf(AccountCommon.PARAM_IN_VALID);
            String iceFuncName="addMoneyWithIp";
            JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName, uid,money,innerNum,payCode,outNum,ip));
            return new Gson().fromJson(obj.get("resultObj"), Double.class);
        }catch (IceServiceRuntimeException e){
            return AccountCommon.OPT_FAILURE;
        }
    }


    /**
     * 冻结回退
     * @param orderId
     * @param ip
     * @param uid
     * @return
     */
    public static double updateRefundFromFreezeWithIp(String orderId,String ip ,Integer uid){
        try{

            if(Strings.isNullOrEmpty(orderId)){
                return Double.valueOf(AccountCommon.PARAM_IN_VALID);
            }
            String iceFuncName="updateRefundFromFreezeWithIp";
            JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName,orderId,ip,uid));
            return new Gson().fromJson(obj.get("resultObj"), Double.class);
        }catch (IceServiceRuntimeException e){
            return AccountCommon.OPT_FAILURE;
        }
    }


    /**
     * 获取分页记录
     * @param pageInfo
     * @param accountWallet
     * @param whereSql
     * @return
     */
    public static PagerControl<AccountWallet> getRecordByPage(PageInfo pageInfo,AccountWallet accountWallet,String whereSql){
        String iceFuncName="getRecordByPage";
        JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + iceFuncName,pageInfo,accountWallet,whereSql));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<AccountWallet>>(){}.getType());
    }


    /**
     * 充值
     * @param uid
     * @param money
     * @param ip 客户端ip
     * @param rechargeType 充值类型
     * @return 失败返回  AccountCommon 中定义的错误码  成功返回钱包金额
     */
    public static double addMoney4Type(Integer uid,double money,String ip,RechargeType rechargeType){
        try{
            if( uid== null || uid <= 0 || money < 0 ) return Double.valueOf(AccountCommon.PARAM_IN_VALID);
            String iceFuncName="addMoney4Type";
            JsonObject obj = IceClientTool.sendMsg(ACCOUNT_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+iceFuncName, uid,money,ip,rechargeType));
            return new Gson().fromJson(obj.get("resultObj"), Double.class);
        }catch (IceServiceRuntimeException e){
            return AccountCommon.OPT_FAILURE;
        }
    }

}
