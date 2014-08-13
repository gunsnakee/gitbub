package com.meiliwan.emall.async.order;

import com.meiliwan.emall.oms.constant.OrdIRetStatus;
import com.meiliwan.emall.oms.constant.OrderStatusType;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskWorker;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;



/**
 * Date: 13-7-5
 * Time: 下午4:51
 * 定时取消退货货
 */
public class RetOrderCancelTaskWorker implements MsgTaskWorker {

    public static final MLWLogger  log= MLWLoggerFactory.getLogger(RetOrderCancelTaskWorker.class);

    /**
     * 退换货自动取消
     */
    @Override
    public void handleMsg(String msg) {
        try {
            if(StringUtil.checkNull(msg)){
            		return ;
            }
            //http://admin.meiliwan.com/oms/service/detailChg?retordItemId=000000010701
            Ordi ordi = OrdClient.getOrdiByOrderItemId(msg);
            if(ordi==null) return ;
            	
            //等待买家退货
            if(ordi.getOrderItemStatus().equals(OrdIRetStatus.RET_BUYER_WAIT_CONSIGNMENT.getCode())){
            	
	            	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
	            	ordStatusDIO.setAdminId(GlobalNames.ADMINID_SYS_DEFAULT);
	            	ordStatusDIO.setBillType(com.meiliwan.emall.oms.constant.Constant.ORDER_BILL_TYPE_REVERSE);
	            	ordStatusDIO.setOrderItemId(msg);
	            	ordStatusDIO.setOrderId(ordi.getOrderId());
	            	ordStatusDIO.setUid(0);
	            	
	            	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_CANCEL.getCode());
	            	
	            	ordStatusDIO.setStatusType(OrderStatusType.IR);
	            	OrdClient.updateStatus(ordStatusDIO);
            	
            }
        } catch (Exception e) {
            log.error(e,msg,null);
        }
    }

    public static void main(String[] args) {
    	RetOrderCancelTaskWorker ww = new RetOrderCancelTaskWorker();
    	ww.handleMsg("000000040201");
    	
	}
}
