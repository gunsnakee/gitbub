package com.meiliwan.emall.pms.dao.impl;

import com.google.common.base.Strings;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProConsult;
import com.meiliwan.emall.pms.dao.ProConsultDao;
import com.meiliwan.emall.pms.dto.ConsultDTO;
import com.meiliwan.emall.pms.dto.QueryCountsDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: guangdetang
 * Date: 13-5-29
 * Time: 下午4:25
 */
@Repository
public class ProConsultDaoImpl extends BaseDao<Integer, ProConsult> implements ProConsultDao {
    @Override
    public String getMapperNameSpace() {
        return ProConsultDao.class.getName();
    }

    @Override
    public PagerControl<ProConsult> getPagerByConsultDTO(ConsultDTO commentView, PageInfo pageInfo) {
        PagerControl<ProConsult> pagerControl = new PagerControl<ProConsult>();
        pageInfo.startTime();
        List<ProConsult> list = null;
        int total = 0;
        list = getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo + "ConsultView",
                getShardParam(null, getMapParamsConsultDTO(commentView, pageInfo, getOrderBySql(pageInfo)), false));
        total = getCountByConsultDTO(commentView);

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }

    @Override
    public List<QueryCountsDTO> getConsultCountsByType(ProConsult entity, String whereSql, String orderBy) {
        try {
            return getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo + "QueryCountsDTO",
                    getShardParam(entity != null ? entity.getId() : null,
                            getMapParams(entity, null, whereSql,
                                    orderBy), false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByObj: {}", "");
        }
    }

    private int getCountByConsultDTO(ConsultDTO consult) {
        Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity + "ConsultView",
                getShardParam(null, getMapParamsConsultDTO(consult, null, null), false));
        return (Integer) selectOne;
    }

    private Map<String, Object> getMapParamsConsultDTO(ConsultDTO entity, PageInfo pageInfo, String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != entity) map.put("entity", entity);
        if (null != pageInfo) map.put("pageInfo", pageInfo);
        if (null != orderBySql) map.put("orderBy", orderBySql);
        return map;
    }
}
