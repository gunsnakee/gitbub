package com.meiliwan.emall.sp2.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.sp2.base.BaseTest;
import com.meiliwan.emall.sp2.bean.SpTicket;
import com.meiliwan.emall.sp2.bean.SpTicketBatchProd;
import com.meiliwan.emall.sp2.constant.SpTicketState;

public class SpTicketDaoTest extends BaseTest{
	
	@Autowired
	SpTicketDao spTicketDao ;
	
	@Autowired
	SpTicketBatchProdDao spTicketBatchProdDao;
	
	@Test
	public void getListByUidAndStateCond(){
		int uid = 6;
		Map<String, Object[]> statMap = SpTicketState.STARED_UN_USED_UN_DATED.getSuitedQueryCondition();
		for (String s : statMap.keySet()) {
			System.out.println(String.format("key : %s, op : %s, rang : %s",  s, statMap.get(s)[0], statMap.get(s)[1]));
		}
		List<SpTicket> spTicketList = spTicketDao.getListByUidAndStateCond(uid, statMap);
		if (spTicketList != null && !spTicketList.isEmpty()) {
			for (SpTicket st : spTicketList) {
				System.out.println(st);
			}
		}
		
	}
	
	@Test
	public void getPageControlByUidAndStateCond(){
		int uid = 6;
		Map<String, Object[]> statMap = SpTicketState.STARED_UN_USED_UN_DATED.getSuitedQueryCondition();
		for (String s : statMap.keySet()) {
			System.out.println(String.format("key : %s, op : %s, rang : %s",  s, statMap.get(s)[0], statMap.get(s)[1]));
		}
		
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPagesize(10);
		pageInfo.setStartIndex(0);
		PagerControl<SpTicket> pageControl = spTicketDao.getPageByUidAndStateCond(uid, statMap, pageInfo);
		if (pageControl.getEntityList() != null && !pageControl.getEntityList().isEmpty()) {
			for (SpTicket st : pageControl.getEntityList()) {
				System.out.println(st);
			}
		}
	}
	
	@Test
	public void getCategoryListByBatchIds(){
		int[] batchIds = {3, 4, 5};
		int[] proIds  = {10000144, 10000172, 10000192};
		
		List<SpTicketBatchProd> spTicketList = spTicketBatchProdDao.getTicketProdsByProIdsAndBatchIds(proIds, batchIds);
		
		for (SpTicketBatchProd st : spTicketList) {
			System.out.println(st.toString());
		}
	}
	
	

}
