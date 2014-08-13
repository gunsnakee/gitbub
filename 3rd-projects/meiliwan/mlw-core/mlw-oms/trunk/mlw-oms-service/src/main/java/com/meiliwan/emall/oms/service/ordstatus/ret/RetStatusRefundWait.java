package com.meiliwan.emall.oms.service.ordstatus.ret;


import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_REFUND_WAIT;
import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_RETUND_FINISHED;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_FINISHED;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_REFUND_FAILURE;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_SELLER_RECEIPTED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IP;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IR;

import java.util.List;

import com.meiliwan.emall.oms.bean.Ordi;

import org.springframework.stereotype.Component;

import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdIRetStatus;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusChecker;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：逆向-待退款,针对换货
 * @author yuxiong
 *
 */
@Component
public class RetStatusRefundWait extends OrderStatusService{

	@Override
	public List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), 
				IR.getType(), OrdIRetStatus.RET_SELLER_CONSINGMENT.getCode()));
	}
	
	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		
		checkOrderStatus(ordStatusDIO, unCheckOrder);
		//插入一条ordisatus
		updateStatusRefundWait(ordStatusDIO, unCheckOrder);
		//修改状态做逆向-退款失败
		//更新订单行表的状态
		updateOrdIAndOrdStatus(ordStatusDIO, OrdIRetStatus.RET_REFUND_WAIT);
		
		ordLogDao.insertRetPayWait(ordi);
	}
	
	public void updateStatusRefundWait(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder) throws ServiceException{
		//OrderStatusChecker.checkOrderRefundWait(ordStatusDIO, unCheckOrder, this.getClass());
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
	}
}
