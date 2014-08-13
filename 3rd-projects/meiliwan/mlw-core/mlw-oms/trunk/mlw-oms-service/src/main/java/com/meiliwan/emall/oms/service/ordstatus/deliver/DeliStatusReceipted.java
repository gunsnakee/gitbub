package com.meiliwan.emall.oms.service.ordstatus.deliver;


import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_DELIVER_GOODS;
import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_RECEIPTED;
import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_WAIT;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_COMMITTED;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_CONSINGMENT;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_FINISHED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.ID;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IP;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.meiliwan.emall.oms.bean.Ordi;
import org.springframework.stereotype.Component;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusChecker;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：已收货
 * @author yuxiong
 *
 */
@Component
public class DeliStatusReceipted extends OrderStatusService{

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
        List<OrdiStatus> unCheckIS = new ArrayList<OrdiStatus>(), unCheckID = new ArrayList<OrdiStatus>();
        if(unCheckOrder!=null){
            for(OrdiStatus ordiStatus : unCheckOrder){
                //校验 IP.getType(), PAY_WAIT.getCode()
                if(ID.getType().equals(ordiStatus.getStatusType()) && DELI_DELIVER_GOODS.getCode().equals(ordiStatus.getStatusCode())){
                    unCheckID.add(ordiStatus);
                }
                //校验 IS.getType(), ORDI_COMMITTED.getCode()
                else if(IS.getType().equals(ordiStatus.getStatusType()) && ORDI_CONSINGMENT.getCode().equals(ordiStatus.getStatusCode())){
                    unCheckIS.add(ordiStatus);
                }
            }
        }
		OrderStatusChecker.require(ordStatusDIO, unCheckIS, this.getClass());
        OrderStatusChecker.require(ordStatusDIO, unCheckID, this.getClass());
		return true;
	}

	@Override
	protected void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException {
		//修改状态做已收货
		ordiStatusDao.updateAllOrdIStatus(ordStatusDIO.getOrderId(), ID.getType(), DELI_RECEIPTED.getCode());

        //修改状态做已完成
        ordiStatusDao.updateAllOrdIStatus(ordStatusDIO.getOrderId(), IS.getType(), ORDI_FINISHED.getCode());
		
		//更新订单行表的状态:已完成
		ordiDao.updateAllOrdIStatus(ordStatusDIO.getOrderId(), ORDI_FINISHED.getCode(),new Date());
        ordDao.updateOrdStatus(ordStatusDIO.getOrderId(), ORDI_FINISHED.getCode());
	}

	
}
