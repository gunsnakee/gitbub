package com.meiliwan.emall.oms.service.ordstatus.ordi;


import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_COMMITTED;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_CONSINGMENT;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_FINISHED;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_RECEIPTED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;

import java.util.List;

import org.springframework.stereotype.Component;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusChecker;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：订单已收货
 * @author yuxiong
 *
 */
@Component
public class OrdIStatusReceipted extends OrderStatusService{


	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), 
				IS.getType(), ORDI_CONSINGMENT.getCode()));
	}
	
	@Override
	protected void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException {
		
		
		//修改状态做已生效
		ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IS.getType(), ORDI_RECEIPTED.getCode());
		
		//更新订单行表的状态
		updateOrdIAndOrdStatus(ordStatusDIO, ORDI_RECEIPTED);
	}
	
}
