package com.meiliwan.emall.oms.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.OrdLog;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.constant.BizCode;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OpteratorType;
import com.meiliwan.emall.oms.dao.OrdLogDao;

/**
 * 订单日志
 * @author rubi
 *
 */
@Repository
public class OrdLogDaoImpl extends BaseDao<Integer, OrdLog> implements
		OrdLogDao {

	
	private static final String LOG_ORDER_CREATE="您提交了订单,请等待系统确认。";
	//private static final String LOG_ORDER_USER_CANCEL=;
	//private static final String LOG_ORDER_SYSTEM_CANCEL=;
	    
	private static final String LOG_APPLY="您的退/换货申请已提交,正在等待相关售后人员审核。";
	    
	    
	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return OrdLogDao.class.getName();
	}


	/**
     * 退换货申请第一步,申请
     */
    public void insertApplyOrdLog(Ordi ordi){
    	OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.RET_APPLY.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent(LOG_APPLY);
    	log.setOpteratorId(GlobalNames.ADMINID_SYS_DEFAULT);
    	log.setOpteratorName(GlobalNames.SESSIONKEY_ADMIN_NAME);
    	log.setOpteratorType(OpteratorType.USER.getCode());
    	
    	setLogCommon(log,ordi);
    	
    }

   
    
    private void setLogCommon(OrdLog log,Ordi ordi){
    	log.setCreateTime(new Date());
    	if(ordi.getUid()!=null){
    		log.setOpteratorId(ordi.getUid());
    	}
    	if(ordi.getUserName()!=null){
    		log.setOpteratorName(ordi.getUserName());
    	}
    	if(log.getOpteratorType().equals(OpteratorType.SYSTEM.getCode())){
    		log.setOpteratorName(OpteratorType.SYSTEM.getDesc());
    	}
    	
    	if(ordi.getOrderId()!=null){
    		log.setOrderId(ordi.getOrderId());
    	}
    	if(ordi.getOrderItemId()!=null){
    		log.setOrderItemId(ordi.getOrderItemId());
    	}
    	if(ordi.getOrderType()!=null){
    		log.setOrderType(ordi.getOrderType());
    	}
    	if(ordi.getOrderItemStatus()!=null){
    		log.setStatusCode(ordi.getOrderItemStatus());
    	}
    	this.insert(log);
    }
    /**
     * 订单创建
     */
	@Override
	public void insertOrderCreate(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    	log.setContent(LOG_ORDER_CREATE);

    	log.setOpteratorType(OpteratorType.USER.getCode());
    	setLogCommon(log,ordi);
    	
	}


	/**
	 * 用户取消
	 */
	@Override
	public void insertOrderUserCancel(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    	log.setContent("您已经取消了订单。");
    	log.setOpteratorType(OpteratorType.USER.getCode());
    	setLogCommon(log,ordi);
    	
	}


	/**
	 * 客服取消
	 */
	@Override
	public void insertOrderAdminCancel(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
	    	log.setBizCode(BizCode.ORD.name());
	    	log.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
	    	log.setContent("您的订单被客服取消。");
	    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
	    	setLogCommon(log,ordi);
    	
	}

	/**
	 * 系统取消
	 */
	@Override
	public void insertOrderSystemCancel(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    	log.setContent("您的订单超时未支付，订单自动取消。");
    	log.setOpteratorType(OpteratorType.SYSTEM.getCode());
    	setLogCommon(log,ordi);
    	
	}


	@Override
	public void insertApplyAgree(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.RET_APPLY.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent("您的退/换货申请已通过审核。请尽快按照上方提供的处理方案进行处理。");
    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
    	setLogCommon(log,ordi);
	}

	@Override
	public void insertApplyAgreeNotBack(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.RET_APPLY.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent("退换货申请通过,买家不需要退货,等待客服发货或者退款。");
    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
    	setLogCommon(log,ordi);
	}
	
	 /**
     * 退换货申请第一步,申请额外的日志
     */
    public void insertApplyExtraOrdLog(Ordi ordi,String content){
    	OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.RET_APPLY.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent(content);
    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
    	
    	setLogCommon(log,ordi);
    	
    }
    
	@Override
	public void insertApplyDisagree(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.RET_APPLY.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent("退换货申请不通过。");
    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
    	setLogCommon(log,ordi);
    	
	}

	@Override
	public void insertOrderPayFinish(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    	log.setContent("订单已支付,等待商品出库。");
    	log.setOpteratorType(OpteratorType.USER.getCode());
    	setLogCommon(log,ordi);
    	
	}
	
	/**
	 * 出库
	 */
	@Override
	public void insertOrderOut(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    	log.setContent("商品已出库,等待商品发货。");
    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
    	setLogCommon(log,ordi);
    	
	}


	@Override
	public void insertOrderShip(Ordi ordi,String transInfo) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    	log.setContent("商品已发货,等待客户收货。 "+transInfo);
    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
    	setLogCommon(log,ordi);
    	
	}


	@Override
	public void insertOrderReceive(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    	log.setContent("商品已发货,等待客户收货。");
    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
    	setLogCommon(log,ordi);
    	
	}


	@Override
	public void insertOrderReject(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    	log.setContent("商品已被拒收,等待客服处理。");
    	log.setOpteratorType(OpteratorType.USER.getCode());
    	setLogCommon(log,ordi);
    	
	}


	/**
	 * 买家已发货
	 */
	@Override
	public void insertRetBuyerDeliver(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent("买家已发货,等待客服收货。");
    	log.setOpteratorType(OpteratorType.USER.getCode());
    	setLogCommon(log,ordi);
    	
	}


	@Override
	public void insertRetSystemCancel(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent("退货货取消,系统自动取消。");
    	log.setOpteratorType(OpteratorType.SYSTEM.getCode());
    	setLogCommon(log,ordi);
    	
	}


	/**
	 * 客服收货
	 */
	@Override
	public void insertRetSellerReceive(Ordi ordi,String content) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent(content);
    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
    	setLogCommon(log,ordi);
    	
	}


	/**
	 * 客服发货
	 */
	@Override
	public void insertRetSellerSend(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent("已寄出换货商品,请注意查收。");
    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
    	setLogCommon(log,ordi);
    	
	}

	/**
	 * 客户收货
	 */
	@Override
	public void insertRetBuyerReceive(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent("您已确认收货,等待财务是否决定退额外的款项。");
    	log.setOpteratorType(OpteratorType.USER.getCode());
    	setLogCommon(log,ordi);
    	
	}


	@Override
	public void insertRetPayFinish(Ordi ordi,String msg) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent(msg);
    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
    	setLogCommon(log,ordi);
    	
	}


	@Override
	public void insertOrderFinish(Ordi ordi,String content) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    	log.setContent(content);
    	log.setOpteratorType(OpteratorType.USER.getCode());
    	setLogCommon(log,ordi);
    	
	}


	@Override
	public void insertRetUserCancel(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent("退换货取消,用户取消。");
    	log.setOpteratorType(OpteratorType.USER.getCode());
    	setLogCommon(log,ordi);
    	
	}


	@Override
	public void insertOrderPayFail(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent("支付失败,请确认账户信息,如有疑问请联系客服。");
    	log.setOpteratorType(OpteratorType.USER.getCode());
    	setLogCommon(log,ordi);
	}


	@Override
	public void insertOrderPayPart(Ordi ordi) {
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent("部分支付,剩余资金尚未支付。");
    	log.setOpteratorType(OpteratorType.USER.getCode());
    	setLogCommon(log,ordi);
	}

    @Override
    public void insertOrderPayPart(Ordi ordi, String content) {
        OrdLog log = new OrdLog();
        log.setBizCode(BizCode.ORD.name());
        log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
        log.setContent(content);
        log.setOpteratorType(OpteratorType.USER.getCode());
        setLogCommon(log,ordi);
    }


    @Override
	public void insertOrderCreateCOD(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
    	log.setContent("您的订单的商品准备出库。");

    	log.setOpteratorType(OpteratorType.SYSTEM.getCode());
    	setLogCommon(log,ordi);
	}


	@Override
	public void insertRetPayFail(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent("系统退款失败,客服正在处理。");
    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
    	setLogCommon(log,ordi);
	}


	@Override
	public void insertRetPayWait(Ordi ordi) {
		// TODO Auto-generated method stub
		OrdLog log = new OrdLog();
    	log.setBizCode(BizCode.ORD.name());
    	log.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
    	log.setContent("等待客服退款。");
    	log.setOpteratorType(OpteratorType.ADMIN.getCode());
    	setLogCommon(log,ordi);
	}


	
	
}
