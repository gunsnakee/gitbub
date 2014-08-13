package com.meiliwan.emall.sp2.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.meiliwan.emall.sp2.base.BaseTest;
import com.meiliwan.emall.sp2.bean.SpTicket;
import com.meiliwan.emall.sp2.bean.view.SimpleOrdi;
import com.meiliwan.emall.sp2.bean.view.SimpleSpTicket;
import com.meiliwan.emall.sp2.bean.view.SpTicketUsageResult;
import com.meiliwan.emall.sp2.constant.SpTicketType;

public class SpTicketServiceTest  extends BaseTest{
	
	@Autowired
	SpTicketService spTicketService;
	
	@Test
	public void getSpTicketMapOfType() {
		int uid = 1848;
		int[] proIds = {10000170,10000175,10000047,10000083};
		
		//Map<SpTicketType, Set<SpTicket>> spTicketsOfType = SpTicketClient.getSpTicketsOfType(uid, proIds);
		
		HashMap<SpTicketType, Set<SpTicket>> spTicketsOfType = spTicketService.getSpTicketMapOfType(uid, proIds);
		
		System.out.println(spTicketsOfType.get(SpTicketType.COMMON));
		
		System.out.println(spTicketsOfType.get(SpTicketType.CATEGORY));
	}
	
	@Test
	public void getCalSrOnTicketSelective(){
		int uid = 1848;
		
		SimpleOrdi simpleOrdi = new SimpleOrdi();
		simpleOrdi.setProId(10000170);
		simpleOrdi.setPrice(new BigDecimal(0.1));
		
		List<SimpleOrdi> sL = new ArrayList<SimpleOrdi>();
		sL.add(simpleOrdi);
		
		simpleOrdi = new SimpleOrdi();
		simpleOrdi.setProId(10000175);
		simpleOrdi.setPrice(new BigDecimal(0.1));
		sL.add(simpleOrdi);
		
		simpleOrdi = new SimpleOrdi();
		simpleOrdi.setProId(10000047);
		simpleOrdi.setPrice(new BigDecimal(12.0));
		sL.add(simpleOrdi);
		
		SimpleSpTicket st = new SimpleSpTicket();
		st.setStType(SpTicketType.COMMON);
		st.setTicketId("Q9OKTU9HMX");
		
		SpTicketUsageResult sr = spTicketService.getCalSrOnTicketSelective(uid, sL,  st) ;
		
		System.out.println(sr);
	}
	
	@Test
	public void useSpTicketOnOrder(){
		int uid = 1848;
		String orderId = "000006260420";
		SimpleSpTicket st = new SimpleSpTicket();
		st.setStType(SpTicketType.COMMON);
		st.setTicketId("QEAS201406031262");
		
		Map<SimpleOrdi, BigDecimal> soiDiscountMap = new HashMap<SimpleOrdi, BigDecimal>();
		SimpleOrdi simpleOrdi = new SimpleOrdi();
		simpleOrdi.setProId(10000170);
		simpleOrdi.setPrice(new BigDecimal(0.1));
		soiDiscountMap.put(simpleOrdi, new BigDecimal(0));
		
		st.setSoiDiscountMap(soiDiscountMap);
		
		spTicketService.useSpTicketOnOrder(uid, orderId, st);
	}
}
