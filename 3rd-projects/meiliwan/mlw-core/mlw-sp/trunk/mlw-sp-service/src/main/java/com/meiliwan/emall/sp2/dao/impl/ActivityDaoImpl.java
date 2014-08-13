package com.meiliwan.emall.sp2.dao.impl;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.dto.CommentDTO;
import com.meiliwan.emall.sp2.bean.ActivityBean;
import com.meiliwan.emall.sp2.dao.ActivityDao;
import com.meiliwan.emall.sp2.dto.ActivityDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : yiyou.luo
 * Date: 13-12-24
 * Time: 上午11:00
 */
@Repository
public class ActivityDaoImpl extends BaseDao<Integer, ActivityBean> implements ActivityDao {
    @Override
    public String getMapperNameSpace() {
        // TODO Auto-generated method stub
        return ActivityDao.class.getName();
    }

    @Override
    public PagerControl<ActivityBean> getSpActivityPaperByActivityDTO(ActivityDTO activityDTO, PageInfo pageInfo) {
        PagerControl<ActivityBean> pagerControl = new PagerControl<ActivityBean>();
        pageInfo.startTime();
        if(StringUtils.isNotEmpty(activityDTO.getActName())) {
            activityDTO.setActName("%"+activityDTO.getActName()+"%");
        }

        List<ActivityBean> list = null;
        int total = 0;
        list = getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo + "ActivityDTO",
                getShardParam(null, getMapParamsActivityDTO(activityDTO, pageInfo, getOrderBySql(pageInfo)), false));
        total = getCountByActivityDTO(activityDTO);

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }
    private Map<String, Object> getMapParamsActivityDTO(ActivityDTO activityDTO, PageInfo pageInfo, String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != activityDTO) map.put("entity", activityDTO);
        if (null != pageInfo) map.put("pageInfo", pageInfo);
        if (null != orderBySql) map.put("orderBy", orderBySql);
        return map;
    }

    private int getCountByActivityDTO(ActivityDTO activityDTO) {
        Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity + "ActivityDTO",
                getShardParam(null, getMapParamsActivityDTO(activityDTO, null, null), false));
        return (Integer) selectOne;
    }

}
 