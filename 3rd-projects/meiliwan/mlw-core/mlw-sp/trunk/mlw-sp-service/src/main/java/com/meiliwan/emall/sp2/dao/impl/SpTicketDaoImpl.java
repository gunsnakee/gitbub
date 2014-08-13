package com.meiliwan.emall.sp2.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.SpTicket;
import com.meiliwan.emall.sp2.dao.SpTicketDao;

/**
 * User: wuzixin
 * Date: 14-5-28
 * Time: 下午4:27
 */
@Repository
public class SpTicketDaoImpl extends BaseDao<String, SpTicket> implements SpTicketDao {
    
	@Override
    public String getMapperNameSpace() {
        return SpTicketDao.class.getName();
    }
	

	@Override
	public PagerControl<SpTicket> getPageByUidAndStateCond(int uid,
			Map<String, Object[]> stateCond, PageInfo pageInfo) {
		PagerControl<SpTicket> pagerControl = new PagerControl<SpTicket>();
		if (stateCond == null || stateCond.isEmpty()) {
			stateCond = new HashMap<String, Object[]>();
		}
		stateCond.put("uid", new Object[]{"=", uid});
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stateCond", stateCond);
		map.put("pageInfo", pageInfo);
		
        pageInfo.startTime();
        List<SpTicket> list = null;
        int total = 0;
        try {
                list = getSqlSession().selectList(getMapperNameSpace() + ".getPageByUidAndStateCond",
                        getShardParam(null, map, false));
                total = getCountByUidAndStateCond( uid, stateCond);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getPageByUidAndStateCond: {},{}", stateCond.toString(), e);
        }

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
	}
	
	@Override
	 public int getCountByUidAndStateCond(int uid,
				Map<String, Object[]> stateCond) {
		 	if (stateCond == null || stateCond.isEmpty()) {
				stateCond = new HashMap<String, Object[]>();
			}
			stateCond.put("uid", new Object[]{"=", uid});
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("stateCond", stateCond);
			
	        try {

	            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".getCountByUidAndStateCond",
	                    getShardParam(null, map, false));
	            return (Integer) selectOne;
	        } catch (Exception e) {
	            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByUidAndStateCond: {},{}", new String[]{stateCond == null ? "" : stateCond.toString()}, e);
	        }
	 }



	@Override
	public List<SpTicket> getListByUidAndStateCond(final int uid,
			 Map<String, Object[]> stateCond) {
		List<SpTicket> list = null;
		if (stateCond == null || stateCond.isEmpty()) {
			stateCond = new HashMap<String, Object[]>();
		}
		stateCond.put("uid", new Object[]{"=", uid});
		Map<String, Map<String, Object[]>> map = new HashMap<String, Map<String, Object[]>>();
		map.put("stateCond", stateCond);
		try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getListByUidAndStateCond", map);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByUidAndStateCond: uid [" + uid + "] statCon: {}", stateCond.toString(), e);
        }
		
        if (list == null || list.isEmpty()) {
        	list = new ArrayList<SpTicket>();
        }
        return list;
	}
	
	@Override
	public SpTicket getEntityByPwd(String ticketPwd) {
		SpTicket spTicket = null;
        try {
        	spTicket = getSqlSession().selectOne(getMapperNameSpace() + ".selectByPwd", ticketPwd);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".selectByPwd: {}", ticketPwd, e);
        }
        return spTicket;
	}

    @Override
    public int updateActUrlByBatchId(int batchId, String actUrl) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("batchId",batchId);
        map.put("actUrl", actUrl);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateActUrlByBatchId", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateActUrlByBatchId: {},{}", new String[]{String.valueOf(batchId),actUrl}, e);
        }
    }


	@Override
	public int updateSpTicketState(int stateValue, Set<String> ticketIdset) {
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("state",stateValue);
        map.put("ticketIds", ticketIdset);
        
        try { 
            return getSqlSession().update(getMapperNameSpace() + ".updateSpTicketState", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateSpTicketState: {},{}", new String[]{String.valueOf(stateValue),ticketIdset.toString()}, e);
        }
	}
    
    

}
