package com.meiliwan.emall.commons.constant.order;

/**
 * 定义OMS订单对应的配送状态<br> 
 * 正向/换货正向       
 * 
 *       等待出库
 *       等待发货
 *       已收（已送达）
 *       拒收
 *
 * @author xiong.yu
 */
public enum OrdIDeliverStatus {
    
    /**
     * 等待出库
     */
	DELI_WAIT_OUT_REPOSITORY("10","等待出库"),
    /**
     * 等待发货
     */
	DELI_WAIT_CONSIGNMENT("20","等待发货"),
    
    /**
     * 已发货
     */
	DELI_DELIVER_GOODS("30","已发货"),
    
    /**
     * 已收
     */
	DELI_RECEIPTED("40","已收货"),
    
    /**
     * 拒收
     */
	DELI_REJECTED("50","拒收");
    
//    /**
//     * 退货已派工
//     */
//	DELI_REFUND_ARRANGED("60"),
//    
//    /**
//     * 退货已销单
//     */
//	DELI_REFUND_REPEALBILL("70"),
//    /**
//     * 退货已入站
//     */
//	DELI_REFUND_INREPOSITORY("80"),
//    /**
//     * 退货已入库
//     */
//	DELI_REFUND_INSTORAGE("90");
    
    private String code;
    private String desc;

    private OrdIDeliverStatus(String code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

	public String getDesc() {
		return desc;
	}
    
}
