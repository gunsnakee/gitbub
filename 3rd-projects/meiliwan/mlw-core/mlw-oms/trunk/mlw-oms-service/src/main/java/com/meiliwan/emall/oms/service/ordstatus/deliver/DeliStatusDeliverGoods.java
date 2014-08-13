package com.meiliwan.emall.oms.service.ordstatus.deliver;


import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_DELIVER_GOODS;
import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_WAIT_CONSIGNMENT;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_CONSINGMENT;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_EFFECTIVED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.*;

import java.util.ArrayList;
import java.util.List;

import com.meiliwan.emall.oms.bean.Ordi;
import org.springframework.stereotype.Component;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusChecker;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：已发货
 * @author yuxiong
 *
 */
@Component
public class DeliStatusDeliverGoods extends OrderStatusService{

	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatusDao.selectByOrderId(ordStatusDIO.getOrderId(), new String[]{ID.getType(), IS.getType()}, true);
	}
	
	
	
	@Override
	protected boolean checkOrderStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder) throws ServiceException {

        /**
         * 校验订单的前置状态
         */
        List<OrdiStatus> unCheckIs = new ArrayList<OrdiStatus>(), unCheckId = new ArrayList<OrdiStatus>();
        if(unCheckOrder!=null){
            for(OrdiStatus ordiStatus : unCheckOrder){
                //校验 ID.getType(), DELI_WAIT_CONSIGNMENT.getCode()
                if(ID.getType().equals(ordiStatus.getStatusType()) && DELI_WAIT_CONSIGNMENT.getCode().equals(ordiStatus.getStatusCode())){
                    unCheckId.add(ordiStatus);
                }
                //校验 IS.getType(), ORDI_EFFECTIVED.getCode()
                else if(IS.getType().equals(ordiStatus.getStatusType()) && ORDI_EFFECTIVED.getCode().equals(ordiStatus.getStatusCode())){
                    unCheckIs.add(ordiStatus);
                }
            }
        }
        //校验 ID.getType(), DELI_WAIT_CONSIGNMENT.getCode()
		OrderStatusChecker.isValidOrdIStatus(ordStatusDIO, unCheckId, this.getClass());
        //IS.getType(), ORDI_EFFECTIVED.getCode()
        OrderStatusChecker.isValidOrdIStatus(ordStatusDIO, unCheckIs, this.getClass());
		return true;
	}



	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		
		//修改状态做已发货
		ordiStatusDao.updateAllOrdIStatus(ordStatusDIO.getOrderId(), ID.getType(), DELI_DELIVER_GOODS.getCode());

        //修改状态做已发货
        ordiStatusDao.updateAllOrdIStatus(ordStatusDIO.getOrderId(), IS.getType(), ORDI_CONSINGMENT.getCode());

		//更新订单行表的状态:已发货
		ordiDao.updateAllOrdIStatus(ordStatusDIO.getOrderId(), ORDI_CONSINGMENT.getCode());

        //更新订单表的状态:已发货
        ordDao.updateOrdStatus(ordStatusDIO.getOrderId(), ORDI_CONSINGMENT.getCode());
	}

}
