package com.meiliwan.emall.oms.service;


import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.bean.OrdException;
import com.meiliwan.emall.oms.dao.OrdDao;
import com.meiliwan.emall.oms.dao.OrdExceptionDao;
import com.meiliwan.emall.oms.dao.OrdLogDao;
import com.meiliwan.emall.oms.exception.OrderException;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;


/**
 * 订单baseService
 *
 */
@Service
public abstract class BaseOrderService extends DefaultBaseServiceImpl {

    @Autowired
    protected OrdExceptionDao ordExceptionDao;
    @Autowired
    private OrdLogDao ordLogDao;

    @Autowired
    protected OrdDao ordDao;

    /*
    定义订单号统一长度为12位
     */
    protected static final int ORDERID_LENGTH = 12;

//    public String getOrderId(){
//        int id = ordDao.nextOrdSeq();
//        return IDGenerator.getId(id, ORDERID_LENGTH);
//    }


    /**
     * 订单ID：00000425818+02
     */
    protected String getOrderItemId(String orderId, int itemNum){
        return orderId + orderItemNum2String(itemNum);
    }

    private static String orderItemNum2String(int orderItemNum){
        return orderItemNum<10?"0"+orderItemNum:Integer.toString(orderItemNum);
    }

    /**
     * 保存异常订单
     * @param bizCode
     * @param orderId
     * @param orderItemId
     * @param billType
     * @param errorCode
     * @param errorMsg
     * @param statusType
     * @param statusCode
     * @param ordLastStatus
     * @param state
     */
    protected void saveOrdException(String bizCode, String orderId, String orderItemId, short billType, String errorCode, String errorMsg,
                                    String statusType, String statusCode, String ordLastStatus, short state){
        Date date = new Date();
        OrdException ordException = new OrdException();
        ordException.setBizCode(bizCode);
        ordException.setOrderId(orderId);
        if(!StringUtils.isEmpty(orderItemId))  {
            ordException.setOrderItemId(orderItemId);
        }
        ordException.setBillType(billType);
        ordException.setCreateTime(date);
        ordException.setErrorCode(errorCode);
        ordException.setErrorMsg(errorMsg.length() > 500 ? errorMsg.substring(0, 500) : errorMsg);
        ordException.setStatusType(statusType);
        ordException.setStatusCode(statusCode);
        if(!StringUtils.isEmpty(ordLastStatus))  {
            ordException.setOrdLastStatus(ordLastStatus);
        }
        ordException.setState(state);
        ordException.setUpdateTime(date);
        ordExceptionDao.insert(ordException);
        Ord ord = new Ord();
        ord.setOrderId(orderId);
        ord.setExceptionCode(bizCode);
        ordDao.update(ord);
        throw new OrderException(errorCode, errorMsg);
    }
    
    /**
     * 保存异常订单
     * @param bizCode
     * @param orderId
     * @param orderItemId
     * @param billType
     * @param errorCode
     * @param errorMsg
     * @param statusType
     * @param statusCode
     * @param ordLastStatus
     * @param state
     */
    protected void saveOrdException(String bizCode, String orderId, String orderItemId, short billType, String errorCode, String errorMsg,
                                    String statusType, String statusCode, String ordLastStatus, short state,Integer uid){
        Date date = new Date();
        OrdException ordException = new OrdException();
        ordException.setBizCode(bizCode);
        ordException.setOrderId(orderId);
        if(!StringUtils.isEmpty(orderItemId))  {
            ordException.setOrderItemId(orderItemId);
        }
        ordException.setUid(uid);
        ordException.setBillType(billType);
        ordException.setCreateTime(date);
        ordException.setErrorCode(errorCode);
        ordException.setErrorMsg(errorMsg.length() > 500 ? errorMsg.substring(0, 500) : errorMsg);
        ordException.setStatusType(statusType);
        ordException.setStatusCode(statusCode);
        if(!StringUtils.isEmpty(ordLastStatus))  {
            ordException.setOrdLastStatus(ordLastStatus);
        }
        ordException.setState(state);
        ordException.setUpdateTime(date);
        ordExceptionDao.insert(ordException);

        Ord ord = new Ord();
        ord.setOrderId(orderId);
        ord.setExceptionCode(bizCode);
        ordDao.update(ord);
        throw new OrderException(errorCode, errorMsg);
    }

    /**
     * 保存异常订单
     * @param bizCode
     * @param orderId
     * @param orderItemId
     * @param billType
     * @param errorCode
     * @param errorMsg
     * @param statusType
     * @param statusCode
     * @param state
     */
    protected void saveOrdException(String bizCode, String orderId, String orderItemId, short billType, String errorCode, String errorMsg,
                                    String statusType, String statusCode, short state,Integer uid){
        saveOrdException(bizCode, orderId, orderItemId, billType, errorCode, errorMsg, statusType, statusCode, "", state,uid);
    }

    /**
     * 保存一个只有orderId的异常订单
     * @param bizCode
     * @param orderId
     * @param billType
     * @param errorCode
     * @param errorMsg
     * @param statusType
     * @param statusCode
     * @param state
     */
    protected void saveOrdException(String bizCode, String orderId, short billType, String errorCode, String errorMsg, String statusType, String statusCode, short state,Integer uid){
        saveOrdException(bizCode, orderId, "", billType, errorCode, errorMsg, statusType, statusCode, "", state,uid);
    }
    
    
}