package com.meiliwan.emall.sp2.dao;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.sp2.bean.ActivityBean;
import com.meiliwan.emall.sp2.dto.ActivityDTO;

public interface ActivityDao extends IDao<Integer, ActivityBean> {
    /**
     * 通过活动查询DTO 实体参数获取对应的实体列表包含物理分页
     * @param activityDTO
     * @param pageInfo
     * @return
     */
    PagerControl<ActivityBean> getSpActivityPaperByActivityDTO(ActivityDTO activityDTO, PageInfo pageInfo);
}