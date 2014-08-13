package com.meiliwan.emall.monitor.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.monitor.bean.Player;
import com.meiliwan.emall.monitor.bean.PlayerRequest;
import com.meiliwan.emall.monitor.bean.PlayerRequestVO;
import com.meiliwan.emall.monitor.dao.PlayerRequestDao;

@Repository
public class PlayerRequestDaoImpl  extends BaseDao<Integer, PlayerRequest> implements PlayerRequestDao {

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return PlayerRequestDao.class.getName();
	}
	
	public PagerControl<PlayerRequestVO> getPagePlayerRequest(PlayerRequest entity, PageInfo pageInfo, String whereSql) {
        PagerControl<PlayerRequestVO> pagerControl = new PagerControl<PlayerRequestVO>();
        pageInfo.startTime();
        List<PlayerRequestVO> list = new ArrayList<PlayerRequestVO>();
        int total = 0;
        try {
            total = getCountByObj(entity, whereSql, false);
            if(total > 0){
                 list = getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo,
                         getShardParam(entity != null ? entity.getId() : null,
                                 getMapParams(entity, pageInfo, whereSql,
                                         pageInfo != null && !Strings.isNullOrEmpty(pageInfo.getOrderField())
                                                 ? getOrderBySql(pageInfo) : null), false));
            }
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getPagePlayer-" + e.getMessage() + ": {},{},{},{}", new String[]{entity == null ? "" : entity.toString(),
                    pageInfo.toString(), whereSql}, e);
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
	public List<Player> getAllPlayer() {
		List<Player> list = new ArrayList<Player>();
		list = getSqlSession().selectList(getMapperNameSpace() + ".getAllPlayer",
                getShardParam( null,
                        getMapParams(null, null, null,null),false));
		return list;
	}

}
