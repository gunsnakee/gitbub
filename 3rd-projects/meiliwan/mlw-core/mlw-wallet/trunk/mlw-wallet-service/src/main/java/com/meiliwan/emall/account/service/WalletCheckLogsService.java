package com.meiliwan.emall.account.service;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.account.bean.AccountWallet;
import com.meiliwan.emall.account.bean.WalletCheckLogs;
import com.meiliwan.emall.account.dao.AccountWalletDao;
import com.meiliwan.emall.account.dao.WalletCheckLogsDao;
import com.meiliwan.emall.account.dao.WalletOptRecordDao;
import com.meiliwan.emall.base.client.BaseMailAndMobileClient;
import com.meiliwan.emall.base.dto.MailEntity;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.plugin.zk.ZKClient;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-11-15
 * Time: 下午3:48
 * To change this template use File | Settings | File Templates.
 */
@Service
public class WalletCheckLogsService extends DefaultBaseServiceImpl{

    @Autowired
    private WalletCheckLogsDao walletCheckLogsDao;
    @Autowired 
    private WalletOptRecordDao walletOptRecordDao;
    @Autowired 
    private AccountWalletDao accountWalletDao;

    private final static MLWLogger logger = MLWLoggerFactory.getLogger(WalletCheckLogsService.class);
    
    /**
     * 插入一条校验数据，无事务控制
     * @param jsonObject
     * @param walletCheckLogs
     */
    public void checkAddCheckLog(JsonObject jsonObject,WalletCheckLogs walletCheckLogs){
      int result =   walletCheckLogsDao.insert(walletCheckLogs);
        JSONTool.addToResult(result,jsonObject);
    }


    /**
     * 根据uid 查询记录
     * @param jsonObject
     * @param uid
     */
    public void getCheckLogByUid(JsonObject jsonObject,Integer uid){
        WalletCheckLogs walletCheckLogs = walletCheckLogsDao.getEntityById(uid);
        JSONTool.addToResult(walletCheckLogs,jsonObject);
    }

    /**
     * 根据条件进行查询记录
     * @param jsonObject
     * @param walletCheckLogs
     * @param time    更新时间
     */
    public void getCheckLogByCondition(JsonObject jsonObject,WalletCheckLogs walletCheckLogs,String time){
            List<WalletCheckLogs> list = null;
            String whereSql = null;
            if(!StringUtils.isEmpty(time)){
               whereSql =new StringBuilder().append(" update_time <='").append(time).append("' order by update_time desc").toString();
            }

           list =walletCheckLogsDao.getListByObj(walletCheckLogs,whereSql);
           JSONTool.addToResult(list,jsonObject);
    }

    /**
     * 根据uid 跟新记录
     * @param jsonObject
     * @param walletCheckLogs
     */
    public void checkUpdate(JsonObject jsonObject,WalletCheckLogs walletCheckLogs){
        int result = walletCheckLogsDao.update(walletCheckLogs);
        JSONTool.addToResult(result,jsonObject);
    }
    
    
    /**
     * 钱包自动检测资金异常
     * 1.检查今日的钱包消费情况，如果有交易情况变动的则取到对应变动的用户id列表；
     * 2.通过用户id去查询对应用户之前是否有对应的校验记录，如果有则取到余额和最后的校验时间；
     * 3.通过用户id和最后的校验时间去查询这个用户自最后校验之后产生的消费记录；
     * 4.对比交易记录中钱包进账和出账的情况是否对得上现在的余额情况；
     * 5.如果满足则继续校验，如果对不上则进行邮件通知有关人员。
     */
    public void autoWalletCheck(){
    	//时间区间：昨日零点到今日零点
    	String startCheckTime = DateUtil.getYestoday() + " 00:00:00";
//    	String startCheckTime = "1997-01-01 00:00:00";
    	String endCheckTime = DateUtil.getDateStr(new Date()) + " 00:00:00";
    	//通过时间段获取今日有交易记录的用户id列表
    	List<Integer> uidList = null;
    	uidList = walletOptRecordDao.getUidsByTime(DateUtil.parseDateTime(startCheckTime), DateUtil.parseDateTime(endCheckTime));
    	if(uidList != null || uidList.size()>0){
    		//邮件内容
    		StringBuffer mailStr = new StringBuffer() ;
    		for(int i=0;i<uidList.size(); i++){
    			int uid = uidList.get(i);
    			//通过用户id去查询是否有历史校验记录
    			WalletCheckLogs walletCheckLogs = walletCheckLogsDao.getEntityById(uid);
    			if(walletCheckLogs != null){
    				//上次校验后记录的余额
    				double endMlwCoin = walletCheckLogs.getEndMlwCoin().doubleValue();
    				String endTime = DateUtil.getDateTimeStr(walletCheckLogs.getEndTime());
    				
    				//通过用户id和最后校验时间去钱包消费记录中分别获取进账总值和出账总值 必带条件：success_flag = 1
    				double inCoin = 0.00 ;
    				double outCoin = 0.00 ;
    				inCoin = walletOptRecordDao.incomeTotalAmount(uid, DateUtil.parseDateTime(endTime), DateUtil.parseDateTime(endCheckTime)) ;
    				outCoin = walletOptRecordDao.paymentTotalAmount(uid, DateUtil.parseDateTime(endTime), DateUtil.parseDateTime(endCheckTime)) ;
    				
    				//计算：当前余额 = 上次校验记录余额 + 入账金额 - 出账金额
    				double nowCoin = endMlwCoin + inCoin - outCoin ;
    				
    				//通过用户id获取当前用户钱包 获取数据库中钱包实际余额
    				AccountWallet accountWallet = accountWalletDao.getEntityById(uid);
    				if(accountWallet != null){
    					double nowRellCoin = accountWallet.getMlwCoin().doubleValue();
    					//校验金额是否一致：收支情况计算余额 ？= 实际余额
    					boolean isCheckOk = true ;
    					StringBuffer checkInfo = new StringBuffer() ;
    					if(Math.round(nowCoin*100) != Math.round(nowRellCoin*100)){
    						isCheckOk = false ;
    						logger.info("用户美丽钱包余额有出入", "用户id:"+uid+",正确余额:"+nowCoin+",实际余额:"+nowRellCoin, null);
    						checkInfo.append("用户美丽钱包余额有出入[").append("用户id:").append(uid).append(",正确余额:").append(nowCoin).append(",实际余额:").append(nowRellCoin).append("]");
    						//记录异常数据
    						mailStr.append(checkInfo.toString()).append("<br/>");
    					}
    					checkInfo.append(" 校验结果：").append(isCheckOk);
    					//更新校验记录
    					walletCheckLogs.setCheckInfo(checkInfo.toString());
    					walletCheckLogs.setCheckStatus(isCheckOk ? 1 : -1);
    					walletCheckLogs.setEndMlwCoin(BigDecimal.valueOf(nowCoin));
    					walletCheckLogs.setStartTime(DateUtil.parseDateTime(endTime));
    					walletCheckLogs.setEndTime(DateUtil.parseDateTime(endCheckTime));
    					walletCheckLogsDao.update(walletCheckLogs);
    					
    				}else{
    					logger.info("用户钱包不存在！", "action:accountWalletDao.getEntityById,uid:"+uid, null);
    				}
    				
    			//如果不存在校验历史则需要全部校验
    			}else{
    				walletCheckLogs = new WalletCheckLogs() ;
    				//通过用户id和最后校验时间去钱包消费记录中分别获取进账总值和出账总值 必带条件：success_flag = 1
    				double inCoin = 0.00 ;
    				double outCoin = 0.00 ;
    				inCoin = walletOptRecordDao.incomeTotalAmount(uid, DateUtil.parseDateTime("1997-01-01 00:00:00"), DateUtil.parseDateTime(endCheckTime)) ;
    				outCoin = walletOptRecordDao.paymentTotalAmount(uid, DateUtil.parseDateTime("1997-01-01 00:00:00"), DateUtil.parseDateTime(endCheckTime)) ;
    				
    				//计算：当前余额 = 上次校验记录余额 + 入账金额 - 出账金额
    				double nowCoin = (double)(Math.round(inCoin*100) - Math.round(outCoin*100))/100 ;
    				
    				//通过用户id获取当前用户钱包 获取数据库中钱包实际余额
    				AccountWallet accountWallet = accountWalletDao.getEntityById(uid);
    				if(accountWallet != null){
    					double nowRellCoin = accountWallet.getMlwCoin().doubleValue();
    					//校验金额是否一致：收支情况计算余额 ？= 实际余额
    					boolean isCheckOk = true ;
    					StringBuffer checkInfo = new StringBuffer() ;
    					if(Math.round(nowCoin*100) != Math.round(nowRellCoin*100)){
    						isCheckOk = false ;
    						logger.info("用户美丽钱包余额有出入", "用户id:"+uid+",正确余额:"+nowCoin+",实际余额:"+nowRellCoin, null);
    						checkInfo.append("用户美丽钱包余额有出入[").append("用户id:").append(uid).append(",正确余额:").append(nowCoin).append(",实际余额:").append(nowRellCoin).append("]");
    						//记录异常数据
    						mailStr.append(checkInfo.toString()).append("<br/>");
    					}
    					checkInfo.append(" 校验结果：").append(isCheckOk);
    					//更新校验记录
    					walletCheckLogs.setUid(uid);
    					walletCheckLogs.setCheckInfo(checkInfo.toString());
    					walletCheckLogs.setCheckStatus(isCheckOk ? 1 : -1);
    					walletCheckLogs.setEndMlwCoin(BigDecimal.valueOf(nowCoin));
    					walletCheckLogs.setStartTime(DateUtil.parseDateTime("1997-01-01 00:00:00"));
    					walletCheckLogs.setEndTime(DateUtil.parseDateTime(endCheckTime));
    					walletCheckLogsDao.insert(walletCheckLogs);
    					
    				}else{
    					logger.info("用户钱包不存在！", "action:accountWalletDao.getEntityById,uid:"+uid, null);
    				}
    			}
    		}
    		
    		//校验完毕后发送邮件
    		StringBuffer bodyStr = new StringBuffer() ;
    		if(mailStr != null && mailStr.length()>0){
    			bodyStr.append("本次校验结果：存在异常用户，请及时处理！<br/>");
    		}else{
    			bodyStr.append("本次校验结果：所有记录校验通过，钱包交易正常！<br/>");
    		}
    		bodyStr.append("校验起始时间：").append(startCheckTime).append("<br/>");
    		bodyStr.append("校验终止时间：").append(endCheckTime).append("<br/>");
    		bodyStr.append("异常用户记录：<br/>").append(mailStr != null && mailStr.length()>0 ? mailStr.toString() : "无");
    		try{
//    			String isSend = ConfigOnZk.getInstance().getValue("zhuanglong-service/system.properties", "mail.send").trim();
//    			String mailTo  = ConfigOnZk.getInstance().getValue("zhuanglong-service/system.properties", "mail.walletcheck.receive.user").trim();
    			byte[] values = ZKClient.get().getData("/mlwconf/account-service/system.properties");
    			ByteArrayInputStream stream = new ByteArrayInputStream(values);
    			java.util.Properties properties = new java.util.Properties();
    			properties.load(stream);
    			String serverEnvironment = properties.getProperty("mail.walletcheck.serverEnvironment").trim();
    			String isSend = properties.getProperty("mail.send").trim();
    			String mailTo  = properties.getProperty("mail.walletcheck.receive.user").trim();
    			logger.info("钱包交易余额异常验证邮件发送信息", "isSend:"+isSend+", mailTo:"+mailTo, null);
    			if(!StringUtils.isBlank(isSend) && !StringUtils.isBlank(mailTo) && "true".equals(isSend)){
//    				MailUtils.sendMail("admin@meiliwan.cn", mailTo, "美丽湾钱包消费检验情况["+serverEnvironment+"]", bodyStr.toString());
    				MailEntity mailEntity = new MailEntity() ;
    				mailEntity.setReceivers(mailTo);
    				mailEntity.setTitle("美丽湾钱包消费检验情况["+serverEnvironment+"]");
    				mailEntity.setContent(bodyStr.toString());
    				try{
    					BaseMailAndMobileClient.sendMail(mailEntity);
    					logger.info("美丽湾钱包消费检验情况反馈邮件发送成功!", "邮件信息:"+mailEntity.toString(), null);
    				}catch (Exception e) {
						logger.error(e, "邮件发送异常:"+mailEntity.toString(), null);
					}
    				
    			}
    			
    		} catch (Exception e) {
                logger.error(e, "[ZKClient.get().getData:zhuanglong-service/system.properties]", null);
            }
    		
    	}else{
    		logger.info("今日无钱包交易记录", "开始时间节点:"+startCheckTime+",结束时间节点:"+endCheckTime, null);
    	}
    	
    }
    
    /**
     * 钱包自动检测资金异常
     * 1.检查今日的钱包消费情况，如果有交易情况变动的则取到对应变动的用户id列表；
     * 2.通过用户id去查询对应用户之前是否有对应的校验记录，如果有则取到余额和最后的校验时间；
     * 3.通过用户id和最后的校验时间去查询这个用户自最后校验之后产生的消费记录；
     * 4.对比交易记录中钱包进账和出账的情况是否对得上现在的余额情况；
     * 5.如果满足则继续校验，如果对不上则进行邮件通知有关人员。
     */
    public void autoWalletCheckJob(JsonObject jsonObject){
    	//时间区间：昨日零点到今日零点
    	String startCheckTime = DateUtil.getYestoday() + " 00:00:00";
//    	String startCheckTime = "1997-01-01 00:00:00";
    	String endCheckTime = DateUtil.getDateStr(new Date()) + " 00:00:00";
    	//通过时间段获取今日有交易记录的用户id列表
    	List<Integer> uidList = null;
    	uidList = walletOptRecordDao.getUidsByTime(DateUtil.parseDateTime(startCheckTime), DateUtil.parseDateTime(endCheckTime));
    	if(uidList != null || uidList.size()>0){
    		//邮件内容
    		StringBuffer mailStr = new StringBuffer() ;
    		for(int i=0;i<uidList.size(); i++){
    			int uid = uidList.get(i);
    			//通过用户id去查询是否有历史校验记录
    			WalletCheckLogs walletCheckLogs = walletCheckLogsDao.getEntityById(uid);
    			if(walletCheckLogs != null){
    				//上次校验后记录的余额
    				double endMlwCoin = walletCheckLogs.getEndMlwCoin().doubleValue();
    				String endTime = DateUtil.getDateTimeStr(walletCheckLogs.getEndTime());
    				
    				//通过用户id和最后校验时间去钱包消费记录中分别获取进账总值和出账总值 必带条件：success_flag = 1
    				double inCoin = 0.00 ;
    				double outCoin = 0.00 ;
    				inCoin = walletOptRecordDao.incomeTotalAmount(uid, DateUtil.parseDateTime(endTime), DateUtil.parseDateTime(endCheckTime)) ;
    				outCoin = walletOptRecordDao.paymentTotalAmount(uid, DateUtil.parseDateTime(endTime), DateUtil.parseDateTime(endCheckTime)) ;
    				
    				//计算：当前余额 = 上次校验记录余额 + 入账金额 - 出账金额
    				double nowCoin = endMlwCoin + inCoin - outCoin ;
    				
    				//通过用户id获取当前用户钱包 获取数据库中钱包实际余额
    				AccountWallet accountWallet = accountWalletDao.getEntityById(uid);
    				if(accountWallet != null){
    					double nowRellCoin = accountWallet.getMlwCoin().doubleValue();
    					//校验金额是否一致：收支情况计算余额 ？= 实际余额
    					boolean isCheckOk = true ;
    					StringBuffer checkInfo = new StringBuffer() ;
    					if(Math.round(nowCoin*100) != Math.round(nowRellCoin*100)){
    						isCheckOk = false ;
    						logger.info("用户美丽钱包余额有出入", "用户id:"+uid+",正确余额:"+nowCoin+",实际余额:"+nowRellCoin, null);
    						checkInfo.append("用户美丽钱包余额有出入[").append("用户id:").append(uid).append(",正确余额:").append(nowCoin).append(",实际余额:").append(nowRellCoin).append("]");
    						//记录异常数据
    						mailStr.append(checkInfo.toString()).append("<br/>");
    					}
    					checkInfo.append(" 校验结果：").append(isCheckOk);
    					//更新校验记录
    					walletCheckLogs.setCheckInfo(checkInfo.toString());
    					walletCheckLogs.setCheckStatus(isCheckOk ? 1 : -1);
    					walletCheckLogs.setEndMlwCoin(BigDecimal.valueOf(nowCoin));
    					walletCheckLogs.setStartTime(DateUtil.parseDateTime(endTime));
    					walletCheckLogs.setEndTime(DateUtil.parseDateTime(endCheckTime));
    					walletCheckLogsDao.update(walletCheckLogs);
    					
    				}else{
    					logger.info("用户钱包不存在！", "action:accountWalletDao.getEntityById,uid:"+uid, null);
    				}
    				
    			//如果不存在校验历史则需要全部校验
    			}else{
    				walletCheckLogs = new WalletCheckLogs() ;
    				//通过用户id和最后校验时间去钱包消费记录中分别获取进账总值和出账总值 必带条件：success_flag = 1
    				double inCoin = 0.00 ;
    				double outCoin = 0.00 ;
    				inCoin = walletOptRecordDao.incomeTotalAmount(uid, DateUtil.parseDateTime("1997-01-01 00:00:00"), DateUtil.parseDateTime(endCheckTime)) ;
    				outCoin = walletOptRecordDao.paymentTotalAmount(uid, DateUtil.parseDateTime("1997-01-01 00:00:00"), DateUtil.parseDateTime(endCheckTime)) ;
    				
    				//计算：当前余额 = 上次校验记录余额 + 入账金额 - 出账金额
    				double nowCoin = (double)(Math.round(inCoin*100) - Math.round(outCoin*100))/100 ;
    				
    				//通过用户id获取当前用户钱包 获取数据库中钱包实际余额
    				AccountWallet accountWallet = accountWalletDao.getEntityById(uid);
    				if(accountWallet != null){
    					double nowRellCoin = accountWallet.getMlwCoin().doubleValue();
    					//校验金额是否一致：收支情况计算余额 ？= 实际余额
    					boolean isCheckOk = true ;
    					StringBuffer checkInfo = new StringBuffer() ;
    					if(Math.round(nowCoin*100) != Math.round(nowRellCoin*100)){
    						isCheckOk = false ;
    						logger.info("用户美丽钱包余额有出入", "用户id:"+uid+",正确余额:"+nowCoin+",实际余额:"+nowRellCoin, null);
    						checkInfo.append("用户美丽钱包余额有出入[").append("用户id:").append(uid).append(",正确余额:").append(nowCoin).append(",实际余额:").append(nowRellCoin).append("]");
    						//记录异常数据
    						mailStr.append(checkInfo.toString()).append("<br/>");
    					}
    					checkInfo.append(" 校验结果：").append(isCheckOk);
    					//更新校验记录
    					walletCheckLogs.setUid(uid);
    					walletCheckLogs.setCheckInfo(checkInfo.toString());
    					walletCheckLogs.setCheckStatus(isCheckOk ? 1 : -1);
    					walletCheckLogs.setEndMlwCoin(BigDecimal.valueOf(nowCoin));
    					walletCheckLogs.setStartTime(DateUtil.parseDateTime("1997-01-01 00:00:00"));
    					walletCheckLogs.setEndTime(DateUtil.parseDateTime(endCheckTime));
    					walletCheckLogsDao.insert(walletCheckLogs);
    					
    				}else{
    					logger.info("用户钱包不存在！", "action:accountWalletDao.getEntityById,uid:"+uid, null);
    				}
    			}
    		}
    		
    		//校验完毕后发送邮件
    		StringBuffer bodyStr = new StringBuffer() ;
    		if(mailStr != null && mailStr.length()>0){
    			bodyStr.append("本次校验结果：存在异常用户，请及时处理！<br/>");
    		}else{
    			bodyStr.append("本次校验结果：所有记录校验通过，钱包交易正常！<br/>");
    		}
    		bodyStr.append("校验起始时间：").append(startCheckTime).append("<br/>");
    		bodyStr.append("校验终止时间：").append(endCheckTime).append("<br/>");
    		bodyStr.append("异常用户记录：<br/>").append(mailStr != null && mailStr.length()>0 ? mailStr.toString() : "无");
    		try{
//    			String isSend = ConfigOnZk.getInstance().getValue("zhuanglong-service/system.properties", "mail.send").trim();
//    			String mailTo  = ConfigOnZk.getInstance().getValue("zhuanglong-service/system.properties", "mail.walletcheck.receive.user").trim();
    			byte[] values = ZKClient.get().getData("/mlwconf/account-service/system.properties");
    			ByteArrayInputStream stream = new ByteArrayInputStream(values);
    			java.util.Properties properties = new java.util.Properties();
    			properties.load(stream);
    			String serverEnvironment = properties.getProperty("mail.walletcheck.serverEnvironment").trim();
    			String isSend = properties.getProperty("mail.send").trim();
    			String mailTo  = properties.getProperty("mail.walletcheck.receive.user").trim();
    			logger.info("钱包交易余额异常验证邮件发送信息", "isSend:"+isSend+", mailTo:"+mailTo, null);
    			if(!StringUtils.isBlank(isSend) && !StringUtils.isBlank(mailTo) && "true".equals(isSend)){
//    				MailUtils.sendMail("admin@meiliwan.cn", mailTo, "美丽湾钱包消费检验情况["+serverEnvironment+"]", bodyStr.toString());
    				MailEntity mailEntity = new MailEntity() ;
    				mailEntity.setReceivers(mailTo);
    				mailEntity.setTitle("美丽湾钱包消费检验情况["+serverEnvironment+"]");
    				mailEntity.setContent(bodyStr.toString());
    				try{
    					BaseMailAndMobileClient.sendMail(mailEntity);
    					logger.info("美丽湾钱包消费检验情况反馈邮件发送成功!", "邮件信息:"+mailEntity.toString(), null);
    				}catch (Exception e) {
						logger.error(e, "邮件发送异常:"+mailEntity.toString(), null);
					}
    				
    			}
    			
    		} catch (Exception e) {
                logger.error(e, "[ZKClient.get().getData:zhuanglong-service/system.properties]", null);
            }
    		
    	}else{
    		logger.info("今日无钱包交易记录", "开始时间节点:"+startCheckTime+",结束时间节点:"+endCheckTime, null);
    	}
    	
    }
    
}
