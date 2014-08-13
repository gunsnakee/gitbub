package com.meiliwan.emall.oms.constant;

/**
 * 声明OMS订单行状态
 *
 * @author xiong.yu
 */
public enum OrdITotalStatus {
    /**
     * 正向-订单已提交
     */
    ORDI_COMMITTED("10","等待付款"),

    /**
     * 正向-处理失败
     */
    ORDI_FAILURE("12","处理失败"),
    
    /**
     * 正向-处理完成
     */
    ORDI_PROCESSED("20","处理完成"),
    
    /**
     * 正向-已生效
     */
    ORDI_EFFECTIVED("30","等待发货"),
    /**
     * 正向-已发货
     */
    ORDI_CONSINGMENT("40","等待确认收货"),
   
    /**
     * 正向-已收货已付
     */
    ORDI_RECEIPTED("50","已收货已付"),
    /**
     * 正向-已完成
     */
    ORDI_FINISHED("60","交易成功"),
    
    /**
     * 正向-取消订单
     */
    ORDI_CANCEL("80","已取消");
    
    private String code;
    private String desc;

    private OrdITotalStatus(String code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }
    public String getDesc() {
        return this.desc;
    }
    public static void main(String[] args) {
    	System.out.println(OrdITotalStatus.values());
    	OrdITotalStatus[] ois = OrdITotalStatus.values();
    	for (OrdITotalStatus OrdITotalStatus : ois) {
    		System.out.print(OrdITotalStatus.ordinal()+":");
    		System.out.print(OrdITotalStatus.getCode());
    		System.out.print(OrdITotalStatus.getDesc());
    		System.out.println(OrdITotalStatus.name());
    		
			
		}
	}
    
    /**
     * 供前台的订单列表使用
     * @return
     */
    public static OrdITotalStatus[] getTotalStatus(){
    	OrdITotalStatus[] list = {
            ORDI_COMMITTED,ORDI_EFFECTIVED,
            ORDI_CONSINGMENT,ORDI_FINISHED,
            ORDI_CANCEL
        };
    	return list;
    }

    /**
     * 根据code获取描述
     * @param code
     * @return
     */
    public static String getDescByCode(String code){
        OrdITotalStatus[] ordITotalStatuss =  OrdITotalStatus.values();
        for(OrdITotalStatus ordITotalStatus:ordITotalStatuss){
             if(ordITotalStatus.getCode().equals(code)){
                    return ordITotalStatus.getDesc();
             }
        }
        return null;
    }


    
    
}
