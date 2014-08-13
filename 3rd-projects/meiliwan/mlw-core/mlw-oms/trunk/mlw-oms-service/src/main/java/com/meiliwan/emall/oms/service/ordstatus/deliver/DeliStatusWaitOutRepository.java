package com.meiliwan.emall.oms.service.ordstatus.deliver;


import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_WAIT_OUT_REPOSITORY;
import static com.meiliwan.emall.oms.constant.OrderStatusType.ID;

import java.util.List;

import com.meiliwan.emall.oms.bean.Ordi;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusChecker;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：等待出库
 * @author yuxiong
 *
 */
@Repository
public class DeliStatusWaitOutRepository extends OrderStatusService{

	@Override
	public List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), ID.getType());
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
		OrdiStatus ordiStatus = new OrdiStatus();
		ordiStatus.setBillType(ordStatusDIO.getBillType());
		ordiStatus.setOrderId(ordStatusDIO.getOrderId());
		ordiStatus.setOrderItemId(ordStatusDIO.getOrderItemId());
		ordiStatus.setState(GlobalNames.STATE_VALID);
		ordiStatus.setStatusCode(DELI_WAIT_OUT_REPOSITORY.getCode());
		ordiStatus.setStatusType(ID.getType());
		ordiStatus.setUid(ordStatusDIO.getUid());
		ordiStatus.setAdminId(ordStatusDIO.getAdminId());
		ordiStatusDao.insert(ordiStatus);
	}
	
//	@Override
//	public boolean checkOrderStatus(OrderStatusDIO ordStatusDIO,
//			List<OmsOrdiStatus> unCheckOrder) throws ServiceException {
//		if(unCheckOrder != null && unCheckOrder.size()>0){
//			throw new ServiceException("OMS-DeliStatusWaitOutRepository-execute", "无效的订单行状态");
//		}
//		return true;
//	}
}
