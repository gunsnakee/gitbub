package com.meiliwan.emall.monitor.service.wms;

import org.apache.commons.lang.StringUtils;

public class Yhd implements ExcelInterface<WMSBean,WMSProduct> {

	@Override
	public void change(int num, String value, OrdSheet<WMSBean,WMSProduct> sheet) {
		// TODO Auto-generated method stub
		
		if(num==0){
			WMSBean ord = new WMSBean();
			ord.setOrderId(value);
			sheet.addOrd(ord);
		}
		WMSBean bean =sheet.getLastOrd();
		if(num==0){
			if(StringUtils.isBlank(value)){
				bean.setValid(false);
			}
		}
		if(num==2){
			WMSProduct pro = new WMSProduct();
			pro.setCode(value);
			pro.setOrderId(bean.getOrderId());
			sheet.addPro(pro);
		}
		if(num==3){
			WMSProduct pro = sheet.getLastPro();
			if(pro!=null){
				pro.setName(value);
			}
		}
		if(num==5){
			WMSProduct pro = sheet.getLastPro();
			if(pro!=null){
				pro.setNum(value);
			}
		}
		if(num==6){
			WMSProduct pro = sheet.getLastPro();
			if(pro!=null){
				pro.setPrice(value);
			}
		}
		if(num==9){
			bean.setBuyerPayment(value);
		}
		if(num==11){
			bean.setCreateTime(value);
			if(StringUtils.isBlank(value)){
				bean.setValid(false);
			}
		}
		if(num==14){
			bean.setReceiver(value);
		}
		if(num==15){
			bean.setPhone(value);
		}
		if(num==16){
			bean.setMobile(value);
		}
		if(num==19){
			bean.setReceiveAddress(value);
		}
		if(num==20){
			if("网上支付".equals(value)){
				bean.setCod("0");
			}else{
				bean.setCod("1");
			}
		}
		if(num==21){
			if(StringUtils.isBlank(value)){
				bean.setInvoiceFalse();
			}else{
				bean.setInvoiceTrue();
				bean.setInvoice(value);
			}
		}
		if(num==25){
			bean.setSellerMeno(value);
		}
	}

	@Override
	public void extra(OrdSheet wmsSheet) {
		
	}
}
