package com.meiliwan.emall.oms.service.ordstatus.ordipay;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.RetApply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdIPayStatus;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dao.RetApplyDao;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;
import com.meiliwan.emall.oms.service.ordstatus.ret.RetStatusFinished;




import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_WAIT_CONSIGNMENT;
import static com.meiliwan.emall.oms.constant.OrdIPayStatus.*;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_FINISHED;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_CONSINGMENT;
import static com.meiliwan.emall.oms.constant.OrderStatusType.ID;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IP;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IR;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;

/**
 * 订单行状态：逆向-退款成功,PAY_RETUND_FINISHED
 * @author yuxiong
 *
 */
@Component
public class OrdIPayStatusRefundFinished extends OrderStatusService{

	@Autowired
	private RetApplyDao retApplyDao;
	
	/**
	 * IR:30,50,
	 * IP:没有,50
	 * @return
	 */
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		//是否有效的订单
		return ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), true);
	}
	
	
	@Override
	protected boolean checkOrderStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder) throws ServiceException {
		// TODO Auto-generated method stub
		if(unCheckOrder == null || unCheckOrder.size()==0){
			throw new ServiceException("OMS-"+getClass().getSimpleName()+"-isValidOrdIStatus", "无效的订单行状态");
		}
		OrdiStatus ordiStatus = unCheckOrder.get(0);
		if(ordiStatus == null || ordiStatus.getState() == GlobalNames.STATE_INVALID){
			throw new ServiceException("OMS-"+getClass().getSimpleName()+"-isValidOrdIStatus", "无效的订单行状态");
		}else if(IR.getType().equals(ordiStatus.getStatusType()) && ordiStatus.getBillType()!=Constant.ORDER_BILL_TYPE_REVERSE){
			//如果查询逆向订单，则billType必须等于逆向
			throw new ServiceException("OMS-"+getClass().getSimpleName()+"-isValidOrdIStatus", "无效的订单行状态");
		}
		return true;
	}


	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		
		checkOrderStatus(ordStatusDIO, unCheckOrder);
		
		//修改状态做退款成功
		ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IP.getType(), PAY_RETUND_FINISHED.getCode());
		
		//修改状态做逆向-已完成
		ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IR.getType(), RET_FINISHED.getCode());

        Date payTime = new Date();

		//更新订单行表的状态
		ordiDao.updateAllOrdIStatus(ordi.getOrderId(), RET_FINISHED.getCode(), payTime);
        ordDao.updateOrdStatus(ordi.getOrderId(), RET_FINISHED.getCode(), payTime);

		RetApply apply = retApplyDao.getEntityById(ordStatusDIO.getOrderItemId(), true);
		//ret_pay_fare ret_total_amount=ret_pay_amount
		
		apply.setRetPayAmount(ordStatusDIO.getRetTotalAmount());
		retApplyDao.update(apply);
//		unCheckOrder = getRetStatusFinished(ordStatusDIO);
//		updateRetStatusFinished(ordStatusDIO, unCheckOrder);
		double retTotal = ordStatusDIO.getRetTotalAmount();
		if(retTotal==0){
			ordLogDao.insertRetPayFinish(ordi,"退款已完成,退还金额:0.00元");
			return ;
		}
		BigDecimal bd = new BigDecimal(retTotal);   
        bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);  
        
		StringBuilder msg = new StringBuilder();
		msg.append("金额已退到电子钱包,退还金额:").append(bd).append("元。");
		ordLogDao.insertRetPayFinish(ordi,msg.toString());
	}

//	private List<OrdiStatus> getRetStatusFinished(OrderStatusDIO ordStatusDIO) throws ServiceException{
//		return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), 
//				IP.getType(), PAY_RETUND_FINISHED.getCode()));
//	}
//	
//	private void updateRetStatusFinished(OrderStatusDIO ordStatusDIO,
//			List<OrdiStatus> unCheckOrder) throws ServiceException{
//		
//		checkOrderStatus(ordStatusDIO, unCheckOrder);
//						
//		//修改状态做逆向-已完成
//		ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IR.getType(), RET_FINISHED.getCode());
//	}
	
}
