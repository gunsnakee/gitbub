package com.meiliwan.emall.monitor.service.oms;

import java.util.List;

import com.meiliwan.emall.monitor.service.wms.ExcelInterface;
import com.meiliwan.emall.monitor.service.wms.OrdSheet;

public class Taocz implements ExcelInterface<OMSBean,OMSProduct> {

	@Override
	public void change(int num, String value, OrdSheet<OMSBean,OMSProduct> sheet) {
		// TODO Auto-generated method stub
		OMSBean bean =sheet.getLastOrd();
		if(num==0){
			OMSBean ord = new OMSBean();
			ord.setOrderId(value);
			ord.setMarketName("淘常州");
			ord.setStroeName("淘常州");
			ord.setState("WAIT_SELLER_SEND_GOODS");
			ord.setIsCOD("0");
			ord.setProvince("江苏省");
			ord.setCity("常州市");
			sheet.addOrd(ord);
		}
		if(num==1){
			bean.setPayTime(value);
			bean.setOrderTime(value);
			bean.setUpdateTime(value);
		}
		if(num==3){
			bean.setNickName(value);
		}
		if(num==4){
			if("无效".equals(value)||"取消".equals(value)){
				bean.setValid(false);
			}
		}
		if(num==5){
			bean.setWillPay(value);
			bean.setRealPay(value);
		}
		if(num==6){
			if(!bean.isValid()){
				return ;
			}
			String str[]=value.split("\n");
			for (String string : str) {
				OMSProduct pro = new OMSProduct();
				pro.setTitle(string);
				pro.setOrderId(bean.getOrderId());
				sheet.addPro(pro);
			}
		}
		if(num==7){
			if(!bean.isValid()){
				return ;
			}
			String str[]=value.split("\n");
			for (int i=0; i<str.length;i++) {
				OMSProduct pro = sheet.getProBySize(i+1);
				pro.setPrice(str[i]);
				pro.setInvoicePay(bean.getWillPay());
			}
			
		}
		if(num==8){
			if(!bean.isValid()){
				return ;
			}
			String str[]=value.split("\n");
			for (int i=0; i<str.length;i++) {
				OMSProduct pro = sheet.getProBySize(i+1);
				pro.setNum(str[i]);
			}
		}
		if(num==9){
			if(!bean.isValid()){
				return ;
			}
			String str[]=value.split("\n");
			for (int i=0; i<str.length;i++) {
				OMSProduct pro = sheet.getProBySize(i+1);
				pro.setSku(str[i]);
				pro.setStoreSku(str[i]);
			}
		}
		if(num==12){
			bean.setReceiver(value);
		}
		if(num==13){
			bean.setPhone(value);
		}
		if(num==14){
			String town = value.substring(0, value.indexOf(" "));
			bean.setTown(town);
			bean.setAddress("江苏省 "+"常州市 "+value);
		}
		
	}

	@Override
	public void extra(OrdSheet wmsSheet) {
		// TODO Auto-generated method stub
		
	}

}
