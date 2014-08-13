package com.meiliwan.emall.monitor.service.oms;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.meiliwan.emall.monitor.bean.Config;
import com.meiliwan.emall.monitor.service.ConfigService;
import com.meiliwan.emall.monitor.service.wms.ExcelInterface;
import com.meiliwan.emall.monitor.service.wms.OrdSheet;

public class Wowotuan implements ExcelInterface<OMSBean,OMSProduct> {

	private static ConfigService configService;
	static {
		ApplicationContext context = new ClassPathXmlApplicationContext(
		        new String[] {"conf/spring/web-integration.xml"});
		configService = (ConfigService)context.getBean("configService");
	}
	
	@Override
	public void change(int num, String value,OrdSheet<OMSBean,OMSProduct> sheet) {
		// TODO Auto-generated method stub
		if(num==1){
			if("订单号".equals(value)||StringUtils.isBlank(value)){
				return ;
			}
			OMSBean ord = new OMSBean();
			ord.setOrderId(value);
			ord.setMarketName("窝窝团");
			ord.setStroeName("窝窝团");
			ord.setState("WAIT_SELLER_SEND_GOODS");
			sheet.addOrd(ord);
		}
		
		OMSBean bean =sheet.getLastOrd();
		if(bean==null){
			return ;
		}
		if(num==4){
			OMSProduct pro = new OMSProduct();
			pro.setTitle(value);
			pro.setOrderId(bean.getOrderId());
			sheet.addPro(pro);			
		}
		if(num==5){
			OMSProduct pro = sheet.getLastPro();
			pro.setSku(value);
		}
		if(num==6){
			OMSProduct pro = sheet.getLastPro();
			if(pro!=null){
				pro.setNum(value);
			}
		}
		if(num==9){
			OMSProduct pro = sheet.getLastPro();
			if(pro!=null){
				pro.setTitle(pro.getTitle()+value);
			}
			String proId = pro.getSku();
			if(StringUtils.isBlank(value)){
				pro.setStoreSku(getStroeSku(proId));
			}else{
				pro.setStoreSku(getStroeSku(proId+value));
			}
		}
		if(num==10){
			bean.setPayTime(value);
			bean.setOrderTime(value);
			bean.setUpdateTime(value);
			
			
			
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
			bean.setAddress(value);
		}
		if(num==15){
			bean.setZipCode(value);
		}
		if(num==16){
			bean.setReceiver(value);
		}
		if(num==18){
			bean.setPhone(value);
		}
		if(num==21){
			bean.setInvoiceHead(value);
		}
		if(num==22){
			bean.setInvoiceDetail(value);
		}
	}

	@Override
	public void extra(OrdSheet wmsSheet) {
		// TODO Auto-generated method stub
	}

	private String getStroeSku(String code) {
		// TODO Auto-generated method stub
		Config conf = configService.getConfigByCode(code,"wowotuan");
		if(conf==null){
			return "";
		}
		return conf.getValue();
	}
}
