package com.meiliwan.emall.oms.constant;

import static com.meiliwan.emall.oms.constant.OrdIDeliverStatus.*;
import static com.meiliwan.emall.oms.constant.OrdIPayStatus.*;
import static com.meiliwan.emall.oms.constant.OrdIRetStatus.*;
import static com.meiliwan.emall.oms.constant.OrdITotalStatus.*;


import java.util.HashMap;
import java.util.Map;

import com.meiliwan.emall.oms.constant.OrdIDeliverStatus;
import com.meiliwan.emall.oms.constant.OrdIPayStatus;
import com.meiliwan.emall.oms.constant.OrdIRetStatus;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;




public enum OrderStatusCategory {

	PAY(PAY_WAIT,PAY_FREEZE,PAY_FAILURE,PAY_PARTIAL,PAY_FINISHED,PAY_CANCEL_FREEZE,PAY_CANCEL,PAY_CANCEL_PARTIAL,PAY_REFUND_WAIT,PAY_RETUND_FINISHED,PAY_REFUND_FAILURE),
    //PAY(PAY_WAIT,PAY_FINISHED,PAY_CANCEL,PAY_REFUND_WAIT,PAY_RETUND_FINISHED,PAY_REFUND_FAILURE),
	DELIVER(DELI_WAIT_OUT_REPOSITORY,DELI_WAIT_CONSIGNMENT,DELI_DELIVER_GOODS,DELI_RECEIPTED,DELI_REJECTED),
	ORDI(ORDI_COMMITTED,ORDI_FAILURE,ORDI_PROCESSED,ORDI_EFFECTIVED,ORDI_CONSINGMENT
			,ORDI_RECEIPTED,ORDI_FINISHED,ORDI_CANCEL),
	RET(RET_APPLY,RET_BUYER_OPT_CANCEL,RET_APPLY_REJECTED,RET_SELLER_OPT_EDIT,RET_BUYER_WAIT_CONSIGNMENT,RET_SYSTEM_CANCEL,RET_BUYER_DELIVER_GOODS,RET_SELLER_RECEIPTED
			,RET_SELLER_CONSINGMENT,RET_BUYER_RECEIPTED,RET_APPLY_PASSED,RET_WAIT_CONSULT,
			RET_REFUND_FAILURE,RET_FINISHED,RET_CANCEL,RET_SELLER_NO_RECEIPTED,RET_SELLER_WAITING_SEND,RET_SELLER_AG_SEND,RET_REFUND_WAIT,RET_REFUND_FINISHED,RET_REFUND_FAILURE);

	
	private Map<String, OrdIPayStatus> payStatus = new HashMap<String, OrdIPayStatus>();
	private Map<String, OrdIDeliverStatus> deliStatus = new HashMap<String, OrdIDeliverStatus>();
	private Map<String, OrdITotalStatus> ordiStatus = new HashMap<String, OrdITotalStatus>();
	private Map<String, OrdIRetStatus> retStatus = new HashMap<String, OrdIRetStatus>();
	
	OrderStatusCategory(OrdIPayStatus... status){
		for(int i=0;i<status.length;i++){
			payStatus.put(status[i].getCode(), status[i]);
		}
	}
	
	OrderStatusCategory(OrdIDeliverStatus... status){
		for(int i=0;i<status.length;i++){
			deliStatus.put(status[i].getCode(), status[i]);
		}
	}
	OrderStatusCategory(OrdITotalStatus... status){
		for(int i=0;i<status.length;i++){
			ordiStatus.put(status[i].getCode(), status[i]);
		}
	}
	OrderStatusCategory(OrdIRetStatus... status){
		for(int i=0;i<status.length;i++){
			retStatus.put(status[i].getCode(), status[i]);
		}
	}
	
	public OrdIPayStatus getPayStatus(String statusCode) {
		return payStatus.get(statusCode);
	}
	public OrdIDeliverStatus getDeliverStatus(String statusCode) {
		return deliStatus.get(statusCode);
	}
	public OrdITotalStatus getOrdIStatus(String statusCode) {
		return ordiStatus.get(statusCode);
	}
	public OrdIRetStatus getRetStatus(String statusCode) {
		return retStatus.get(statusCode);
	}
}
