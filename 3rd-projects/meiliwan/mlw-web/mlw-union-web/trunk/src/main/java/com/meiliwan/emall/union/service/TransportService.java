package com.meiliwan.emall.union.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.base.client.BaseMailAndMobileClient;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.oms.bean.OrdAddr;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.union.bean.Transport;
import com.meiliwan.emall.union.bean.TransportCode;
import com.meiliwan.emall.union.dao.TransportDao;

@Service
public class TransportService {

	private final static MLWLogger logger = MLWLoggerFactory.getLogger(TransportService.class);
	
	@Autowired
	private TransportDao transportDao;

	public TransportCode insertTransport(Transport transport){
		if(transport==null){
			return TransportCode.REQUEST_DATA;
		}
		if(StringUtils.isBlank(transport.getOrderId())){
			return TransportCode.VALIDATE;
		}
		if(StringUtils.isBlank(transport.getInfo())){
			return TransportCode.VALIDATE;
		}
		if(transport.getOrderId().length()<12){
			return TransportCode.NOT_ORDER_ID;
		}
		//boolean result = transportDao.queryOrderByOrderId(transport.getOrderId());
		OrdAddr addr = OrdClient.getOrdAddrById(transport.getOrderId());
		if(addr==null){
			return TransportCode.NOT_FOUND;
		}
		boolean resultCust = transportDao.queryByCustCodeAndId(transport.getLogisticsCompany(), transport.getCustDataId());
		if(resultCust){
			return TransportCode.DATA_REPEAT;
		}
		
		try {
			transportDao.insertTransportLog(transport);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, null, null);
			return TransportCode.SYSTEM;
		}
		
		if(transport.isSigned()){
			sendMessage(addr.getMobile());
		}
			
		return TransportCode.SUCCESS;
	}

	public void sendMessage(String mobile){
		try {
			BaseMailAndMobileClient.sendMobile(mobile, "你的订单已经签收!");
		} catch (Exception e) {
			logger.error(e, null, null);
		}
	}

}
