package com.meiliwan.emall.oms.service.ordstatus.ret;


import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_APPLY;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_APPLY_REJECTED;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_BUYER_DELIVER_GOODS;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_BUYER_WAIT_CONSIGNMENT;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IR;

import java.util.List;

import com.meiliwan.emall.oms.bean.Ordi;
import org.springframework.stereotype.Component;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：逆向买家已发货
 * @author yuxiong
 *
 */
@Component
public class RetStatusBuyerDeliverGoods extends OrderStatusService{

	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), 
				IR.getType(), RET_BUYER_WAIT_CONSIGNMENT.getCode(), true));
	}
	
	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		
		checkOrderStatus(ordStatusDIO, unCheckOrder);
							
		//修改状态做逆向买家已发货
		ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IR.getType(), RET_BUYER_DELIVER_GOODS.getCode());
		//更新订单行表的状态
		//ordiDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), RET_APPLY_REJECTED.getCode());
		updateOrdIAndOrdStatus(ordStatusDIO, RET_BUYER_DELIVER_GOODS);
	}

}
