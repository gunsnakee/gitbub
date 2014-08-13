package com.meiliwan.emall.sp2.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.sp2.bean.SpTicket;

public interface SpTicketDao extends IDao<String,SpTicket>{
	
	/**
	 * @param uid
	 * @param stateCond
	 * @param pageInfo
	 * @return
	 */
	public PagerControl<SpTicket> getPageByUidAndStateCond(int uid, Map<String, Object[]> stateCond, PageInfo pageInfo) ;
	
	
	public int getCountByUidAndStateCond(int uid, Map<String, Object[]> stateCond) ;
	
	public List<SpTicket> getListByUidAndStateCond(int uid, Map<String, Object[]> stateCond);
	
    /**
     * 修改关联活动URL
     * @param batchId
     * @param actUrl
     * @return
     */
    int updateActUrlByBatchId(int batchId, String actUrl);
	
	
	public SpTicket getEntityByPwd(String ticketPwd);
	
	
	public int updateSpTicketState(int stateValue, Set<String> ticketIdset);
}