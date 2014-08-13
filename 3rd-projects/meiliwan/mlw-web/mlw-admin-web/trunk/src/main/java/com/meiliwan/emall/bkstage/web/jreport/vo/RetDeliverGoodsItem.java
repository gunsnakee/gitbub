package com.meiliwan.emall.bkstage.web.jreport.vo;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.meiliwan.emall.bkstage.web.html.HtmlFilterUtil;

public class RetDeliverGoodsItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8999277343665515197L;
	private  DecimalFormat df = new DecimalFormat("#0.00");
	
	private int proId;
	//条形码
	private String proSn;
	private String Storagepaces; 
	private String proName;

	private int retNum;
	
	
	public int getProId() {
		return proId;
	}

	public void setProId(int proId) {
		this.proId = proId;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		
		this.proName = HtmlFilterUtil.encoding(proName);
	}

	public String getProSn() {
		return proSn;
	}

	public void setProSn(String proSn) {
		this.proSn = HtmlFilterUtil.encoding(proSn);
	}

	public String getStoragepaces() {
		return Storagepaces;
	}

	public void setStoragepaces(String storagepaces) {
		Storagepaces = HtmlFilterUtil.encoding(storagepaces);
	}

	public int getRetNum() {
		return retNum;
	}

	public void setRetNum(int retNum) {
		this.retNum = retNum;
	}
	
	
	
}
 