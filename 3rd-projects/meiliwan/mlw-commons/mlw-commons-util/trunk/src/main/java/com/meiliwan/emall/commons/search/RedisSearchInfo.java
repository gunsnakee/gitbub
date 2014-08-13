package com.meiliwan.emall.commons.search;


public class RedisSearchInfo {
	
	private String objId;
	
	private String msgType;

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@Override
	public String toString() {
		return "{objId:\"" + getObjId() + "\",msgType:\"" + getMsgType() + "\"}";
	}

}
