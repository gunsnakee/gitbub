package com.meiliwan.emall.sf;

import java.util.Date;

import com.meiliwan.emall.commons.util.DateUtil;

public class WaybillRoute {

	private String id;
	private String mailno;
	private String orderid;
	private Date acceptTime;
	private String acceptAddress;
	private String remark;
	private String opCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAcceptAddress() {
		return acceptAddress;
	}
	public void setAcceptAddress(String acceptAddress) {
		this.acceptAddress = acceptAddress;
	}
	public String getMailno() {
		return mailno;
	}
	public void setMailno(String mailno) {
		this.mailno = mailno;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public Date getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOpCode() {
		return opCode;
	}
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	
	public String toXml(){
		StringBuilder sb = new StringBuilder();
		//sb.append("<WaybillRoute id=\""+id+"\" mailno=\""+mailno+"\" orderid=\""+orderid+"\" acceptTime=\""+DateUtil.getDatetimeStr(acceptTime)+"\" acceptAddress=\""+acceptAddress+"\" remark=\""+remark+"\" opCode=\""+opCode+"\"/>");
		return sb.toString();
	}
	
}
