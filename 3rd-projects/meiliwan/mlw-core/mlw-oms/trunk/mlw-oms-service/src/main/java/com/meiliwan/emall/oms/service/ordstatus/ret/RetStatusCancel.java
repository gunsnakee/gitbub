package com.meiliwan.emall.oms.service.ordstatus.ret;



import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_APPLY;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_BUYER_WAIT_CONSIGNMENT;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_CANCEL;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IR;
import static com.meiliwan.emall.oms.constant.RetFlag.NONE;

import java.util.List;

import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.RetApply;
import com.meiliwan.emall.oms.dao.RetApplyDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meiliwan.emall.oms.constant.OrdIRetStatus;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusChecker;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：逆向-已取消
 * @author yuxiong
 *
 */
@Component
public class RetStatusCancel extends OrderStatusService{

    @Autowired
    private RetApplyDao retApplyDao;

	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		//如果是用户自己取消的退换货，则必须 申请退换货状态
		if(!ordStatusDIO.isAdmin()){
			return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), 
					IR.getType(), RET_APPLY.getCode(), true));
		}else{
			//如果是系统取消的退换货，则必须 是等待用户发货20天
			return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), 
					IR.getType(), RET_BUYER_WAIT_CONSIGNMENT.getCode(), true));
		}
	}
	
	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		if(ordi.getOrderItemStatus().equals(RET_CANCEL.getCode())){
	          throw new ServiceException("OMS-"+getClass().getSimpleName()+"-checkOrderCancel", "已取消的订单不能再取消。");
	    }
		
		checkOrderStatus(ordStatusDIO, unCheckOrder);
				
		if(ordStatusDIO.isAdmin()){
			ordStatusDIO.setAdminId(GlobalNames.ADMINID_SYS_DEFAULT);
		}

        //修改旧订单行的逆向标记 ret_flag 重置为未申请退换状态
        RetApply apply = retApplyDao.getEntityById(ordStatusDIO.getOrderItemId(), true);
        ordiDao.updateOrdIRetFlag(apply.getOldOrdItemId(), NONE.getFlag());

		//修改状态做逆向-已取消
		//ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IR.getType(), RET_CANCEL.getCode());
		//更新订单行表的状态
		//ordiDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), RET_CANCEL.getCode());
		updateOrdIAndOrdStatus(ordStatusDIO, RET_CANCEL);
	}

	@Override
	protected boolean checkOrderStatus(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder)
			throws ServiceException {
		OrderStatusChecker.require(ordStatusDIO, unCheckOrder, this.getClass()) ;
		return true;
	}
	
}
