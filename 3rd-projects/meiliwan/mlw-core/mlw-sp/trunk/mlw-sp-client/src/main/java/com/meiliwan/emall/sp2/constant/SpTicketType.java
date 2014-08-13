package com.meiliwan.emall.sp2.constant;

public enum SpTicketType {
	
	ALL(-1, "ALL", "券"),
	COMMON(0, "COMMON","通用券"), 
	CATEGORY(1, "CATEGORY","品类券");
	
	private String desc;
	private String code;
	private int value;
	
	private SpTicketType(int value, String code, String desc){
		this.value = value;
	    this.code = code;
	    this.desc = desc;
	}

	public String getDesc() {
	    return desc;
	}

	public void setDesc(String desc) {
	    this.desc = desc;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public int getValue() {
		return value;
	}

	public static SpTicketType getByCode(String code){
		SpTicketType[] typeArr = SpTicketType.values();
        for(SpTicketType sType : typeArr){
            if(code.equals(sType.getCode())){
                return sType;
            }
        }

        return null;
	}
	
	public static SpTicketType valueOf(int v) {
		 for(SpTicketType sType : SpTicketType.values()){
			 if (sType.value == v) 
				 return sType;
		 }
		 return null;
	}

}
