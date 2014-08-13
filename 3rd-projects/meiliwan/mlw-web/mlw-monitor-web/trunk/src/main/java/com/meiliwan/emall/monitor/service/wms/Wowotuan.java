package com.meiliwan.emall.monitor.service.wms;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Wowotuan implements ExcelInterface<WMSBean,WMSProduct> {

	@Override
	public void change(int num, String value,OrdSheet<WMSBean,WMSProduct> sheet) {
		// TODO Auto-generated method stub
		if(num==1){
			if("订单号".equals(value)||StringUtils.isBlank(value)){
				return ;
			}
			WMSBean ord = new WMSBean();
			ord.setOrderId(value);
			sheet.addOrd(ord);
		}
		
		WMSBean bean =sheet.getLastOrd();
		if(bean==null){
			return ;
		}
		if(num==4){
			WMSProduct pro = new WMSProduct();
			pro.setName(value);
			pro.setOrderId(bean.getOrderId());
			sheet.addPro(pro);			
		}
		if(num==6){
			WMSProduct pro = sheet.getLastPro();
			if(pro!=null){
				pro.setNum(value);
			}
		}
		if(num==9){
			WMSProduct pro = sheet.getLastPro();
			if(pro!=null){
				pro.setName(pro.getName()+value);
			}
		}
		if(num==10){
			bean.setCreateTime(value);
		}
		if(num==11){
			bean.setProvince(value);
		}
		if(num==12){
			bean.setCity(value);
		}
		if(num==13){
			bean.setTown(value);
		}
		if(num==14){
			bean.setReceiveAddress(value);
		}
		if(num==16){
			bean.setReceiver(value);
		}
		if(num==18){
			bean.setMobile(value);
		}
		if(num==19){
			if("N".equals(value)){
				bean.setInvoiceFalse();
			}else{
				bean.setInvoiceTrue();
			}
		}
		if(num==21){
			bean.setInvoice(value);
		}
	}

	@Override
	public void extra(OrdSheet wmsSheet) {
		// TODO Auto-generated method stub
	}

}
