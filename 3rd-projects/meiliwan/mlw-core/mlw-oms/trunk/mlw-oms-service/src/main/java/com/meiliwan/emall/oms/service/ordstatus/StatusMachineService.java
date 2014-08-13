package com.meiliwan.emall.oms.service.ordstatus;

import static com.meiliwan.emall.oms.constant.OrderStatusCategory.DELIVER;
import static com.meiliwan.emall.oms.constant.OrderStatusCategory.ORDI;
import static com.meiliwan.emall.oms.constant.OrderStatusCategory.PAY;
import static com.meiliwan.emall.oms.constant.OrderStatusCategory.RET;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.oms.constant.OrderStatusType;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.rabbitmq.MqModel;
import com.meiliwan.emall.commons.rabbitmq.MsgSender;
import com.meiliwan.emall.commons.rabbitmq.asyncmsg.AsyncMsg;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.deliver.DeliStatusDeliverGoods;
import com.meiliwan.emall.oms.service.ordstatus.deliver.DeliStatusReceipted;
import com.meiliwan.emall.oms.service.ordstatus.deliver.DeliStatusRejected;
import com.meiliwan.emall.oms.service.ordstatus.deliver.DeliStatusWaitConsigment;
import com.meiliwan.emall.oms.service.ordstatus.ordi.OrdIStatusCancel;
import com.meiliwan.emall.oms.service.ordstatus.ordi.OrdIStatusSubmited;
import com.meiliwan.emall.oms.service.ordstatus.ordipay.OrdIPayStatusCancel;
import com.meiliwan.emall.oms.service.ordstatus.ordipay.OrdIPayStatusFailure;
import com.meiliwan.emall.oms.service.ordstatus.ordipay.OrdIPayStatusFinished;
import com.meiliwan.emall.oms.service.ordstatus.ordipay.OrdIPayStatusPartial;
import com.meiliwan.emall.oms.service.ordstatus.ordipay.OrdIPayStatusRefundFailure;
import com.meiliwan.emall.oms.service.ordstatus.ordipay.OrdIPayStatusRefundFinished;
import com.meiliwan.emall.oms.service.ordstatus.ret.RetStatusApply;
import com.meiliwan.emall.oms.service.ordstatus.ret.RetStatusApplyPassed;
import com.meiliwan.emall.oms.service.ordstatus.ret.RetStatusApplyRejected;
import com.meiliwan.emall.oms.service.ordstatus.ret.RetStatusBuyerDeliverGoods;
import com.meiliwan.emall.oms.service.ordstatus.ret.RetStatusBuyerReceipted;
import com.meiliwan.emall.oms.service.ordstatus.ret.RetStatusBuyerWaitConsignment;
import com.meiliwan.emall.oms.service.ordstatus.ret.RetStatusCancel;
import com.meiliwan.emall.oms.service.ordstatus.ret.RetStatusSellerConsingment;
import com.meiliwan.emall.oms.service.ordstatus.ret.RetStatusSellerReceipted;
import com.meiliwan.emall.oms.service.ordstatus.ret.RetStatusWaitConsult;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;


/**
 * 订单状态机
 * @author yuxiong
 * 
 */
@Service
public class StatusMachineService  extends DefaultBaseServiceImpl {
	private static final MLWLogger logger = MLWLoggerFactory.getLogger(StatusMachineService.class) ;
	// -------------------------订单行状态----------------------------//
	@Autowired
    private OrdIStatusSubmited ordIStatusSubmited;

//	@Autowired
//    private OrdIPayStatusWait ordIPayStatusPayWait;
//	@Autowired
//    private OrdIStatusReceipted ordIStatusReceipted;
//	@Autowired
//    private OrdIStatusFinished ordIStatusFinished;
	@Autowired
    private OrdIStatusCancel ordIStatusCancel; 
//	@Autowired
//    private OrdIStatusEffectived ordIStatusEffectived;
//	@Autowired
//    private OrdIStatusConsingment ordIStatusConsingment;
	// -------------------------订单行状态----------------------------//
	
	// -------------------------订单行配送状态----------------------------//
	@Autowired
    private DeliStatusWaitConsigment deliStatusWaitConsigment;
	@Autowired
    private DeliStatusDeliverGoods deliStatusDeliverGoods;
//	@Autowired
//    private DeliStatusWaitOutRepository deliStatusWaitOutRepository;
	@Autowired
    private DeliStatusRejected deliStatusRejected;
	@Autowired
    private DeliStatusReceipted deliStatusReceipted;
	// -------------------------订单行配送状态----------------------------//
	
	// -------------------------订单行支付状态----------------------------//
	@Autowired
    private OrdIPayStatusFinished ordIPayStatusFinished;
	@Autowired
    private OrdIPayStatusCancel ordIPayStatusCancel;
    @Autowired
    private OrdIPayStatusFailure ordIPayStatusFailure;
    @Autowired
    private OrdIPayStatusPartial ordIPayStatusPartial;

	@Autowired
    private OrdIPayStatusRefundFailure ordIPayStatusRefundFailure;
	@Autowired
    private OrdIPayStatusRefundFinished ordIPayStatusRefundFinished;
	// -------------------------订单行支付状态----------------------------//
	
	// -------------------------逆向退换货状态----------------------------//
	@Autowired
	private RetStatusApply retStatusApply;
	@Autowired
	private RetStatusApplyRejected retStatusApplyRejected;
	@Autowired
	private RetStatusCancel retStatusCancel;
	@Autowired
	private RetStatusBuyerWaitConsignment retStatusBuyerWaitConsignment;
	@Autowired
	private RetStatusBuyerDeliverGoods retStatusBuyerDeliverGoods;
	@Autowired
	private RetStatusWaitConsult retStatusWaitConsult;
	@Autowired
	private RetStatusSellerReceipted retStatusSellerReceipted;
	@Autowired
	private RetStatusSellerConsingment retStatusSellerConsingment;
//	@Autowired
//	private RetStatusFinished retStatusFinished;
//	@Autowired
//	private RetStatusRefundFailure retStatusRefundFailure;
	@Autowired
	private RetStatusBuyerReceipted retStatusBuyerReceipted;
//	@Autowired
//	private OrdIPayStatusRefundWait ordIPayStatusRefundWait;
	// -------------------------逆向退换货状态----------------------------//
	@Autowired
	private RetStatusApplyPassed retStatusApplyPassed;

    /**
     * 状态机的执行
     * @param ordStatusDIO
     */
    public void updateStatus(JsonObject resultObj,OrderStatusDTO ordStatusDIO) throws ServiceException{
    	//校验订单是否有效
    	//isValidOrder(ordStatusDIO);
    	
       	OrderStatusType statusType = ordStatusDIO.getStatusType();
    	String statusCode = ordStatusDIO.getStatusCode();
    	
    	
	    //switch订单类型
	    switch (statusType) {
	    	case IS:
	    		//订单行状态code
	    		switch (ORDI.getOrdIStatus(statusCode)) {
	    			// ---------------------------------提交订单 START ----------------------------------//
	    			//1、提交订单 IS ： ORDI_COMMITTED
	    			//2、待付款 IP ： PAY_WAIT
//	    			case ORDI_COMMITTED:
//	    				ordIStatusSubmited.updateStatus(ordStatusDIO);
//	    				break;
	    			// ---------------------------------提交订单 END ----------------------------------//	
	    			
	    			
		    		//---------------------------------取消订单 START ----------------------------------//
			    	case ORDI_CANCEL:
			    		ordIStatusCancel.updateStatus(ordStatusDIO);
			    		break;
			    	// ---------------------------------取消订单 END ----------------------------------//		
	    		}
	    		break;
	    	case ID:
	    		//订单行配送状态code
	    		switch (DELIVER.getDeliverStatus(statusCode)) {
	    			// ---------------------------------待发货 START ----------------------------------//
	    			case DELI_WAIT_CONSIGNMENT:
	    				deliStatusWaitConsigment.updateStatus(ordStatusDIO);
	    				break;
	    			// ---------------------------------待发货 END ----------------------------------//
	    			
	    			// ---------------------------------已发货 START ----------------------------------//
	    			//1、配送已发货 ID ： DELI_DELIVER_GOODS
	    			//2、订单行状态已发货 IS ： ORDI_CONSINGMENT
	    			case DELI_DELIVER_GOODS:
	    				deliStatusDeliverGoods.updateStatus(ordStatusDIO);
	    				//ordIStatusConsingment.updateStatus(ordStatusDIO);
	    				//发送消息，定期未签收则自动签收订单  lzl add 0523
	    				sendMsgToAppraiseOrder(ordStatusDIO.getOrderId()) ;
	    				break;
	    			// ---------------------------------已发货 END ----------------------------------//	
	    				
	    			// ---------------------------------确认收货 START ----------------------------------//
			    	//1、确认收货 ID ： DELI_RECEIPTED
	    			//	2、对于货到付款来说，需增加一个管理后台确认收款的步骤
			    	//2、已完成 IS ： ORDI_FINISHED
			    	case DELI_RECEIPTED:
			    		deliStatusReceipted.updateStatus(ordStatusDIO);
			    	//	ordIStatusFinished.updateStatus(ordStatusDIO);
			    		//签收发送消息，超时自动置好评 lzl add 0523
			    		sendMsgToReceivedOrder(ordStatusDIO.getOrderId()) ;
			    		break;
			    	// ---------------------------------确认收货 END ----------------------------------//
			    				
	    			// ---------------------------------拒绝收货 START ----------------------------------//
	    			//1、拒绝收货 ID: DELI_REJECTED
	    			//2、取消订单 IS: ORDI_CANCEL，需特别注意，这里得留意记录是因为用户的拒绝收货而导致的取消订单
			    	//这里需求上做了调整，拒绝收货的话，是没有自动取消订单的，add by yuxiong 2013.6.7
	    			case DELI_REJECTED:
	    				deliStatusRejected.updateStatus(ordStatusDIO);
	    				//ordIStatusCancel.updateStatus(ordStatusDIO);
	    				break;
	    			// ---------------------------------拒绝收货 END ----------------------------------//	
	    		}
	    		break;
	    	case IP:
	    		//订单行支付订单code
	    		switch (PAY.getPayStatus(statusCode)) {
		    		// ---------------------------------已支付 START ----------------------------------//
	    			//1、已支付 IP ： PAY_FINISHED
	    			//2、订单生效 IS ： ORDI_EFFECTIVED
	    			//3、等待出库 ID ： DELI_WAIT_OUT_REPOSITORY
	    			case PAY_FINISHED:
	    				ordIPayStatusFinished.updateStatus(ordStatusDIO);
	    				//ordIStatusEffectived.updateStatus(ordStatusDIO);
	    				//deliStatusWaitOutRepository.updateStatus(ordStatusDIO);
	    				break;
	    			// ---------------------------------已支付 END ----------------------------------//	

                    // ---------------------------------支付失败 START ----------------------------------//
                    case PAY_FAILURE:
                        ordIPayStatusFailure.updateStatus(ordStatusDIO);
                        break;
                    // ---------------------------------支付失败 END ----------------------------------//

                    // ---------------------------------部分已支付 START ----------------------------------//
                    case PAY_PARTIAL:
                        ordIPayStatusPartial.updateStatus(ordStatusDIO);
                        break;
                    // ---------------------------------部分已支付 END ----------------------------------//

	    			// ---------------------------------取消支付 START ----------------------------------//
		    		//1、取消付款 IP ： PAY_CANCEL
		    		//2、订单取消 IS ： ORDI_CANCEL，如果是取消支付的情况去取消订单，则以系统默认管理员身份去取消
		    		case PAY_CANCEL:
		    			ordIPayStatusCancel.updateStatus(ordStatusDIO);
		    	//		//如果是取消支付的情况去取消订单，则以系统默认管理员身份去取消
		    	//		ordStatusDIO.setAdminId(GlobalNames.ADMINID_SYS_DEFAULT);
		    	//		ordIStatusCancel.updateStatus(ordStatusDIO);
		    			break;
		    		// ---------------------------------取消支付 END ----------------------------------//
		    				
		    		//---------------------------------逆向-退款失败 START ---------------------------------//
//		    		case PAY_REFUND_WAIT:
//		    			ordIPayStatusRefundWait.updateStatus(ordStatusDIO);
			    	//	retStatusRefundFailure.updateStatus(ordStatusDIO);
			    		//break;
			    	//1、逆向-退款失败 IP ： PAY_REFUND_FAILURE
			    	//2、逆向-退款失败 IR ： RET_REFUND_FAILURE
			    	case PAY_REFUND_FAILURE:
			    		ordIPayStatusRefundFailure.updateStatus(ordStatusDIO);
			    	//	retStatusRefundFailure.updateStatus(ordStatusDIO);
			    		break;
			    	// ---------------------------------逆向-退款失败 END ----------------------------------//		
			    		
			    	//---------------------------------逆向-退款成功 START ---------------------------------//
				    //1、逆向-退款成功 IP ： PAY_RETUND_FINISHED
				    //2、逆向-退换货已完成 IR ： RET_FINISHED
				    case PAY_RETUND_FINISHED:
				    	ordIPayStatusRefundFinished.updateStatus(ordStatusDIO);
				   // 	retStatusFinished.updateStatus(ordStatusDIO);
				    	break;
				    // ---------------------------------逆向-退款成功 END ----------------------------------//
	    		}
	    		break;
	    	case IR:
	    		//逆向订单状态
	    		switch (RET.getRetStatus(statusCode)) {
		    		// ---------------------------------退换货申请 START ----------------------------------//
//	    			//1、退换货申请 IR ： RET_APPLY
//	    			case RET_APPLY:
//	    				retStatusApply.updateStatus(ordStatusDIO);
//	    				break;
	    			// ---------------------------------退换货申请 END ----------------------------------//	
	    					
	    			// ---------------------------------拒绝退换货 START ----------------------------------//
		    		//1、拒绝退换货 IR ： RET_APPLY_REJECTED
		    		case RET_APPLY_REJECTED:
		    			retStatusApplyRejected.updateStatus(ordStatusDIO);
		    			break;
		    		//申请通过
		    		case RET_APPLY_PASSED:
	    				retStatusApplyPassed.updateStatus(ordStatusDIO);
	    			break;
		    		// ---------------------------------拒绝退换货 END ----------------------------------//	
		    				
		    		// ---------------------------------取消退换货 START ----------------------------------//
			    	//1、取消退换货 IR ： RET_CANCEL
			    	case RET_CANCEL:
			    		retStatusCancel.updateStatus(ordStatusDIO);
			    		break;
			    	// ---------------------------------取消退换货 END ----------------------------------//
			    		
			    	// ---------------------------------逆向-等待买家发货 START ----------------------------------//
					//1、逆向-等待买家发货 IR ： RET_BUYER_WAIT_CONSIGNMENT
					case RET_BUYER_WAIT_CONSIGNMENT:
					    retStatusBuyerWaitConsignment.updateStatus(ordStatusDIO);
					    break;
					// ---------------------------------逆向-等待买家发货 END ----------------------------------//
					    		
			    	// ---------------------------------逆向-买家已发货 START ----------------------------------//
				    //1、逆向-买家已发货 IR ： RET_BUYER_DELIVER_GOODS
				    case RET_BUYER_DELIVER_GOODS:
				    	retStatusBuyerDeliverGoods.updateStatus(ordStatusDIO);
				    	break;
				    // ---------------------------------逆向-买家已发货 END ----------------------------------//
				    		
				    // ---------------------------------逆向-待协商 START ----------------------------------//
					//1、逆向-待协商 IR ： RET_WAIT_CONSULT
					case RET_WAIT_CONSULT:
					    retStatusWaitConsult.updateStatus(ordStatusDIO);
					    break;
					// ---------------------------------逆向-待协商 END ----------------------------------//
					
					// ---------------------------------逆向-客服确认收货 START ----------------------------------//
					//1、逆向-客服确认收货 IR ： RET_SELLER_RECEIPTED
					//2、在这里做判断，如果是退货流程，则直接走退款状态，逆向-待退款，IP: PAY_REFUND_WAIT
					case RET_SELLER_RECEIPTED:
						retStatusSellerReceipted.updateStatus(ordStatusDIO);
						break;
					// ---------------------------------逆向-客服确认收货 END ----------------------------------//
						    
					// ---------------------------------逆向-客服已发货 START ----------------------------------//
					//1、逆向-客服已发货 IR ： RET_SELLER_CONSINGMENT
					case RET_SELLER_CONSINGMENT:
						retStatusSellerConsingment.updateStatus(ordStatusDIO);
						break;
					// ---------------------------------逆向-客服已发货 END ----------------------------------//
						
					// ---------------------------------逆向-买家确认收货 START ----------------------------------//
					//1、逆向-买家确认收货 IR ： RET_BUYER_RECEIPTED
					//2、逆向-待退款，IP: PAY_REFUND_WAIT
					case RET_BUYER_RECEIPTED:
						retStatusBuyerReceipted.updateStatus(ordStatusDIO);
		//				ordIPayStatusRefundWait.updateStatus(ordStatusDIO);
						break;
					// ---------------------------------逆向-买家确认收货 END ----------------------------------//
							    
					// ---------------------------------逆向-退换货已完成 START ----------------------------------//
//					//1、逆向-退换货已完成 IR ： RET_FINISHED
//					case RET_FINISHED:
//						retStatusFinished.updateStatus(ordStatusDIO);
//						break;
					// ---------------------------------逆向-退换货已完成 END ----------------------------------//
					
	    		}
	    		break;
	    	case OS:
	    		break;
	    	case OP:
	    		break;		
	    }

    }
    
    /**
     * 发送消息，定期未评价则自动置好评订单
     */
    public static void sendMsgToReceivedOrder(String oid) {
    	try{
    		String time = ConfigOnZk.getInstance().getValue("web/system.properties", "AUTO_APPRAISE_ORDER_TIME");//分钟
    		long times = new Date().getTime() + Integer.parseInt(time) * 60000;
            MsgSender.delaySend(new AsyncMsg.AsyncMsgBuilder(MqModel.ORDER, MqModel.ORDER, "appraiseOrder")
                    .setMsg(oid, "orderId")
                    .setTime(new Date(times))
                    .build());
            
    	}catch (Exception e) {
			logger.error(e, "ConfigOnZk.getInstance().getValue erro ： web/system.properties AUTO_APPRAISE_ORDER_TIME ", null);
		}
    }
    
    /**
     * 发送消息，定期未签收则自动签收订单
     */
    public static void sendMsgToAppraiseOrder(String oid) {
    	try{
    		String time = ConfigOnZk.getInstance().getValue("web/system.properties", "AUTO_RECEIVED_ORDER_TIME");//分钟
    		long times = new Date().getTime() + Integer.parseInt(time) * 60000;
            MsgSender.delaySend(new AsyncMsg.AsyncMsgBuilder(MqModel.ORDER, MqModel.ORDER, "receivedOrder")
                    .setMsg(oid, "orderId")
                    .setTime(new Date(times))
                    .build());
            
    	}catch (Exception e) {
			logger.error(e, "ConfigOnZk.getInstance().getValue erro ： web/system.properties AUTO_RECEIVED_ORDER_TIME ", null);
		}
    }
    
    
    public static void main(String[] args){
//    	System.out.println(GlobalNames.ORDER_RET_TYPE.REFUND);
//    	OrderStatusDIO ordi = new OrderStatusDIO();
//    	ordi.setOrderId("123");
//    	ordi.setOrderItemId("12345");
//    	StatusMachine sm = new StatusMachine();
//    	sm.updateStatus(ordi);
//    	
    }
    
}
