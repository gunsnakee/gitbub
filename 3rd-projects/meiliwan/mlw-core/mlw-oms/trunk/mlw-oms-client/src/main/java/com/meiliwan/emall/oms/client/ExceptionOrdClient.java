package com.meiliwan.emall.oms.client;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.bean.OrdException;
import com.meiliwan.emall.oms.constant.BizCode;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdIDeliverStatus;
import com.meiliwan.emall.oms.constant.OrderStatusType;
import com.meiliwan.emall.oms.dto.OrdExceptionDTO;
import com.meiliwan.emall.service.BaseService;

/**
 * 异常订单
 * @author rubi
 *
 */
public class ExceptionOrdClient {

	public static final String PAGEBYOBJ = "exceptionOrderService/pageByObj";
	
	public static PagerControl<OrdException> pageByObj(OrdExceptionDTO dto,
			PageInfo pageInfo, String orderName, String orderForm){
		
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(PAGEBYOBJ, dto, pageInfo,orderName,orderForm));
		
		JsonObject jbean = obj.get(BaseService.RESULT_OBJ).getAsJsonObject();
		return new Gson().fromJson(jbean, new TypeToken<PagerControl<OrdException>>() {
        }.getType());
	}
	
	/**
	 * 出库时调用库存时异常订单
	 * @param ord
	 * @param bizCode
	 * @param errorCode
	 * @param errorMsg
	 */
	public static void createSubStockOrdException(Ord ord,String errorMsg,int adminId){
		 OrdException ordException = new OrdException();
         ordException.setState(GlobalNames.STATE_VALID);
         ordException.setOrderId(ord.getOrderId());
         ordException.setBillType(Constant.ORDER_BILL_TYPE_FORWARD);
         ordException.setBizCode(BizCode.SUB_STOCK.name());
         ordException.setErrorCode(BizCode.SUB_STOCK.name());
         ordException.setErrorMsg(errorMsg);
         ordException.setAdminId(adminId);
         ordException.setOrdLastStatus(ord.getOrderStatus());
         ordException.setStatusCode(OrdIDeliverStatus.DELI_WAIT_CONSIGNMENT.getCode());
         ordException.setStatusType(OrderStatusType.ID.name());
         Date date = new Date();
         ordException.setUpdateTime(date);
         ordException.setCreateTime(date);
         IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE, JSONTool.buildParams("exceptionOrderService/createOrdException", ordException));
	}
}
