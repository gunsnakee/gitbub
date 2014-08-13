package com.meiliwan.emall.service.oms;

import java.io.ByteArrayInputStream;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.base.client.BaseMailAndMobileClient;
import com.meiliwan.emall.base.dto.MailEntity;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.plugin.zk.ZKClient;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.constant.OrderType;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

@Service
public class PresaleOrderTaskService extends DefaultBaseServiceImpl{
	private final MLWLogger logger = MLWLoggerFactory.getLogger(PresaleOrderTaskService.class) ;
	private final String CONFIG_FILE = "/mlwconf/bg-oms-service/system.properties";
	
	/**
	 * 1、距订单预订时间剩余3天：当天上午11点，邮件 通知
	 * 2、距订单预订时间当天：当天上午11点，邮件+短信 通知
	 * @param jsonObject
	 */
	public void autoNotifyPresaleOrder(JsonObject jsonObject){
		PageInfo pageInfo = new PageInfo() ;
		pageInfo.setPagesize(Integer.MAX_VALUE);
		//条件：预售订单 + 待发货 
		OrderQueryDTO orderQuery = new OrderQueryDTO() ;
		orderQuery.setOrderType(OrderType.RPS);
		orderQuery.setOrderItemStatus(OrdITotalStatus.ORDI_EFFECTIVED.getCode());
		PagerControl<OrdDTO> pc = OrdClient.getOrderList(orderQuery, pageInfo, true);
		//如果没有这类订单则不需要继续逻辑
		if(pc == null || pc.getEntityList() == null || pc.getEntityList().size() <= 0){
			logger.info("无需要提醒发货的预售订单", orderQuery.toString(), null);
			return ;
		}
		
		StringBuffer mobileSend = new StringBuffer() ;
		StringBuffer emailSend1 = new StringBuffer() ;
		StringBuffer emailSend3 = new StringBuffer() ;
		
		int moNum = 0 ;
		int eoNum = 0 ;
		for(OrdDTO dto : pc.getEntityList()){
			Date presaleDate = dto.getPreSaleTime();
			//考虑到结束的情况是没有时间的，所以排除掉
			if(presaleDate != null){
				Date nowDate = new Date() ;
				long between = (presaleDate.getTime() - nowDate.getTime())/1000 ;
				long day = between / (24*3600) ;
				// 距预计发货时间在一天内的都需要发送短信和邮件
				if(day < 1 && day > -1){
					if(moNum == 0){
						mobileSend.append("订单号：").append(dto.getOrderId());
						emailSend1.append("订单号：").append(dto.getOrderId());
					}else{
						//手机指发前三个订单
						if(moNum < 3){
							mobileSend.append(",").append(dto.getOrderId());
						}
						emailSend1.append(",").append(dto.getOrderId());
					}
					
					moNum ++ ;
				}
				// 距预计发货时间在一天以上三天以内的都需要发送邮件
				if(day >= 2 && day < 3){
					if(eoNum == 0){
						emailSend3.append("订单号：").append(dto.getOrderId());
					}else{
						emailSend3.append(",").append(dto.getOrderId());
					}
					
					eoNum ++ ;
				}
			}
		}
		
		// 如果都没有需要提醒的则结束
		if((mobileSend == null || StringUtils.isBlank(mobileSend.toString())) 
				&& (emailSend3 == null || StringUtils.isBlank(emailSend3.toString())) ){
			logger.info("无需要提醒发货的预售订单", orderQuery.toString(), null);
			return ;
		}
		
//		ConfigOnZk zkConfig = ConfigOnZk.getInstance();
		String toMobile = "" ;
		String toEmail = "" ;
		try{
			//这种方式只能获取逗号前的一个号码
//			toMobile = zkConfig.getValue(CONFIG_FILE, "oms.presale.notify.mobile");
//			toEmail = zkConfig.getValue(CONFIG_FILE, "oms.presale.notify.email");
			
			byte[] values = ZKClient.get().getData(CONFIG_FILE);
			ByteArrayInputStream stream = new ByteArrayInputStream(values);
			java.util.Properties properties = new java.util.Properties();
			properties.load(stream);
			toMobile = properties.getProperty("oms.presale.notify.mobile");
			toEmail = properties.getProperty("oms.presale.notify.email");
			
		}catch (Exception e) {
			logger.error(e, "zkConfig.getValue erro 获取短信、邮件接收人失败.", null);
		}
		
		if(mobileSend != null && !StringUtils.isBlank(mobileSend.toString())){
			mobileSend.append("等").append(moNum).append("个订单已到预售时间，请注意跟进发货。");
			emailSend1.append("共").append(moNum).append("个订单，已到预售时间，请注意检查货品并跟进发货！");
			BaseMailAndMobileClient.sendMobile(toMobile, mobileSend.toString());
			MailEntity mailEntity = new MailEntity() ;
			mailEntity.setReceivers(toEmail);
			mailEntity.setTitle("预售商品发货提醒（剩余1天）");
			mailEntity.setContent(emailSend1.toString());
			BaseMailAndMobileClient.sendMail(mailEntity);
			
		}
		
		if(emailSend3 != null && !StringUtils.isBlank(emailSend3.toString())){
			emailSend3.append("共").append(eoNum).append("个订单，已到预售时间，请注意检查货品并跟进发货！");
			MailEntity mailEntity = new MailEntity() ;
			mailEntity.setReceivers(toEmail);
			mailEntity.setTitle("预售商品发货提醒");
			mailEntity.setContent(emailSend3.toString());
			BaseMailAndMobileClient.sendMail(mailEntity);
			
		}
		
	}
}
