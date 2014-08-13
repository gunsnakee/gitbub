package com.meiliwan.emall.oms.service.ordstatus.ordipay;


import java.util.List;

import com.meiliwan.emall.oms.bean.Ordi;

import org.springframework.stereotype.Component;

import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_WAIT;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_COMMITTED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IP;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;

/**
 * 订单行状态：待支付
 * @author yuxiong
 *
 */
@Component
public class OrdIPayStatusWait extends OrderStatusService{

	
	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		checkOrderStatus(ordStatusDIO, unCheckOrder);
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

	@Override
	protected boolean checkOrderStatus(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder)
			throws ServiceException {
		if(unCheckOrder != null){
			//循环遍历这个订单行状态结果集
			for(OrdiStatus ordiStatus : unCheckOrder){
				if(ordiStatus.getStatusType().equals(IP.getType())){
					throw new ServiceException("OMS-PayStatusPayWait-execute", "订单行状态已存在，不可重复提交");
				}else if(ordiStatus.getStatusType().equals(IS.getType())
						&& !ordiStatus.getStatusCode().equals(ORDI_COMMITTED.getCode())){
					throw new ServiceException("OMS-PayStatusPayWait-execute", "订单行状态不等于已提交，不可写入待付款"); 
				}
			}
		}
		return true;
	}

}
