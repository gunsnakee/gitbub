package com.meiliwan.emall.oms.service.ordstatus.ret;


import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_APPLY;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_BUYER_WAIT_CONSIGNMENT;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IR;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meiliwan.emall.oms.constant.OrderType;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.bean.RetApply;
import com.meiliwan.emall.oms.dao.RetApplyDao;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：逆向买家待发货
 * @author yuxiong
 *
 */
@Component
public class RetStatusBuyerWaitConsignment extends OrderStatusService{

	
//	@Autowired
//	private RetChgDao retChgDao;
//	@Autowired
//	private RetPayDao retPayDao;
	@Autowired
	private RetApplyDao retApplyDao;
	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), 
				IR.getType(), RET_APPLY.getCode(), true));
	}
	
	/**
	 * 
	 */
	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		//checkOrderStatus(ordStatusDIO, unCheckOrder);
				
		//修改状态做逆向买家待发货
		ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IR.getType(), RET_BUYER_WAIT_CONSIGNMENT.getCode());
		//更新订单行表的状态
		String orderItemId = ordi.getOrderItemId();
		String orderType = ordStatusDIO.getOrderType();
		
		RetApply apply = retApplyDao.getEntityById(orderItemId, true);
        apply.setState(GlobalNames.STATE_CHECK_PASS);
        short proCount = apply.getProCount();
        if(ordStatusDIO.getRetProCount()>0){
			apply.setProCount(ordStatusDIO.getRetProCount());
		}
        
		if(orderType.equals(OrderType.CHANGE.getCode())){
			insertChg(apply,ordStatusDIO);
			ordiDao.updateOrdIStatus(apply.getRetordItemId(), RET_BUYER_WAIT_CONSIGNMENT.getCode(),OrderType.CHANGE.getCode());
			ordDao.updateOrdStatus(apply.getRetordId(), RET_BUYER_WAIT_CONSIGNMENT.getCode(),OrderType.CHANGE.getCode());
		}
		if(orderType.equals(OrderType.RECEDE.getCode())){
			
			insertPay(apply,ordStatusDIO);
			ordiDao.updateOrdIStatus(apply.getRetordItemId(), RET_BUYER_WAIT_CONSIGNMENT.getCode(),OrderType.RECEDE.getCode());
			ordDao.updateOrdStatus(apply.getRetordId(), RET_BUYER_WAIT_CONSIGNMENT.getCode(),OrderType.RECEDE.getCode());
		}
		
		ordLogDao.insertApplyExtraOrdLog(ordi,"申请退换货数量"+proCount +",审核通过的数量为"+ordStatusDIO.getRetProCount()+"。");
		ordLogDao.insertApplyAgree(ordi);
		
        
	}

	

	/**
	 * 换货,换货订单插入一条记录，更新申请订单的类型
	 * @param apply
	 * @param ordStatusDIO
	 */
	private void insertChg(RetApply apply,OrderStatusDTO ordStatusDIO) {
//		RetChg chg = applyCloneToRetChg(apply,ordStatusDIO);

		apply.setRetType(OrderType.CHANGE.getCode());
		apply.setReturnReason(ordStatusDIO.getReturnReasonType());
		apply.setNeedRetpro(ordStatusDIO.getNeedRetpro());
		retApplyDao.update(apply);
//		retChgDao.insert(chg);

	}
	
	/**
	 * 退货,退货订单插入一条记录，更新申请订单的类型
	 * @param apply
	 * @param ordStatusDIO
	 */
	private void insertPay(RetApply apply,OrderStatusDTO ordStatusDIO) {
//		RetPay pay = applyCloneToRetPay(apply,ordStatusDIO);
        apply.setRetType(OrderType.RECEDE.getCode());
		apply.setReturnReason(ordStatusDIO.getReturnReasonType());
		apply.setNeedRetpro(ordStatusDIO.getNeedRetpro());

		retApplyDao.update(apply);
//		retPayDao.insert(pay);

	}
	
//	private RetChg applyCloneToRetChg(RetApply apply,OrderStatusDTO ordStatusDIO){
//		RetChg retChg = new RetChg();
//		retChg.setChgReason(apply.getReturnReason());
//		retChg.setChgTime(apply.getCreateTime());
//		retChg.setCreateTime(new Date());
//		retChg.setRetordId(apply.getRetordId());
//		retChg.setRetordItemId(apply.getRetordItemId());
//		retChg.setState(GlobalNames.STATE_VALID);
//		retChg.setUpdateTime(new Date());
//		return retChg;
//	}
//
//	private RetPay applyCloneToRetPay(RetApply apply,
//			OrderStatusDTO ordStatusDIO) {
//        Date date = new Date();
//		RetPay pay = new RetPay();
//		pay.setCreateTime(date);
//		pay.setRetordId(apply.getRetordId());
//		pay.setRetordItemId(apply.getRetordItemId());
//		pay.setState(GlobalNames.STATE_VALID);
//		pay.setUpdateTime(date);
//        pay.setRetTime(date);
//		return pay;
//	}
}
