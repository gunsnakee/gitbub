package com.meiliwan.emall.oms.service.ordstatus.ordipay;


import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_REFUND_WAIT;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_BUYER_RECEIPTED;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_SELLER_RECEIPTED;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_WAIT_CONSULT;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IP;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IR;

import java.util.List;

import org.springframework.stereotype.Component;

import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdIRetStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：逆向-待退款,针对换货
 * @author yuxiong
 *
 */
@Component
public class OrdIPayStatusRefundWait extends OrderStatusService{
	/**
	 * 拿订单行状态并且校验订单是否有效
	 * @return
	 */
	@Override
	public List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), 
				IR.getType(), OrdIRetStatus.RET_SELLER_CONSINGMENT.getCode()));
	}
	
	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		
		checkOrderStatus(ordStatusDIO, unCheckOrder);
		OrdiStatus ordiStatus = new OrdiStatus();
		ordiStatus.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
		ordiStatus.setOrderId(ordStatusDIO.getOrderId());
		ordiStatus.setOrderItemId(ordStatusDIO.getOrderItemId());
		ordiStatus.setState(GlobalNames.STATE_VALID);
		ordiStatus.setStatusCode(PAY_REFUND_WAIT.getCode());
		ordiStatus.setStatusType(IP.getType());
		ordiStatus.setUid(ordStatusDIO.getUid());
		ordiStatus.setAdminId(ordStatusDIO.getAdminId());
		ordiStatusDao.insert(ordiStatus);
		
		updateOrdIAndOrdStatus(ordStatusDIO, OrdIRetStatus.RET_REFUND_WAIT);
		
		ordLogDao.insertRetPayWait(ordi);
		
	}
	

	@Override
	protected boolean checkOrderStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder) throws ServiceException {
		
		if(unCheckOrder != null){
			//循环遍历这个订单行状态结果集
			for(OrdiStatus ordiStatus : unCheckOrder){
				if(ordiStatus.getStatusType().equals(IP.getType())){
					throw new ServiceException("OMS-OrdIPayStatusRefundWait-execute", "订单行状态已存在，不可重复提交"); 
				}else if(ordiStatus.getStatusType().equals(IR.getType())
						&& !ordiStatus.getStatusCode().equals(RET_WAIT_CONSULT.getCode())
						&& !ordiStatus.getStatusCode().equals(RET_SELLER_RECEIPTED.getCode())
						&& !ordiStatus.getStatusCode().equals(RET_BUYER_RECEIPTED.getCode())){
					throw new ServiceException("OMS-OrdIPayStatusRefundWait-execute", "订单行状态只有待协商和客服确认收货，方可退款"); 
				}
			}
		}
		return true;
	}
	
	
	
}
