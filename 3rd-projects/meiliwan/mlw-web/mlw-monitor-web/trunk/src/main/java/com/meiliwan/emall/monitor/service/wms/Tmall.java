package com.meiliwan.emall.monitor.service.wms;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Tmall implements ExcelInterface<WMSBean,WMSProduct> {

	public void change(int cellIndex,String value,OrdSheet<WMSBean,WMSProduct> sheet){
		if(cellIndex==0){
			WMSBean ord = new WMSBean();
			ord.setOrderId(value);
			ord.setInvoiceFalse();
			sheet.addOrd(ord);
		}
		WMSBean bean =sheet.getLastOrd();
		if(cellIndex==8){
			bean.setBuyerPayment(value);
		}
		if(cellIndex==12){
			bean.setReceiver(value);
		}
		if(cellIndex==13){
			bean.setReceiveAddress(value);
		}
		if(cellIndex==15){
			bean.setPhone(value);
		}
		if(cellIndex==16){
			bean.setMobile(value);
		}
		if(cellIndex==17){
			bean.setCreateTime(value);
		}
		if(cellIndex==19){
			if(StringUtils.isNotBlank(value)){
				String strpro[]=value.split(",");
				for (int i = 0; i < strpro.length; i++) {
					WMSProduct pro = new WMSProduct();
					pro.setName(strpro[i]);
					pro.setOrderId(bean.getOrderId());
					sheet.addPro(pro);
				}
			}else{
				bean.setValid(false);
			}
		}
		if(cellIndex==22){
			bean.setNameOfTransport(value);
		}
		if(cellIndex==30){
			if(StringUtils.isNotBlank(value)){
				bean.setInvoice(value);
				bean.setInvoiceTrue();
			}
		}
	}

	@Override
	public void extra(OrdSheet wmsSheet) {
		// TODO Auto-generated method stub
		
	}
}
