package com.meiliwan.emall.async.order;

import java.util.ArrayList;
import java.util.List;

import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskWorker;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.dto.OrdDetailDTO;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.pay.client.PayClient;
import com.meiliwan.emall.sp2.client.SpTicketClient;
import com.meiliwan.emall.stock.bean.OrderItem;
import com.meiliwan.emall.stock.bean.StockItem;
import com.meiliwan.emall.stock.client.ProStockClient;

import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_CANCEL;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;


/**
 * User: guangdetang
 * Date: 13-7-5
 * Time: 下午4:51
 * 定时取消订单
 */
public class OrderCancelTaskWorker implements MsgTaskWorker {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    @Override
    public void handleMsg(String msg) {
        try {
            if (msg != null) {
                OrdDetailDTO order = OrdClient.getDetail(msg);
                if (order != null && order.getOrd() != null && order.getOrd().getOrderStatus().equals(OrdITotalStatus.ORDI_COMMITTED.getCode())) {
                		//回滚库存
                		cancelStock(order.getOrdiList());
                	
                		OrderStatusDTO statusDTO = new OrderStatusDTO();
                    statusDTO.setOrderId(order.getOrd().getOrderId());
                    statusDTO.setStatusCode(ORDI_CANCEL.getCode());
                    statusDTO.setStatusType(IS);
                    statusDTO.setBillType(order.getOrd().getBillType());
                    statusDTO.setAdminId(GlobalNames.ADMINID_SYS_DEFAULT);
                    OrdClient.updateStatus(statusDTO);
                    //如果部分已支付，则退款那部分
                    Boolean success = PayClient.backMoneyForCancel(msg, "", GlobalNames.ADMINID_SYS_DEFAULT);
                    if (!success) {
                        logger.warn("系统自动取消订单退款失败", "orderId(" + msg + ")", null);
                    }
                    //---------- lzl cancel ticket 0609 -----------------------------
                    if (success) {
                    	SpTicketClient.cancelSpTicketOnOrder(msg);
                    }
                    //---------- lzl cancel ticket 0609 -----------------------------
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e, "系统自动取消订单遇到异常, orderId(" + msg + ")", null);
        }
    }
    
    private boolean cancelStock(List<Ordi> ordis) {
        List<StockItem> items = new ArrayList<StockItem>();
        int uid = 0;
        for (Ordi ordi : ordis) {
            uid = ordi.getUid();
            StockItem si = new StockItem();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(ordi.getOrderId());
            orderItem.setOrderItemId(ordi.getOrderItemId());
            orderItem.setStatus(ordi.getSubStockFlag().shortValue() == GlobalNames.STATE_VALID);
            si.setOrderItem(orderItem);
            si.setBuyNum(ordi.getSaleNum());
            si.setProId(ordi.getProId());
            items.add(si);
        }
        return ProStockClient.stockUpdateOnOrderCancel(items, uid, GlobalNames.USER_ADMIN);
    }
}
