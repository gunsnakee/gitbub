package com.meiliwan.emall.oms.dao;

import java.util.List;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.oms.bean.OrdiStatus;
/**
 * 订单行状态DAO
 * @author yuxiong
 *
 */
public interface OrdiStatusDao extends IDao<String, OrdiStatus>{

	/**
	 * 查询订单行的状态
	 * @param orderItemId
	 * @param statusType
	 * @param statusCode
	 * @return
	 */
	OrdiStatus selectByOrdIStatus(String orderItemId, String statusType, String statusCode);

    /**
     * 查询订单行的状态
     * @param orderItemId
     * @param statusType
     * @param statusCode
     * @return
     */
    OrdiStatus selectByOrdIStatus(String orderItemId, String statusType, String statusCode, boolean isMainDS);

    /**
     * 根据订单ID，查询订单行的状态
     * @param orderId
     * @return
     */
    List<OrdiStatus> selectByOrderId(String orderId, boolean isMainDS);

    /**
     * 根据订单ID，查询订单行的状态
     * @param orderId
     * @return
     */
    List<OrdiStatus> selectByOrderId(String orderId);

    /**
     * 根据订单ID，查询订单行的状态
     * @param orderId
     * @return
     */
    List<OrdiStatus> selectByOrderId(String orderId, String[] statusType);

    /**
     * 根据订单ID，查询订单行的状态
     * @param orderId
     * @return
     */
    List<OrdiStatus> selectByOrderId(String orderId, String[] statusType, boolean isMainDS);

    /**
     * 根据订单ID，查询订单行的状态
     * @param orderId
     * @return
     */
    OrdiStatus selectOneByOrderId(String orderId, String statusType, String statusCode);

    /**
     * 根据订单ID，查询订单行的状态
     * @param orderIds
     * @return
     */
    List<String> selectOneOrderIdByOrderIds(List<String> orderIds, String statusType, String statusCode);

    /**
     * 根据订单ID，查询订单行的状态
     * @param orderId
     * @return
     */
    OrdiStatus selectOneByOrderId(String orderId, String statusType, String statusCode, boolean isMainDS);

    /**
     * 根据订单ID，查询订单行的状态
     * @param orderId
     * @return
     */
    OrdiStatus selectOneByOrderIdCodes(String orderId, String statusType, String[] statusCodes, boolean isMainDS);

	/**
	 * 查询订单行的状态
	 * @param orderItemId
	 * @param statusType
	 * @return
	 */
	List<OrdiStatus> selectByOrdIStatus(String orderItemId, String statusType);

    /**
     * 查询订单行的状态
     * @param orderItemId
     * @param statusType
     * @return
     */
    List<OrdiStatus> selectByOrdIStatus(String orderItemId, String statusType, boolean isMainDS);

	/**
	 * 查询订单行的状态
	 * @param orderItemId
	 * @return
	 */
	List<OrdiStatus> selectByOrdIStatus(String orderItemId);

    /**
     * 查询订单行的状态
     * @param orderItemId
     * @return
     */
    List<OrdiStatus> selectByOrdIStatus(String orderItemId, boolean isMainDS);


	void updateOrdIStatus(String orderItemId, String statusType, String statusCode);
	void updateAllOrdIStatus(String orderId, String statusType, String statusCode);

    void updateAllOrdIStatus(String orderId, String statusType,String statusCode, int adminId);

    /**
     * 根据订单ID和状态类型，去伪删除订单行状态
     * @param orderId
     * @param statusType
     * @param adminId
     */
    void deleteByOrdIdStatusType(String orderId, String statusType, int adminId);
}