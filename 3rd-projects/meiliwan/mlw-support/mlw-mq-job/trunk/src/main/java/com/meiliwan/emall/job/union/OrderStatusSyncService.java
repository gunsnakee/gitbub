package com.meiliwan.emall.job.union;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.bean.union.UnionOrder;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

@Service
public class OrderStatusSyncService  extends DefaultBaseServiceImpl{
	
	@Autowired
	private static UnionOrderUpdateService unionOrderUpdateService;
	public static UnionOrderUpdateService getUnionOrderUpdateService() {
		return unionOrderUpdateService;
	}

	public static void setUnionOrderUpdateService(
			UnionOrderUpdateService unionOrderUpdateService) {
		OrderStatusSyncService.unionOrderUpdateService = unionOrderUpdateService;
	}

	private static UnionOrder getUnionOrder(int orderStatus){
		UnionOrder unionOrder = new UnionOrder();
//		unionOrder.setSourceType("channet");
		unionOrder.setOrderStatus(orderStatus);
		unionOrder.setPayStatus(0);
		return unionOrder;
	}
	
	/**
	 * 默认日期45天之内
	 * @throws InterruptedException 
	 * */
	public void syncOrderStatus(JsonObject jsonObject){
		UnionOrder  UnionOrder = getUnionOrder(60);
		unionOrderUpdateService.handlerCenter(UnionOrder);
		
		UnionOrder = getUnionOrder(7);
		unionOrderUpdateService.reOrderCenter(UnionOrder);
	}

}
