package com.meiliwan.emall.oms.service.ordstatus.ret;


import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdIRetStatus;
import com.meiliwan.emall.oms.constant.OrderType;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.oms.bean.*;
import com.meiliwan.emall.oms.dao.RetApplyDao;
import com.meiliwan.emall.oms.dto.OrderStatusDTO;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusChecker;
import com.meiliwan.emall.oms.service.ordstatus.OrderStatusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.DELI_WAIT_OUT_REPOSITORY;
import static com.meiliwan.emall.oms.constant.OrdIPayStatus.PAY_REFUND_WAIT;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.*;
import static com.meiliwan.emall.oms.constant.OrderStatusType.*;

/**
 * 订单行状态：退换货审核通过
 * @author yuxiong
 *
 */
@Component
public class RetStatusApplyPassed extends OrderStatusService{

//    @Autowired
//    private RetChgDao retChgDao;
//    @Autowired
//    private RetPayDao retPayDao;
    @Autowired
    private RetApplyDao retApplyDao;

	@Override
	protected List<OrdiStatus> getOrdiStatus(OrderStatusDTO ordStatusDIO) throws ServiceException{
		return ordiStatus2List(ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(),
				IR.getType(), RET_APPLY.getCode(), true));
	}

	@Override
	public void updateStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder, Ordi ordi) throws ServiceException{
		
		checkOrderStatus(ordStatusDIO, unCheckOrder);

        String orderItemId = ordi.getOrderItemId();
        String orderType = ordStatusDIO.getOrderType();

        RetApply apply = retApplyDao.getEntityById(orderItemId, true);
        
        //审核通过,用户不需要退货
		ordLogDao.insertApplyExtraOrdLog(ordi,"申请退换货数量"+apply.getProCount() +",审核通过的数量为"+ordStatusDIO.getRetProCount()+"。");
		ordLogDao.insertApplyAgreeNotBack(ordi);
		//        
       
        apply.setState(GlobalNames.STATE_CHECK_PASS);
        apply.setRetPayFare(ordStatusDIO.getBackTransportFee().doubleValue());
        apply.setNeedRetpro(ordStatusDIO.getNeedRetpro());
        apply.setReturnReason(ordStatusDIO.getReturnReasonType());
        apply.setProCount(ordStatusDIO.getRetProCount());
       
        
        OrdIRetStatus toStatus = null;

        if(orderType.equals(OrderType.CHANGE.getCode())){
            insertChg(apply,ordStatusDIO);
            //审核通过修改做等待客服发货
            toStatus = RET_SELLER_RECEIPTED;

                    //对于换货来说，需要校验它的物流配送状态
            unCheckOrder = ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), ID.getType(), true);
            OrderStatusChecker.isNull(ordStatusDIO, unCheckOrder, this.getClass());

            //对于换货来说，需要写入一个待出库的状态
            OrdiStatus ordiStatus = new OrdiStatus();
            ordiStatus.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
            ordiStatus.setOrderId(ordStatusDIO.getOrderId());
            ordiStatus.setOrderItemId(ordStatusDIO.getOrderItemId());
            ordiStatus.setState(GlobalNames.STATE_VALID);
            ordiStatus.setStatusCode(DELI_WAIT_OUT_REPOSITORY.getCode());
            ordiStatus.setStatusType(ID.getType());
            ordiStatus.setUid(ordStatusDIO.getUid());
            ordiStatus.setAdminId(ordStatusDIO.getAdminId());
            ordiStatusDao.insert(ordiStatus);
        }
        if(orderType.equals(OrderType.RECEDE.getCode())){
            insertPay(apply,ordStatusDIO);
            //审核通过修改做等待退款
            toStatus = RET_REFUND_WAIT;

            //对于退货来说，需要校验它的支付状态
            unCheckOrder = ordiStatusDao.selectByOrdIStatus(ordStatusDIO.getOrderItemId(), IP.getType(), true);
            OrderStatusChecker.isNull(ordStatusDIO, unCheckOrder, this.getClass());

            //写入待支付
            OrdiStatus ordiStatus = new OrdiStatus();
            ordiStatus.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
            ordiStatus.setOrderId(ordStatusDIO.getOrderId());
            ordiStatus.setOrderItemId(ordStatusDIO.getOrderItemId());
            ordiStatus.setState(GlobalNames.STATE_VALID);
            ordiStatus.setStatusCode(PAY_REFUND_WAIT.getCode());
            ordiStatus.setStatusType(IP.getType());
            ordiStatus.setUid(ordStatusDIO.getUid());
            ordiStatus.setAdminId(ordStatusDIO.getAdminId());
            ordiStatusDao.insert(ordiStatus);
        }
        ordiStatusDao.updateOrdIStatus(ordStatusDIO.getOrderItemId(), IR.getType(), toStatus.getCode());
        //更新订单行表的状态
        //updateOrdIAndOrdStatus(ordStatusDIO, toStatus);
        
        if(orderType.equals(OrderType.CHANGE.getCode())){
        	//同时更新orderType
			ordiDao.updateOrdIStatus(apply.getRetordItemId(), toStatus.getCode(),OrderType.CHANGE.getCode());
			ordDao.updateOrdStatus(apply.getRetordId(), toStatus.getCode(),OrderType.CHANGE.getCode());
		}
		if(orderType.equals(OrderType.RECEDE.getCode())){
			//同时更新orderType
			ordiDao.updateOrdIStatus(apply.getRetordItemId(), toStatus.getCode(),OrderType.RECEDE.getCode());
			ordDao.updateOrdStatus(apply.getRetordId(), toStatus.getCode(),OrderType.RECEDE.getCode());
		}
	}

	@Override
	protected boolean checkOrderStatus(OrderStatusDTO ordStatusDIO,
			List<OrdiStatus> unCheckOrder) throws ServiceException {

        if(!ordStatusDIO.isAdmin()){
            throw new ServiceException("OMS-"+this.getClass().getSimpleName()+"-execute", "只有管理员可以审核退换货");
        }

        if(unCheckOrder == null){
            throw new ServiceException("OMS-"+this.getClass().getSimpleName()+"-execute", "无效的订单行状态");
        }
        OrdiStatus ordiStatus = unCheckOrder.get(0);
        if(ordiStatus == null || ordiStatus.getState() == GlobalNames.STATE_INVALID){
            throw new ServiceException("OMS-"+this.getClass().getSimpleName()+"-execute", "无效的订单行状态");
        }else if(IR.getType().equals(ordiStatus.getStatusType()) && ordiStatus.getBillType()!=Constant.ORDER_BILL_TYPE_REVERSE){
            //如果查询逆向订单，则billType必须等于逆向
            throw new ServiceException("OMS-"+this.getClass().getSimpleName()+"-execute", "无效的订单行状态");
        }
        return true;
	}

    /**
     * 换货,换货订单插入一条记录，更新申请订单的类型
     * @param apply
     * @param ordStatusDIO
     */
    private void insertChg(RetApply apply,OrderStatusDTO ordStatusDIO) {
//        RetChg chg = applyCloneToRetChg(apply,ordStatusDIO);

        apply.setRetType(OrderType.CHANGE.getCode());
        retApplyDao.update(apply);
//        retChgDao.insert(chg);
        
    }

    /**
     * 退货,退货订单插入一条记录，更新申请订单的类型
     * @param apply
     * @param ordStatusDIO
     */
    private void insertPay(RetApply apply,OrderStatusDTO ordStatusDIO) {
//        RetPay pay = applyCloneToRetPay(apply,ordStatusDIO);
        apply.setRetType(OrderType.RECEDE.getCode());
        
        retApplyDao.update(apply);
//        retPayDao.insert(pay);

     
        
    }

//    private RetChg applyCloneToRetChg(RetApply apply,OrderStatusDTO ordStatusDIO){
//        RetChg retChg = new RetChg();
//        retChg.setChgReason(apply.getReturnReason());
//        retChg.setChgTime(apply.getCreateTime());
//        retChg.setCreateTime(new Date());
//        retChg.setRetordId(apply.getRetordId());
//        retChg.setRetordItemId(apply.getRetordItemId());
//        retChg.setState(GlobalNames.STATE_VALID);
//        retChg.setUpdateTime(new Date());
//        return retChg;
//    }
//
//    private RetPay applyCloneToRetPay(RetApply apply,
//                                      OrderStatusDTO ordStatusDIO) {
//        Date date = new Date();
//        RetPay pay = new RetPay();
//        pay.setCreateTime(date);
//        pay.setRetordId(apply.getRetordId());
//        pay.setRetordItemId(apply.getRetordItemId());
//        pay.setState(GlobalNames.STATE_VALID);
//        pay.setUpdateTime(date);
//        pay.setRetTime(date);
//
//        return pay;
//    }
}
