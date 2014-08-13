package com.meiliwan.emall.commons.log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.meiliwan.emall.commons.rabbitmq.MsgSender;

public class UserBizLogDemo {
	
	public static void main(String[] args) {
		//user 业务用户登录:
		// user 
		
		Map<String, String> extraDataMap = new HashMap<String, String>();
		// 使用金额数
		extraDataMap.put("quantity", "38");
		// why
		extraDataMap.put("target", "buyProduct");
		// with product id
		extraDataMap.put("productId", "123198898198");
		// with order id
		extraDataMap.put("orderId", "30989771279");
		
		BizLog bizLog =  new BizLog.LogBuilder(BizLogModel.BKSTAGE, new BizLogSubModel() {
			
			@Override
			public String getSubModelName() {
				return "SubModelName";
			}
		}, "SP-smallsp-add", new Date())
			.setUser(123456, "user", "0")
			.setObj(BizLogObjOrder.ORDER, "a231920990")
			.setOp(BizLogOp.ADD)
			.setExtraData(new Gson().toJson(extraDataMap))
			
			.setHost("account.meiliwan.com")
			.setUserClientIp("10.249.10.110")
			
			.build();
		
		BizLog.log(bizLog);
		
		MsgSender.bizLog(bizLog);
		
	}
	
}
