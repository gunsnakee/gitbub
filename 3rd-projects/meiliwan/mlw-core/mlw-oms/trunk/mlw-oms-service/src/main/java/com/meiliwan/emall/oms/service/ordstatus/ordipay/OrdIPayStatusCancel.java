package com.meiliwan.emall.oms.service.ordstatus.ordipay;


import java.util.List;

import com.meiliwan.emall.oms.bean.Ordi;

import org.springframework.stereotype.Component;

import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusChecker;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;
import com.meiliwan.emall.oms.service.ordstatus.ordi.OrdIStatusCancel;
import com.meiliwan.emall.oms.service.ordstatus.ordi.OrdIStatusEffectived;

import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_WAIT_CONSIGNMENT;
import static com.meiliwan.emall.oms.constant.OrdIPayStatus.*;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_CANCEL;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_CONSINGMENT;
import static com.meiliwan.emall.oms.constant.OrderStatusType.ID;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IP;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;

/**
 * 订单行状态：支付取消
 * @author yuxiong
 *
 */
@Component
public class OrdIPayStatusCancel extends OrderStatusService{

	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatus2List(ordiStatusDao.selectOneByOrderId(ordStatusDIO.getOrderId(),
				IP.getType(), PAY_WAIT.getCode(), true));
	}
	
	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		
		checkOrderStatus(ordStatusDIO, unCheckOrder);

        //1、取消付款 IP ： PAY_CANCEL
		//修改所有的订单行状态做支付取消
		ordiStatusDao.updateAllOrdIStatus(ordStatusDIO.getOrderId(), IP.getType(), PAY_CANCEL.getCode());

		//2、订单取消 IS ： ORDI_CANCEL，如果是取消支付的情况去取消订单，则以系统默认管理员身份去取消
        //修改状态做已取消
        ordiStatusDao.updateAllOrdIStatus(ordStatusDIO.getOrderId(), IS.getType(), ORDI_CANCEL.getCode(), GlobalNames.ADMINID_SYS_DEFAULT);
		
		//更新订单行表的状态
		ordiDao.updateAllOrdIStatus(ordStatusDIO.getOrderId(), ORDI_CANCEL.getCode());

        ordDao.updateOrdStatus(ordStatusDIO.getOrderId(), ORDI_CANCEL.getCode());
	}

//	private void updateStatusCancel(OrderStatusDTO ordStatusDIO) throws ServiceException {
//		List<OrdiStatus> unCheckOrder = ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId());
//		ordStatusDIO.setAdminId(GlobalNames.ADMINID_SYS_DEFAULT);
//		checkOrderStatus(ordStatusDIO, unCheckOrder);
//		//修改状态做已取消
//		ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IS.getType(), ORDI_CANCEL.getCode());
//	}
}
