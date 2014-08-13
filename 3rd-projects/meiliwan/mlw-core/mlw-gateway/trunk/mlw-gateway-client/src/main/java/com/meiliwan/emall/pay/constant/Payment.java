package com.meiliwan.emall.pay.constant;

import java.util.ArrayList;
import java.util.List;

import com.meiliwan.emall.commons.bean.PayCode;


/**
 * 创建新类的原因，就是原来设计的Code被用作他用了
 * @author rubi
 *
 */
public class Payment {

	
	private String code;
	private String desc;
	private String img;
    //前台显示
    private String show;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getShow() {
		return show;
	}
	public void setShow(String show) {
		this.show = show;
	}
	
	@Override
	public String toString() {
		return "Payment [code=" + code + ", desc=" + desc + ", img=" + img
				+ ", show=" + show + "]";
	}
	public static List<Payment> getPaymentList(){
		List<Payment> list = new ArrayList<Payment>();
		PayCode[] codes = PayCode.values();
		for (PayCode payCode : codes) {
			Payment pay = new Payment();
			pay.setCode(payCode.name());
			pay.setDesc(payCode.getDesc());
			pay.setImg(payCode.getImg());
			pay.setShow(payCode.getShow());
			list.add(pay);
		}
		return list;
	}
	
	public static void main(String[] args) {
		System.out.println(Payment.getPaymentList());
	}
}
