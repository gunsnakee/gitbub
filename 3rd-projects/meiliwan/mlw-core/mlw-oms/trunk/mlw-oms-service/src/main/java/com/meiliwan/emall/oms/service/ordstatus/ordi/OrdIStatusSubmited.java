package com.meiliwan.emall.oms.service.ordstatus.ordi;


import java.util.List;

import org.springframework.stereotype.Component;

import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusChecker;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;
import com.meiliwan.emall.oms.service.ordstatus.ordipay.OrdIPayStatusWait;

import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_WAIT;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_COMMITTED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IP;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;

/**
 * 订单行状态：订单已提交
 * @author yuxiong
 *
 */
@Component
public class OrdIStatusSubmited extends OrderStatusService{
	
	@Override
	protected void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException {
		
		OrdiStatus ordiStatus = new OrdiStatus();
		ordiStatus.setBillType(ordStatusDIO.getBillType());
		ordiStatus.setOrderId(ordStatusDIO.getOrderId());
		ordiStatus.setOrderItemId(ordStatusDIO.getOrderItemId());
		ordiStatus.setState(GlobalNames.STATE_VALID);
		ordiStatus.setStatusCode(ORDI_COMMITTED.getCode());
		ordiStatus.setStatusType(IS.getType());
		ordiStatus.setUid(ordStatusDIO.getUid());
		ordiStatus.setAdminId(ordStatusDIO.getAdminId());
		ordiStatusDao.insert(ordiStatus);
		
		//2、待付款 IP ： PAY_WAIT
		updatePayWait(ordStatusDIO, unCheckOrder);
		
		//更新订单行表的状态
		updateOrdIAndOrdStatus(ordStatusDIO, ORDI_COMMITTED);
	}

//	/**
//	 * //1、提交订单 IS ： ORDI_COMMITTED
//		//2、待付款 IP ： PAY_WAIT
//	 */
//	@Override
//	public void updateStatus(OrderStatusDIO ordStatusDIO) throws ServiceException{
//		
//		List<OrdiStatus> unCheckOrder = getOrdiStatus(ordStatusDIO);
//		OrderStatusChecker.isNull(ordStatusDIO, unCheckData, this.getClass());
//		updateStatus(ordStatusDIO, unCheckOrder);
//	}

	/**
	 * 订单行状态校验
	 * @param ordStatusDIO
	 * @throws ServiceException
	 */
	protected boolean checkOrderStatus(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder) throws ServiceException{
		OrderStatusChecker.isNull(ordStatusDIO, unCheckOrder, this.getClass());
		return true;
	}
	
	private void updatePayWait(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder) throws ServiceException{
		OrderStatusChecker.checkPayWait(ordStatusDIO, unCheckOrder, this.getClass());
		OrdiStatus ordiStatus = new OrdiStatus();
		ordiStatus.setBillType(ordStatusDIO.getBillType());
		ordiStatus.setOrderId(ordStatusDIO.getOrderId());
		ordiStatus.setOrderItemId(ordStatusDIO.getOrderItemId());
		ordiStatus.setState(GlobalNames.STATE_VALID);
		ordiStatus.setStatusCode(PAY_WAIT.getCode());
		ordiStatus.setStatusType(IP.getType());
		ordiStatus.setUid(ordStatusDIO.getUid());
		ordiStatus.setAdminId(ordStatusDIO.getAdminId());
		ordiStatusDao.insert(ordiStatus);
	}


	

}