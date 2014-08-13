package com.meiliwan.emall.mms.dao.impl;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.mms.bean.UserIntegral;
import com.meiliwan.emall.mms.bean.UserIntegralDetail;
import com.meiliwan.emall.mms.dao.UserIntegralDao;
import com.meiliwan.emall.mms.dao.UserIntegralDetailDao;
import com.meiliwan.emall.mms.dto.UserIntegralDetailDto;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserIntegralDetailDaoImpl extends BaseDao<Integer, UserIntegralDetail> implements
        UserIntegralDetailDao {

	@Override
	public String getMapperNameSpace() {
		return UserIntegralDetailDao.class.getName();
	}

    public PagerControl<UserIntegralDetail> getPagerByIntegralDetailDto(UserIntegralDetailDto integralDetailDto, PageInfo pageInfo) {

        PagerControl<UserIntegralDetail> pagerControl = new PagerControl<UserIntegralDetail>();
        pageInfo.startTime();
        List<UserIntegralDetail> list = null;
        int total = 0;
        list = getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo + "" ,
                getShardParam(null, getMapParamsIntegralDetailDto(integralDetailDto, pageInfo, getOrderBySql(pageInfo)), false));
        total = getCountByIntegralDetailDto(integralDetailDto);

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }

    private int getCountByIntegralDetailDto(UserIntegralDetailDto integralDetailDto) {
        Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity + "",
                getShardParam(null, getMapParamsIntegralDetailDto(integralDetailDto, null, null), false));
        return (Integer) selectOne;
    }

    private Map<String, Object> getMapParamsIntegralDetailDto(UserIntegralDetailDto entity, PageInfo pageInfo, String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != entity) map.put("entity", entity);
        if (null != pageInfo) map.put("pageInfo", pageInfo);
        if (null != orderBySql) map.put("orderBy", orderBySql);
        return map;
    }

	
}
