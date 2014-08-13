package com.meiliwan.emall.commons.web;

import java.io.Serializable;

/**
 * json返回结果
 * @author rubi
 *
 */
public class JsonResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7894985520208659138L;
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 返回消息
	 */
	private String msg;
	/**
	 * 处理成功true
	 * 处理失败false
	 */
	private boolean result;
	
	//{result:true}
	public JsonResult(boolean result) {
		super();
		this.result = result;
	}
	public JsonResult(String code, String msg, boolean result) {
		super();
		this.code = code;
		this.msg = msg;
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	
	
}
