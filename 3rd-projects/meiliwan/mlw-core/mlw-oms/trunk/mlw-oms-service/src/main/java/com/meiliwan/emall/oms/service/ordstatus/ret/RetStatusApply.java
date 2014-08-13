package com.meiliwan.emall.oms.service.ordstatus.ret;


import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_APPLY;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_FINISHED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IR;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.bean.RetApply;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.dao.RetApplyDao;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：申请退换货
 * @author yuxiong
 *
 */
@Component
public class RetStatusApply extends OrderStatusService{

    @Autowired
    private RetApplyDao retApplyDao;

	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOldOrderItemId(), 
				IS.getType(), ORDI_FINISHED.getCode()));
	}

	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		
		checkOrderStatus(ordStatusDIO, unCheckOrder);
        //校验订单行和头的数据
        //Ordi ordi = ordiDao.getEntityById(ordStatusDIO.getOrderItemId());
        if(ordi != null){
            throw new ServiceException("OMS-"+this.getClass().getSimpleName()+"-execute", "订单ID已存在");
        }
        Ord ord = ordDao.getEntityById(ordStatusDIO.getOrderId());
        if(ord != null){
            throw new ServiceException("OMS-"+this.getClass().getSimpleName()+"-execute", "订单行ID已存在");
        }
        RetApply apply = retApplyDao.getEntityById(ordStatusDIO.getOrderItemId());
        if(ord != null){
            throw new ServiceException("OMS-"+this.getClass().getSimpleName()+"-execute", "退换货申请ID已存在");
        }

		OrdiStatus ordiStatus = new OrdiStatus();
		ordiStatus.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);	//写逆向订单
		ordiStatus.setOrderId(ordStatusDIO.getOrderId());
		ordiStatus.setOrderItemId(ordStatusDIO.getOrderItemId());
		ordiStatus.setState(GlobalNames.STATE_VALID);
		ordiStatus.setStatusCode(RET_APPLY.getCode());
		ordiStatus.setStatusType(IR.getType());
		ordiStatus.setUid(ordStatusDIO.getUid());
		ordiStatus.setAdminId(ordStatusDIO.getAdminId());
		ordiStatusDao.insert(ordiStatus);

        //保存apply
        //保存ord
        //保存ordi
		//更新订单行表的状态
		//updateOrdIAndOrdStatus(ordStatusDIO, RET_APPLY);
	}

	@Override
	protected boolean checkOrderStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder) throws ServiceException {
		
		if(unCheckOrder == null){
			throw new ServiceException("OMS-"+this.getClass().getSimpleName()+"-execute", "无效的订单行状态");
		}
		OrdiStatus ordiStatus = unCheckOrder.get(0);
		if(ordiStatus == null || ordiStatus.getState() == GlobalNames.STATE_INVALID){
			throw new ServiceException("OMS-RetStatusApply-execute", "必须对已完成的订单做申请退换货");
		}
		List<OrdiStatus> ordiStatusList = ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), IR.getType());
		if(ordiStatusList != null && ordiStatusList.size()>0){
			throw new ServiceException("OMS-RetStatusApply-execute", "退换货订单行状态已存在，不可重复提交");
		}
		return true;
	}
	
}
