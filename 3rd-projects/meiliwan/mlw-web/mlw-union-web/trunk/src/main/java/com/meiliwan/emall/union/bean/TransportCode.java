package com.meiliwan.emall.union.bean;

import java.util.HashMap;
import java.util.Map;

public enum TransportCode {
	
	SUCCESS("B00","数据处理成功"),
	NOT_FOUND("B01","找不到订单"),
	VALIDATE("B02","Track节点中字段校验不通过"),
	DATA_REPEAT("B03","数据重复异常"),
	NOT_ORDER_ID("B04","非承运商承运的订单"),
	STATUS_ERROR("B05","状态编码错误"),
	DATA_ERROR("B99","数据处理出现不可预见异常"),
	
	REQUEST_DATA("S01","非法的请求格式"),
	SECURITY("S02","非法的数字签名"),
	COMPANY("S03","非法的物流公司"),
	LENGTH("S04","请求数据量超限"),
	TIMES("S05","一天中请求次数过多"),
	SYSTEM("S99","系统出现不可预见异常");
	
	private String code;
	private String msg;
	private TransportCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	public boolean isSuccess(){
		if(this.getCode().equals("B00")){
			return true;
		}
		return false;
	}
	public boolean isRepeat(){
		if(this.getCode().equals("B03")){
			return true;
		}
		return false;
	}
	public Map<String,String> toMap(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("code", this.getCode());
		map.put("msg", this.getMsg());
		return map;
	}
}
