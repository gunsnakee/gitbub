package com.meiliwan.emall.sp2.constant;

public enum SpTicketBatchState {

    OFF("未上线", 0),
	ON("已上线",1),
    ABLED("未停用", 0),
	DISABLED("已停用", 1);


	private String desc;

	private int value;

	private SpTicketBatchState(String desc, int value){
	    this.desc = desc;
        this.value = value;
	}
	
	public String getDesc() {
	    return desc;
	}

	public int getValue() {
		return value;
	}
	
}
