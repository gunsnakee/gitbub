package com.meiliwan.emall.oms.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.meiliwan.emall.oms.constant.OrdIDeliverStatus;
import com.meiliwan.emall.oms.constant.OrdIRetStatus;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.constant.OrderStatusType;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.BaseTest;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.StatusMachineService;

public class StatusMachineServiceTest extends BaseTest{


    @Autowired
    private StatusMachineService service;
    
    @Test
    public void test() throws ServiceException {
    	
    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
    	//ordStatusDIO.setCheckOrder(true);
    	ordStatusDIO.setAdminId(0);
    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    	ordStatusDIO.setOrderId("1000502");
    	ordStatusDIO.setOrderItemId("100050201");
    	ordStatusDIO.setUid(100);
    	
//    	//提交订单
//    	ordStatusDIO.setStatusType(OrderStatusType.IS);
//    	ordStatusDIO.setStatusCode(OrdITotalStatus.ORDI_COMMITTED.getCode());
////    	
////    	//支付完成
    	//ordStatusDIO.setStatusType(OrderStatusType.IP);
    	//ordStatusDIO.setStatusCode(OrdIPayStatus.PAY_FINISHED.getCode());
////    	
////    	//配送待发货
//    	ordStatusDIO.setStatusType(OrderStatusType.ID);
//    	ordStatusDIO.setStatusCode(OrdIDeliverStatus.DELI_WAIT_CONSIGNMENT.getCode());
//////    	
////    	//配送已发货
//    	ordStatusDIO.setStatusType(OrderStatusType.ID);
//    	ordStatusDIO.setStatusCode(OrdIDeliverStatus.DELI_DELIVER_GOODS.getCode());
////    	
//////    	//配送已收货
//    	ordStatusDIO.setStatusType(OrderStatusType.ID);
//    	ordStatusDIO.setStatusCode(OrdIDeliverStatus.DELI_RECEIPTED.getCode());
//    	
//    	
    	//取消订单
    	ordStatusDIO.setStatusType(OrderStatusType.IS);
    	ordStatusDIO.setStatusCode(OrdITotalStatus.ORDI_CANCEL.getCode());
    	ordStatusDIO.setAdminId(10);
//    	
//    	//取消支付
//    	ordStatusDIO.setAdminId(10);
//    	ordStatusDIO.setUid(0);
//    	ordStatusDIO.setStatusType(OrderStatusType.IP);
//    	ordStatusDIO.setStatusCode(OrdIPayStatus.PAY_CANCEL.getCode());
//    	
//    	//申请退换货
//    	ordStatusDIO.setBillType(GlobalNames.ORDER_BILL_TYPE_REVERSE);
//    	ordStatusDIO.setOrderItemId("98765601");
//    	ordStatusDIO.setOrderId("987656");
//    	ordStatusDIO.setOldOrderItemId("1234501");
//    	
//    	ordStatusDIO.setStatusType(OrderStatusType.IR);
//        ordStatusDIO.setStatusCode(OrdIRetStatus.RET_APPLY.getCode());

//        ordStatusDIO.setAdminId(GlobalNames.ADMINID_SYS_DEFAULT);
//        ordStatusDIO.setUid(0);
//        ordStatusDIO.setOrderType(OrderType.CHANGE.getCode());
//        ordStatusDIO.setReturnReasonType(ReturnReasonType.QUALITY);
//        ordStatusDIO.setStatusType(OrderStatusType.IR);
//        ordStatusDIO.setStatusCode(OrdIRetStatus.RET_APPLY_PASSED.getCode());
//    	
//    	//拒绝退换货
//    	ordStatusDIO.setStatusType(OrderStatusType.IR);
//    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_APPLY_REJECTED.getCode());
////    	
////    	//取消退换货
//    	ordStatusDIO.setStatusType(OrderStatusType.IR);
//    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_CANCEL.getCode());
//    	
//    	//退换待发货
//    	ordStatusDIO.setStatusType(OrderStatusType.IR);
//    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_BUYER_WAIT_CONSIGNMENT.getCode());
////    	
////    	//退换待协商
//    	ordStatusDIO.setStatusType(OrderStatusType.IR);
//    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_WAIT_CONSULT.getCode());
////    	
////    	//退换客服确认收货
//    	ordStatusDIO.setStatusType(OrderStatusType.IR);
//    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_SELLER_RECEIPTED.getCode());
////    	
//    	//逆向-客服已发货 IR ： RET_SELLER_CONSINGMENT
//    	ordStatusDIO.setStatusType(OrderStatusType.IR);
//    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_SELLER_CONSINGMENT.getCode());
////    	
////    	//逆向-买家已收货 IR ： RET_BUYER_RECEIPTED
//    	ordStatusDIO.setStatusType(OrderStatusType.IR);
//    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_BUYER_RECEIPTED.getCode());
////    	
////    	//逆向-退款成功
//    	ordStatusDIO.setStatusType(OrderStatusType.IP);
//    	ordStatusDIO.setStatusCode(OrdIPayStatus.PAY_RETUND_FINISHED.getCode());
////    	
////    	//逆向-退款失败
//    	ordStatusDIO.setStatusType(OrderStatusType.IP);
//    	ordStatusDIO.setStatusCode(OrdIPayStatus.PAY_REFUND_FAILURE.getCode());
    	
        service.updateStatus(null,ordStatusDIO);
        
    }
    
   
    
    /**
     * 主线状态出库
     * @throws ServiceException
     */
    @Test
    public void testORDI_EFFECTIVED() throws ServiceException {
    	
	    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
	    	//ordStatusDIO.setCheckOrder(true);
	    	ordStatusDIO.setAdminId(0);
	    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
	    	ordStatusDIO.setOrderId("000004255935");
	    	
	    	//支付完成
	    	ordStatusDIO.setStatusType(OrderStatusType.IS);
	    	ordStatusDIO.setStatusCode(OrdITotalStatus.ORDI_EFFECTIVED.getCode());
	    	
	    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
		ordStatusDIO.setUid(0);
		ordStatusDIO.setStatusType(OrderStatusType.ID);
		ordStatusDIO.setStatusCode(OrdIDeliverStatus.DELI_WAIT_CONSIGNMENT
                            .getCode());
		
		 service.updateStatus(null,ordStatusDIO);
    }
    
    /**
     * 主线状态发货
     * @throws ServiceException
     */
    @Test
    public void testDELI_DELIVER_GOODS() throws ServiceException {
    	
	    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
	    	//ordStatusDIO.setCheckOrder(true);
	    	ordStatusDIO.setAdminId(0);
	    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
	    	ordStatusDIO.setOrderId("000004255935");
	    	
	    	//支付完成
	    	ordStatusDIO.setStatusType(OrderStatusType.IS);
	    	ordStatusDIO.setStatusCode(OrdITotalStatus.ORDI_EFFECTIVED.getCode());
	    	
	    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
		ordStatusDIO.setUid(0);
		
		ordStatusDIO.setTransportInfo("物流公司:顺风快递 货运单号:123124142314");
		// ordStatusDIO.setCheckOrder(true);

		ordStatusDIO.setStatusType(OrderStatusType.ID);
		ordStatusDIO.setStatusCode(OrdIDeliverStatus.DELI_DELIVER_GOODS
				.getCode());
		 service.updateStatus(null,ordStatusDIO);
    }
    
    
    /**
     * 同意换货,不需要退货
     */
    @Test
    public void testRET_APPLY_PASSED(){
    	    	
    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
    	//ordStatusDIO.setCheckOrder(true);
    	ordStatusDIO.setAdminId(100);
    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	ordStatusDIO.setOrderId("11");
    	ordStatusDIO.setOrderItemId("2538375");
    	ordStatusDIO.setUid(0);
    	
    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_APPLY_PASSED.getCode());
		ordStatusDIO.setBackTransportFee(new BigDecimal(11));
		
    	ordStatusDIO.setStatusType(OrderStatusType.IR);
    	 service.updateStatus(null,ordStatusDIO);
    }
    
    /**
     * 同意退货 ,需要退货,需要为申请状态,ordi,和status表
     */
    @Test
    public void testRET_BUYER_WAIT_CONSIGNMENT(){
    	    	
    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
    	//ordStatusDIO.setCheckOrder(true);
    	ordStatusDIO.setAdminId(100);
    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	ordStatusDIO.setOrderId("11");
    	ordStatusDIO.setOrderItemId("2538375");
    	ordStatusDIO.setUid(0);
    	
    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_BUYER_WAIT_CONSIGNMENT.getCode());
		ordStatusDIO.setBackTransportFee(new BigDecimal(11));
		
    	ordStatusDIO.setStatusType(OrderStatusType.IR);
    	 service.updateStatus(null,ordStatusDIO);
    }
    
    /**
     * 同意换货 ,需要退货,需要为申请状态,ordi,和status表
     */
    @Test
    public void testRET_APPLY_REJECTED(){
    	    	
    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
    	//ordStatusDIO.setCheckOrder(true);
    	ordStatusDIO.setAdminId(100);
    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	ordStatusDIO.setOrderId("11");
    	ordStatusDIO.setOrderItemId("2538375");
    	ordStatusDIO.setUid(0);
    	
    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_APPLY_REJECTED.getCode());
		ordStatusDIO.setBackTransportFee(new BigDecimal(11));
		
    	ordStatusDIO.setStatusType(OrderStatusType.IR);
    	 service.updateStatus(null,ordStatusDIO);
    }
    
    
    /**
     * 等待客服确认收货
     */
    @Test
    public void testRET_BUYER_DELIVER_GOODS(){
    	    	
    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
    	//ordStatusDIO.setCheckOrder(true);
    	ordStatusDIO.setAdminId(100);
    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	ordStatusDIO.setOrderId("11");
    	ordStatusDIO.setOrderItemId("001000054401");
    	ordStatusDIO.setUid(0);
    	
    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_BUYER_DELIVER_GOODS.getCode());
		ordStatusDIO.setBackTransportFee(new BigDecimal(11));
		
    	ordStatusDIO.setStatusType(OrderStatusType.IR);
    	 service.updateStatus(null,ordStatusDIO);
    }
    
    /**
     * RET_CANCEL
     */
    @Test
    public void testRET_CANCEL(){
    	    	
    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
    	//ordStatusDIO.setCheckOrder(true);
    	ordStatusDIO.setAdminId(0);
    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	ordStatusDIO.setOrderId("01130704090011000");
    	ordStatusDIO.setOrderItemId("0113070409001100001");
    	ordStatusDIO.setUid(10);
    		
    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_CANCEL.getCode());
		
    	ordStatusDIO.setStatusType(OrderStatusType.IR);
    	 service.updateStatus(null,ordStatusDIO);
    }
    
    @Test
    public void testORDI_CANCEL(){
    	    	
    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
    	//ordStatusDIO.setCheckOrder(true);
    	ordStatusDIO.setAdminId(0);
    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    	ordStatusDIO.setOrderId("1000500");
    	//ordStatusDIO.setOrderItemId("0113070409001100001");
    	ordStatusDIO.setUid(10);
    		
    	ordStatusDIO.setStatusCode(OrdITotalStatus.ORDI_CANCEL.getCode());
		
    	ordStatusDIO.setStatusType(OrderStatusType.IS);
    	 service.updateStatus(null,ordStatusDIO);
    }
    
    /**
     * RET_SELLER_RECEIPTED
     */
    @Test
    public void testRET_SELLER_RECEIPTED(){
    	    	
    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
    	//ordStatusDIO.setCheckOrder(true);
    	ordStatusDIO.setAdminId(100);
    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	ordStatusDIO.setOrderItemId("001000054401");
    	ordStatusDIO.setUid(0);
    	
    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_SELLER_RECEIPTED.getCode());
		ordStatusDIO.setBackTransportFee(new BigDecimal(11));
		
    	ordStatusDIO.setStatusType(OrderStatusType.IR);
    	 service.updateStatus(null,ordStatusDIO);
    }
    
    
    /**
     * RET_SELLER_RECEIPTED
     */
    @Test
    public void testRET_SELLER_CONSINGMENT(){
    	    	
    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
    	//ordStatusDIO.setCheckOrder(true);
    	ordStatusDIO.setAdminId(100);
    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	ordStatusDIO.setOrderId("11");
    	ordStatusDIO.setOrderItemId("2538375");
    	ordStatusDIO.setUid(0);
    	
    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_SELLER_CONSINGMENT.getCode());
		ordStatusDIO.setBackTransportFee(new BigDecimal(11));
		
    	ordStatusDIO.setStatusType(OrderStatusType.IR);
    	 service.updateStatus(null,ordStatusDIO);
    }
    
    
    /**
     * RET_SELLER_RECEIPTED
     */
    @Test
    public void testRET_BUYER_RECEIVED(){
    	    	
    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
    	//ordStatusDIO.setCheckOrder(true);
    	ordStatusDIO.setAdminId(100);
    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	ordStatusDIO.setOrderItemId("001000054601");
    	ordStatusDIO.setUid(0);
    	
    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_BUYER_RECEIPTED.getCode());
		ordStatusDIO.setBackTransportFee(new BigDecimal(11));
		
    	ordStatusDIO.setStatusType(OrderStatusType.IR);
    	 service.updateStatus(null,ordStatusDIO);
    }
    
    
    /**
     * RET_SELLER_RECEIPTED
     */
    @Test
    public void testRET_REFUND_FAILURE(){
    	    	
    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
    	//ordStatusDIO.setCheckOrder(true);
    	ordStatusDIO.setAdminId(100);
    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	ordStatusDIO.setOrderId("0010000139");
    	ordStatusDIO.setOrderItemId("001000013901");
    	ordStatusDIO.setUid(0);
    	
    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_REFUND_FAILURE.getCode());
		ordStatusDIO.setBackTransportFee(new BigDecimal(11));
		
    	ordStatusDIO.setStatusType(OrderStatusType.IR);
    	 service.updateStatus(null,ordStatusDIO);
    }
    
    
    /**
     * RET_SELLER_RECEIPTED
     */
    @Test
    public void testRET_FINISHED(){
    	    	
    	OrderStatusDTO ordStatusDIO = new OrderStatusDTO();
    	//ordStatusDIO.setCheckOrder(true);
    	ordStatusDIO.setAdminId(100);
    	ordStatusDIO.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	ordStatusDIO.setOrderId("11");
    	ordStatusDIO.setOrderItemId("2538375");
    	ordStatusDIO.setUid(0);
    	
    	ordStatusDIO.setStatusCode(OrdIRetStatus.RET_FINISHED.getCode());
		ordStatusDIO.setBackTransportFee(new BigDecimal(11));
		
    	ordStatusDIO.setStatusType(OrderStatusType.IR);
    	 service.updateStatus(null,ordStatusDIO);
    }
}
