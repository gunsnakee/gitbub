package com.meiliwan.emall.oms.service.ordstatus.ordi;


import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_CANCEL;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_COMMITTED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;

import java.util.List;

import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.constant.OrderStatusType;

import org.springframework.stereotype.Component;

import com.meiliwan.emall.oms.constant.OrderType;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusChecker;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

/**
 * 订单行状态：订单已取消
 * @author yuxiong
 *
 */
@Component
public class OrdIStatusCancel extends OrderStatusService{

    /**
     * 拿订单行状态数据
     * @return
     */
    @Override
    protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
        //是否有效的订单
        return ordiStatusDao.selectByOrderId(ordStatusDIO.getOrderId(), true);
    }

	@Override
    protected void updateStatus(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException {
        if (ordi.getOrderItemStatus().equals(OrdITotalStatus.ORDI_CANCEL.getCode())) {
            throw new ServiceException("OMS-" + getClass().getSimpleName() + "-checkOrderCancel", "已取消的订单不能再取消。");
        }
        if (ordi.getOrderType().equals(OrderType.REAL_ORDER_COD.getCode())) {
            OrderStatusChecker.checkOrderCancelCOD(ordStatusDIO, unCheckOrder, this.getClass());
        } else {
            OrderStatusChecker.checkOrderCancel(ordStatusDIO, unCheckOrder, this.getClass());
        }
//        if(ordStatusDIO.isAdmin()){
//            ordStatusDIO.setAdminId(GlobalNames.ADMINID_SYS_DEFAULT);
//        }
        //修改状态做已取消
        //ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IS.getType(), ORDI_CANCEL.getCode());
        //更新订单行表的状态、
        updateOrdIAndOrdStatus(ordStatusDIO, ORDI_CANCEL);
        //取消订单，伪删除ID订单行状态
        ordiStatusDao.deleteByOrdIdStatusType(ordStatusDIO.getOrderId(), OrderStatusType.ID.getType(), ordStatusDIO.getAdminId());
        //客户取消
        if (ordStatusDIO.getUid() > 0) {
            ordLogDao.insertOrderUserCancel(ordi);
        }
        //管理员取消
        if (ordStatusDIO.getAdminId() > 0) {
            ordi.setUid(ordStatusDIO.getAdminId());
            ordi.setUserName(ordStatusDIO.getAdminName());
            if (ordStatusDIO.getAdminId() == GlobalNames.ADMINID_SYS_DEFAULT) {
                ordLogDao.insertOrderSystemCancel(ordi);
            } else {
                ordLogDao.insertOrderAdminCancel(ordi);
            }
        }
    }


	/**
	 * 订单行状态校验
	 * @param ordStatusDIO
	 * @throws ServiceException
	 */
	@Override
	protected boolean checkOrderStatus(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder) throws ServiceException{

		return true;
	}

}
