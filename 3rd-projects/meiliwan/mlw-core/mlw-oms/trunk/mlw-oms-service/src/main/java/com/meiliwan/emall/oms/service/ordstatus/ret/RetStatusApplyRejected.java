package com.meiliwan.emall.oms.service.ordstatus.ret;


import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_APPLY;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_APPLY_REJECTED;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_FINISHED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IR;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;

import java.util.List;

import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.RetApply;
import com.meiliwan.emall.oms.dao.RetApplyDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.RetFlag;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：逆向-卖家拒绝退换货申请
 * @author yuxiong
 *
 */
@Component
public class RetStatusApplyRejected extends OrderStatusService{
    @Autowired
    private RetApplyDao retApplyDao;

	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), 
				IR.getType(), RET_APPLY.getCode(), true));
	}
	
	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		
		checkOrderStatus(ordStatusDIO, unCheckOrder);
        RetApply apply = retApplyDao.getEntityById(ordStatusDIO.getOrderItemId(), true);
        apply.setState(GlobalNames.STATE_CHECK_NOPASS);
        retApplyDao.update(apply);

		//修改状态做卖家拒绝退换货申请
		ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IR.getType(), RET_APPLY_REJECTED.getCode());
		//更新订单行表的状态
		//ordiDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), RET_APPLY_REJECTED.getCode());
		//updateOrdIAndOrdStatus(ordStatusDIO, RET_APPLY_REJECTED);
		ordiDao.updateOrdIRetFlag(ordStatusDIO.getOrderItemId(), RET_APPLY_REJECTED.getCode(),RetFlag.NONE);
		ordDao.updateOrdStatus(ordStatusDIO.getOrderId(), RET_APPLY_REJECTED.getCode());
	}

	@Override
	protected boolean checkOrderStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder) throws ServiceException {
		
		if(!ordStatusDIO.isAdmin()){
			throw new ServiceException("OMS-"+this.getClass().getSimpleName()+"-execute", "只有管理员可以拒绝退换货");
		}
		
		if(unCheckOrder == null){
			throw new ServiceException("OMS-"+this.getClass().getSimpleName()+"-execute", "无效的订单行状态");
		}
		OrdiStatus ordiStatus = unCheckOrder.get(0);
		if(ordiStatus == null || ordiStatus.getState() == GlobalNames.STATE_INVALID){
			throw new ServiceException("OMS-"+this.getClass().getSimpleName()+"-execute", "无效的订单行状态");
		}else if(IR.getType().equals(ordiStatus.getStatusType()) && ordiStatus.getBillType()!=Constant.ORDER_BILL_TYPE_REVERSE){
			//如果查询逆向订单，则billType必须等于逆向
			throw new ServiceException("OMS-"+this.getClass().getSimpleName()+"-execute", "无效的订单行状态");
		}
		return true;
	}
	
}
