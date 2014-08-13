package com.meiliwan.emall.oms.service.ordstatus.deliver;


import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_DELIVER_GOODS;
import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_REJECTED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.ID;

import java.util.List;

import com.meiliwan.emall.oms.bean.Ordi;

import org.springframework.stereotype.Component;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：拒绝收货
 * @author yuxiong
 *
 */
@Component
public class DeliStatusRejected extends OrderStatusService{

	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatus2List(ordiStatusDao.selectOneByOrderId(ordStatusDIO.getOrderId(), ID.getType(), DELI_DELIVER_GOODS.getCode(), true));

	}
	
	@Override
	protected void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{

		//修改状态做拒绝收货
		ordiStatusDao.updateAllOrdIStatus(ordStatusDIO.getOrderId(), ID.getType(), DELI_REJECTED.getCode());
		//客户拒收,第二版
		ordLogDao.insertOrderReject(ordi);
	}

}
