package com.meiliwan.emall.oms.service.ordstatus.ordipay;


import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_REFUND_FAILURE;
import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_REFUND_WAIT;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_REFUND_FAILURE;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IP;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IR;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.meiliwan.emall.oms.constant.OrdIPayStatus;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：逆向-退款失败
 * @author yuxiong
 *
 */
@Component
public class OrdIPayStatusRefundFailure extends OrderStatusService{

	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return Collections.emptyList();
	}
	
	/**
	 * 订单行状态校验
	 * @param ordStatusDIO
	 * @throws ServiceException
	 */
	@Override
	public boolean checkOrderStatus(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder) throws ServiceException{
		//待付款，和退款失败
		List<OrdiStatus> list = ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), 
				IP.getType(), true);
		
		for (OrdiStatus ordiStatus : list) {
			if(ordiStatus.getStatusCode().equals(OrdIPayStatus.PAY_REFUND_FAILURE.getCode())||
					ordiStatus.getStatusCode().equals(PAY_REFUND_WAIT.getCode())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		
		//checkOrderStatus(ordStatusDIO, unCheckOrder);
		
		//修改状态做退款失败
		ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IP.getType(), PAY_REFUND_FAILURE.getCode());
		//修改状态做逆向-退款失败
		ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IR.getType(), RET_REFUND_FAILURE.getCode());
		//更新订单行表的状态
		ordiDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), RET_REFUND_FAILURE.getCode());
        ordDao.updateOrdStatus(ordi.getOrderId(), RET_REFUND_FAILURE.getCode());
		//记录日志
		ordLogDao.insertRetPayFail(ordi);
		
	}
}
