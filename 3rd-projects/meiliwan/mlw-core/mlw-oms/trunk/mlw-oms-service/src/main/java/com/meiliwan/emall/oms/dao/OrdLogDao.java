package com.meiliwan.emall.oms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.oms.bean.OrdLog;
import com.meiliwan.emall.oms.bean.Ordi;

public interface OrdLogDao extends IDao<Integer,OrdLog>{
	

	/**
	 * 逆向
     * 退换货申请第一步,申请
     *
     */
	void insertApplyOrdLog(Ordi ordi);
	/**
	 * 逆向
	 * 退换货申请,审核通过
	 * @param ord
	 */
	void insertApplyAgree(Ordi ordi);
	/**
	 * 逆向
	 * 退换货申请,审核不通过
	 * @param ord
	 */
	void insertApplyDisagree(Ordi ordi);
	/**
	 * 逆向，用户发货
	 * RET_BUYER_DELIVER_GOODS
	 * @param ordi
	 */
	void insertRetBuyerDeliver(Ordi ordi);
	
	/**
	 * 逆向，系统自动取消
	 * RET_BUYER_DELIVER_GOODS
	 * @param ordi
	 */
	void insertRetSystemCancel(Ordi ordi);
	
	/**
	 * 逆向，系统自动取消
	 * RET_BUYER_DELIVER_GOODS
	 * @param ordi
	 */
	void insertRetUserCancel(Ordi ordi);
	
	/**
	 * 逆向，客服已收货
	 * RET_SELLER_RECEIPTED
	 * @param ordi
	 */
	void insertRetSellerReceive(Ordi ordi,String content);
	
	/**
	 * 逆向，客服已发货
	 * RET_SELLER_CONSINGMENT
	 * @param ordi
	 */
	void insertRetSellerSend(Ordi ordi);
	
	/**
	 * 逆向，买家已收货
	 * RET_BUYER_RECEIPTED
	 * @param ordi
	 */
	void insertRetBuyerReceive(Ordi ordi);
	
	/**
	 * 逆向,退款失败
	 * @param ordi
	 */
	void insertRetPayWait(Ordi ordi);
	
	/**
	 * 逆向,退款完成
	 * @param ordi
	 */
	void insertRetPayFinish(Ordi ordi,String msg);
	/**
	 * 逆向,退款失败
	 * @param ordi
	 */
	void insertRetPayFail(Ordi ordi);
	/**
	 * 订单创建第一部，生成订单
	 * @param ord
	 */
	void insertOrderCreate(Ordi ordi);
	
	/**
	 * 订单创建第一部，生成货到付款订单
	 * @param ord
	 */
	void insertOrderCreateCOD(Ordi ordi);
	
	/**
	 * 正向用户取消订单
	 * @param ordi
	 */
	void insertOrderUserCancel(Ordi ordi);
	/**
	 * 正向系统取消订单
	 * @param ordi
	 */
	void insertOrderSystemCancel(Ordi ordi);
	
	/**
	 * 出库
	 * @param ordi
	 */
	void insertOrderPayFinish(Ordi ordi);
	
	/**
	 * 出库
	 * @param ordi
	 */
	void insertOrderOut(Ordi ordi);
	/**
	 * 发货
	 * @param ordi
	 */
	void insertOrderShip(Ordi ordi,String transInfo);
	/**
	 * 等待客户收货
	 * @param ordi
	 */
	void insertOrderReceive(Ordi ordi);
	/**
	 * 拒收
	 * @param ordi
	 */
	void insertOrderReject(Ordi ordi);
	/**
	 * 客户收货,交易完成
	 * @param ordi
	 */
	void insertOrderFinish(Ordi ordi,String content);
	/**
	 * 退款失败
	 * @param ordi
	 */
	void insertOrderPayFail(Ordi ordi);
	/**
	 * 部分支付
	 * @param ordi
	 */
	void insertOrderPayPart(Ordi ordi);

    /**
     * 部分支付，自定义支付描述
     * @param ordi
     * @param content
     */
	void insertOrderPayPart(Ordi ordi,String content);
	/**
	 * 退换货申请通过,买家不需要退货,等待客服退货或者退款
	 * @param ordi
	 */
	void insertApplyAgreeNotBack(Ordi ordi);
	void insertApplyExtraOrdLog(Ordi ordi, String content);
	void insertOrderAdminCancel(Ordi ordi);
}