package com.meiliwan.emall.pay.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pay.bean.PayItem;

import java.util.List;

public interface B2BPayItemDao  extends IDao<String, PayItem>{

	List<PayItem> getPayItemByOrderId(String orderId);

    List<PayItem> getPayItemByOrderId(String orderId, boolean isMainDS);
	
	int delFailPayItemByOrderId(String orderId);
	
}