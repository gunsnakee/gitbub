package com.meiliwan.emall.oms.dao;

import java.util.List;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.oms.bean.OrdPay;
import com.meiliwan.emall.oms.bean.OrdPayKey;

public interface OrdPayDao extends IDao<OrdPayKey,OrdPay>{

	List<OrdPay> getOrdPayListByOrderId(String orderId);

    List<OrdPay> getOrdPayListByOrderIds(List<String> orderIds);

    public void updateByOrderId(String orderId, String payId, String payStatus);
}