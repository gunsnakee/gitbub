package com.meiliwan.emall.oms.service.ordstatus.deliver;


import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_WAIT_CONSIGNMENT;
import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_WAIT_OUT_REPOSITORY;
import static com.meiliwan.emall.oms.constant.OrderStatusType.ID;

import java.util.Date;
import java.util.List;

import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.bean.Ordi;
import org.springframework.stereotype.Component;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusChecker;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：待发货
 * @author yuxiong
 *
 */
@Component
public class DeliStatusWaitConsigment extends OrderStatusService{

	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), 
				ID.getType(), DELI_WAIT_OUT_REPOSITORY.getCode(), true));
	}

	@Override
	protected boolean checkOrderStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder) throws ServiceException {
		OrderStatusChecker.require(ordStatusDIO, unCheckOrder, this.getClass());
		return true;
	}

	@Override
	protected void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException {
		//修改状态做待发货
		ordiStatusDao.updateAllOrdIStatus(ordStatusDIO.getOrderId(), ID.getType(), DELI_WAIT_CONSIGNMENT.getCode());
        //修改订单出库时间
        Ord ord = new Ord();
        ord.setOrderId(ordi.getOrderId());
        ord.setStockTime(new Date());
        ord.setUpdateTime(ord.getStockTime());
        ordDao.update(ord);
	}
}
