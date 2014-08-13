package com.meiliwan.emall.oms.service.ordstatus.ret;


import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_WAIT_OUT_REPOSITORY;
import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_REFUND_WAIT;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_BUYER_DELIVER_GOODS;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_SELLER_RECEIPTED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.ID;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IP;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IR;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdIRetStatus;
import com.meiliwan.emall.oms.constant.OrderType;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.bean.RetApply;
import com.meiliwan.emall.oms.dao.RetApplyDao;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusChecker;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：逆向-卖家确认收货
 * @author yuxiong
 *
 */
@Component
public class RetStatusSellerReceipted extends OrderStatusService{

	@Autowired
	private RetApplyDao retOrderDao;	
	
	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), 
				IR.getType(), RET_BUYER_DELIVER_GOODS.getCode(), true));
	}
	
	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		
		RetApply retOrder = retOrderDao.getEntityById(ordStatusDIO.getOrderItemId(), true);
		if(retOrder == null){
			throw new ServiceException("OMS-RetStatusSellerReceipted-execute", "无效的退换货订单");
		}
		
		checkOrderStatus(ordStatusDIO, unCheckOrder);
		
		//修改状态做逆向卖家确认收货
		ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IR.getType(), RET_SELLER_RECEIPTED.getCode());
		
		//在这里做判断，如果是退货流程，则直接走退款状态，逆向-待退款，IP: 
		if(retOrder.getRetType().equals(OrderType.RECEDE.getCode())){
			List<OrdiStatus> unCheckOrderRefundWait = getOrdiStatusRefundWait(ordStatusDIO);
			updateStatusRefundWait(ordStatusDIO, unCheckOrderRefundWait);
			updateOrdIAndOrdStatus(ordStatusDIO, OrdIRetStatus.RET_REFUND_WAIT);
			
			ordLogDao.insertRetSellerReceive(ordi,"客服确认收货,等待客服退款。");
			return ;
		}else{
            //写入一条物流的待出库状态
            //对于换货来说，需要校验它的物流配送状态
            unCheckOrder = ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), ID.getType(), true);
            OrderStatusChecker.isNull(ordStatusDIO, unCheckOrder, this.getClass());

            //对于换货来说，需要写入一个待出库的状态
            OrdiStatus ordiStatus = new OrdiStatus();
            ordiStatus.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
            ordiStatus.setOrderId(ordi.getOrderId());
            ordiStatus.setOrderItemId(ordStatusDIO.getOrderItemId());
            ordiStatus.setState(GlobalNames.STATE_VALID);
            ordiStatus.setStatusCode(DELI_WAIT_OUT_REPOSITORY.getCode());
            ordiStatus.setStatusType(ID.getType());
            ordiStatus.setUid(ordStatusDIO.getUid());
            ordiStatus.setAdminId(ordStatusDIO.getAdminId());
            ordiStatusDao.insert(ordiStatus);
            updateOrdIAndOrdStatus(ordStatusDIO, RET_SELLER_RECEIPTED);
            ordLogDao.insertRetSellerReceive(ordi,"客服确认收货,等待客服发货。");
        }
		
		//更新订单行表的状态
		//ordiDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), RET_SELLER_RECEIPTED.getCode());
	}

	
	private List<OrdiStatus> getOrdiStatusRefundWait(OrderStatusDTO ordStatusDIO) throws ServiceException{
		//是否有效的订单
		return ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), true);
	}
	
	public void updateStatusRefundWait(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder) throws ServiceException{
		OrderStatusChecker.checkOrderRefundWait(ordStatusDIO, unCheckOrder, this.getClass());
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
	
//	/**
//	 * 链式执行各订单状态的切换
//	 * @param ordStatusDIO
//	 * @param orderStatuses
//	 * @throws ServiceException
//	 */
//	public void updateStatusChain(OrderStatusDIO ordStatusDIO, OrderStatusService... orderStatuses) throws ServiceException{
//
//		OmsRetOrder retOrder = retOrderDao.getEntityById(ordStatusDIO.getOrderItemId());
//		if(retOrder == null){
//			throw new ServiceException("OMS-RetStatusSellerReceipted-execute", "无效的退换货订单");
//		}
//		
//		//执行第一个订单状态
//		List<OrdiStatus> unCheckOrder = this.getOrdiStatus(ordStatusDIO);
//		List<OrdiStatus> unCheckOrderRefundWait = null;
//		
//		updateStatus(ordStatusDIO,  unCheckOrder);
//		
//		//在这里做判断，如果是退货流程，则直接走退款状态，逆向-待退款，IP: 
//		if(retOrder.getRetType().equals(GlobalNames.ORDER_RET_TYPE.REFUND.toString())){
//			OrdIPayStatusRefundWait ordIPayStatusRefundWait = (OrdIPayStatusRefundWait)orderStatuses[0];
//			unCheckOrderRefundWait = ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId());
//			ordIPayStatusRefundWait.updateStatus(ordStatusDIO, unCheckOrderRefundWait);
//		}
//	
//	}
	
}
