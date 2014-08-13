package com.meiliwan.emall.oms.service.ordstatus;

import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_FREEZE;
import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_WAIT;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_BUYER_RECEIPTED;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_SELLER_RECEIPTED;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.RET_WAIT_CONSULT;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_CANCEL;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_COMMITTED;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.ORDI_FINISHED;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IP;
import static com.meiliwan.emall.oms.constant.OrderStatusType.ID;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IR;
import static com.meiliwan.emall.oms.constant.OrderStatusType.IS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdIDeliverStatus;
import com.meiliwan.emall.oms.constant.OrdIPayStatus;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;

public class OrderStatusChecker {

	/**
	 * 校验订单状态必须为空
	 * @param ordStatusDIO
	 * @param unCheckOrder
	 * @param clazz
	 * @return
	 * @throws ServiceException
	 */
	public static boolean isNull(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder, Class clazz) 
			throws ServiceException {
		if(unCheckOrder != null && unCheckOrder.size()>0){
			throw new ServiceException("OMS-"+clazz.getSimpleName()+"-isNull", "订单行状态已存在，不可重复提交");
		}
		return true;
	}
	
	/**
	 * 校验订单行状态非空
	 * @param ordStatusDIO
	 * @param unCheckOrder
	 * @param clazz
	 * @return
	 * @throws ServiceException
	 */
	public static boolean require(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder, Class clazz) 
			throws ServiceException {
		
		if(unCheckOrder == null || unCheckOrder.size() ==0){
			throw new ServiceException("OMS-"+clazz.getSimpleName()+"-require", "无效的订单行状态");
		}
		return true;
	}
	
	/**
	 * 校验订单状态待支付
	 * @param ordStatusDIO
	 * @param unCheckOrder
	 * @param clazz
	 * @return
	 * @throws ServiceException
	 */
	public static boolean checkPayWait(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder, Class clazz)
			throws ServiceException {
		if(unCheckOrder != null){
			//循环遍历这个订单行状态结果集
			for(OrdiStatus ordiStatus : unCheckOrder){
				if(ordiStatus.getStatusType().equals(IP.getType())){
					throw new ServiceException("OMS-"+clazz.getSimpleName()+"-checkPayWait", "订单行状态已存在，不可重复提交");
				}else if(ordiStatus.getStatusType().equals(IS.getType())
						&& !ordiStatus.getStatusCode().equals(ORDI_COMMITTED.getCode())){
					throw new ServiceException("OMS-"+clazz.getSimpleName()+"-checkPayWait", "订单行状态不等于已提交，不可写入待付款");
				}
			}
		}
		return true;
	}
	
	/**
	 * 校验是否是有效的一条订单行状态
	 * @param ordStatusDIO
	 * @param unCheckOrder
	 * @param clazz
	 * @return
	 * @throws ServiceException
	 */
	public static boolean isValidOrdIStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Class clazz) throws ServiceException {
		if(unCheckOrder == null || unCheckOrder.size()==0){
			throw new ServiceException("OMS-"+clazz.getSimpleName()+"-isValidOrdIStatus", "无效的订单行状态");
		}
		OrdiStatus ordiStatus = unCheckOrder.get(0);
		if(ordiStatus == null || ordiStatus.getState() == GlobalNames.STATE_INVALID){
			throw new ServiceException("OMS-"+clazz.getSimpleName()+"-isValidOrdIStatus", "无效的订单行状态");
		}else if(IR.getType().equals(ordiStatus.getStatusType()) && ordiStatus.getBillType()!=Constant.ORDER_BILL_TYPE_REVERSE){
			//如果查询逆向订单，则billType必须等于逆向
			throw new ServiceException("OMS-"+clazz.getSimpleName()+"-isValidOrdIStatus", "无效的订单行状态");
		}
		return true;
	}
	
	/**
	 * 校验取消订单
	 * @param ordStatusDIO
	 * @param unCheckOrder
	 * @param clazz
	 * @return
	 * @throws ServiceException
	 */
	public static boolean checkOrderCancel(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder, Class clazz)
			throws ServiceException {

		if(unCheckOrder == null){
			throw new ServiceException("OMS-"+clazz.getSimpleName()+"-checkOrderCancel", "无效的订单行状态");
		}else{
			Map<String, OrdiStatus> ordiStatusMap = new HashMap<String, OrdiStatus>();
			for(OrdiStatus ordiStatus : unCheckOrder){
				ordiStatusMap.put(ordiStatus.getStatusType(), ordiStatus);
			}
			
			//判断当前是否是管理员在操作状态机
			if(!ordStatusDIO.isAdmin()){
				//普通用户校验， 用户只能取消待付款的订单
                OrdiStatus isStatus = ordiStatusMap.get(IS.getType());
                OrdiStatus ipStatus = ordiStatusMap.get(IP.getType());

                if(isStatus!=null && !isStatus.getStatusCode().equals(OrdITotalStatus.ORDI_COMMITTED.getCode()) ||
                        (ipStatus!=null && (!ipStatus.getStatusCode().equals(OrdIPayStatus.PAY_WAIT.getCode())
                                && !ipStatus.getStatusCode().equals(OrdIPayStatus.PAY_FREEZE.getCode())
                                && !ipStatus.getStatusCode().equals(OrdIPayStatus.PAY_PARTIAL.getCode())))){
                    throw new ServiceException("OMS-"+clazz.getSimpleName()+"-checkOrderCancel", "用户只能取消待付款的订单");
                }
              
			}else{
                if(ordiStatusMap.containsKey(IS.getType())){
                    //管理员校验， 管理员不能取消已完成了的订单
                    if(ordiStatusMap.get(IS.getType()).getStatusCode().equals(ORDI_FINISHED.getCode())){
                        throw new ServiceException("OMS-"+clazz.getSimpleName()+"-checkOrderCancel", "管理员不能取消已完成了的订单");
                    }
                    //管理员校验， 管理员不能取消已完成了的订单
                    if(ordiStatusMap.get(IS.getType()).getStatusCode().equals(ORDI_CANCEL.getCode())){
                        throw new ServiceException("OMS-"+clazz.getSimpleName()+"-checkOrderCancel", "管理员不能重复取消订单");
                    }
                }
			}
		}
		return true;
	}
	
	/**
	 * 校验取消货到付款订单
	 * @param ordStatusDIO
	 * @param unCheckOrder
	 * @param clazz
	 * @return
	 * @throws ServiceException
	 */
	public static boolean checkOrderCancelCOD(OrderStatusDTO ordStatusDIO, List<OrdiStatus> unCheckOrder, Class clazz)
			throws ServiceException {

		if(unCheckOrder == null){
			throw new ServiceException("OMS-"+clazz.getSimpleName()+"-checkOrderCancel", "无效的订单行状态");
		}else{
			Map<String, OrdiStatus> ordiStatusMap = new HashMap<String, OrdiStatus>();
			for(OrdiStatus ordiStatus : unCheckOrder){
				ordiStatusMap.put(ordiStatus.getStatusType(), ordiStatus);
			}
			
			//判断当前是否是管理员在操作状态机
			if(!ordStatusDIO.isAdmin()){
				//普通用户校验， 货到付款用户只能取消待出库的订单
                if(ordiStatusMap.containsKey(IS.getType()) && !ordiStatusMap.get(IS.getType()).getStatusCode().equals(OrdITotalStatus.ORDI_EFFECTIVED.getCode()) ||
                        ordiStatusMap.containsKey(ID.getType()) && !ordiStatusMap.get(ID.getType()).getStatusCode().equals(OrdIDeliverStatus.DELI_WAIT_OUT_REPOSITORY.getCode())){
                    throw new ServiceException("OMS-"+clazz.getSimpleName()+"-checkOrderCancelCOD", "货到付款订单,用户只能取消待出库的订单");
                }
			}else{
                if(ordiStatusMap.containsKey(IS.getType())){
                    //管理员校验， 管理员不能取消已完成了的订单
                    if(ordiStatusMap.get(IS.getType()).getStatusCode().equals(ORDI_FINISHED.getCode())){
                        throw new ServiceException("OMS-"+clazz.getSimpleName()+"-checkOrderCancelCOD", "管理员不能取消已完成了的订单");
                    }
                    //管理员校验， 管理员不能取消已完成了的订单
                    if(ordiStatusMap.get(IS.getType()).getStatusCode().equals(ORDI_CANCEL.getCode())){
                        throw new ServiceException("OMS-"+clazz.getSimpleName()+"-checkOrderCancelCOD", "管理员不能重复取消订单");
                    }
                }
			}
		}
		return true;
	}
	
	/**
	 * 校验待退款
	 * @param ordStatusDIO
	 * @param unCheckOrder
	 * @return
	 * @throws ServiceException
	 */
	public static boolean checkOrderRefundWait(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Class clazz) throws ServiceException {
		
		if(unCheckOrder != null){
			//循环遍历这个订单行状态结果集
			for(OrdiStatus ordiStatus : unCheckOrder){
				if(ordiStatus.getStatusType().equals(IP.getType())){
					throw new ServiceException("OMS-"+clazz.getSimpleName()+"-checkOrderRefundWait", "订单行状态已存在，不可重复提交");
				}else if(ordiStatus.getStatusType().equals(IR.getType())
						&& !ordiStatus.getStatusCode().equals(RET_WAIT_CONSULT.getCode())
						&& !ordiStatus.getStatusCode().equals(RET_SELLER_RECEIPTED.getCode())
						&& !ordiStatus.getStatusCode().equals(RET_BUYER_RECEIPTED.getCode())){
					throw new ServiceException("OMS-"+clazz.getSimpleName()+"-checkOrderRefundWait", "订单行状态只有待协商和客服确认收货，方可退款");
				}
			}
		}
		return true;
	}
}
