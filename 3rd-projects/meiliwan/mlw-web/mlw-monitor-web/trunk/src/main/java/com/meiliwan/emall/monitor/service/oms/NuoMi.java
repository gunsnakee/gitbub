package com.meiliwan.emall.monitor.service.oms;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import com.meiliwan.emall.monitor.bean.Config;
import com.meiliwan.emall.monitor.service.ConfigService;
import com.meiliwan.emall.monitor.service.wms.ExcelInterface;
import com.meiliwan.emall.monitor.service.wms.OrdSheet;


public class NuoMi implements ExcelInterface<OMSBean,OMSProduct> {

	private static ConfigService configService;
	static {
		ApplicationContext context = new ClassPathXmlApplicationContext(
		        new String[] {"conf/spring/web-integration.xml"});
		configService = (ConfigService)context.getBean("configService");
	}
	
	static Map<String,String> province = new HashMap<String,String>();
	static{
		province.put("广西省", "广西壮族自治区");
		province.put("宁夏", "宁夏回族自治区");
		province.put("内蒙古", "内蒙古自治区");
		province.put("新疆", "新疆维吾尔自治区");
	}
	@Override
	public void change(int cellIndex, String value,
			OrdSheet<OMSBean, OMSProduct> sheet) {
		if(cellIndex==6){
			OMSBean ord = new OMSBean();
			ord.setOrderId(value);
			ord.setMarketName("糯米团");
			ord.setStroeName("糯米团");
			ord.setState("WAIT_SELLER_SEND_GOODS");
			ord.setIsCOD("0");
			sheet.addOrd(ord);
		}
		OMSBean bean =sheet.getLastOrd();
		if(cellIndex==7){
			OMSProduct pro = new OMSProduct();
			pro.setSku(value);
			pro.setOrderId(bean.getOrderId());
			sheet.addPro(pro);
		}
		
		if(cellIndex==8){
			bean.setWillPay(value);
			bean.setRealPay(value);
		}
		if(cellIndex==9){
			OMSProduct pro = sheet.getLastPro();
			pro.setTitle(value);
		}
		if(cellIndex==10){
			bean.setPayTime(value);
			bean.setUpdateTime(value);
			bean.setOrderTime(value);
		}
		if(cellIndex==11){
			OMSProduct pro = sheet.getLastPro();
			pro.setNum(value);
		}
		if(cellIndex==12){
			OMSProduct pro = sheet.getLastPro();
			if(StringUtils.isNotBlank(value)){
				String[] names = value.split(";");
				String temp = pro.getTitle();
				for (int i = 0; i < names.length&&names.length>0; i++) {
					String last = names[i].substring(names[i].lastIndexOf("x")+1);
					String pre = names[i].substring(0,names[i].indexOf("x"));
					if(i==0){
						pro.setNum(last);
						pro.setTitle(temp+pre);
						pro.setStoreSku(getStroeSku(pro.getSku()+temp+pre));
						pro.setPrice(bean.getRealPay());
					}else{
						OMSProduct pronew = new OMSProduct();
						pronew.setOrderId(pro.getOrderId());
						pronew.setSku(pro.getSku());
						pronew.setNum(last);
						pronew.setTitle(temp+pre);
						pronew.setStoreSku(getStroeSku(pro.getSku()+temp+pre));
						pronew.setPrice(bean.getRealPay());
						sheet.addPro(pronew);
					}
				}
			}else{
				pro.setStoreSku(getStroeSku(pro.getSku()));
				pro.setPrice(bean.getRealPay());
			}
		}
		if(cellIndex==13){
			bean.setNickName(value);
			bean.setReceiver(value);
		}
		if(cellIndex==15){
			bean.setPhone(value);
		}
		if(cellIndex==16){
			String cha = province.get(value);
			if(StringUtils.isNotBlank(cha)){
				bean.setProvince(cha);
			}else{
				bean.setProvince(value);
			}
		}
		if(cellIndex==17){
			bean.setCity(value);
		}
		if(cellIndex==18){
			bean.setTown(value);
		}
		if(cellIndex==19){
			StringBuilder sb = new StringBuilder();
			sb.append(bean.getProvince());
			sb.append(bean.getCity());
			sb.append(bean.getTown());
			sb.append(value);
			bean.setAddress(sb.toString());
		}
		if(cellIndex==20){
			bean.setZipCode(value);
		}
		if(cellIndex==22){
			bean.setBuyerMemo(value);
		}
	}

	private String getStroeSku(String code) {
		// TODO Auto-generated method stub
		Config conf = configService.getConfigByCode(code,"nuomi");
		if(conf==null){
			return "";
		}
		return conf.getValue();
	}

	@Override
	public void extra(OrdSheet<OMSBean, OMSProduct> wmsSheet) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		String aa ="草莓x1;苹果x1;香橙x1;什果x1";
		System.out.println(aa.substring(aa.lastIndexOf("x")+1));
	}
}
