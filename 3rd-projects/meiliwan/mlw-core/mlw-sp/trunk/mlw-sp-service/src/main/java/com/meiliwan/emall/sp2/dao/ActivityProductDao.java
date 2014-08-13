package com.meiliwan.emall.sp2.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.sp2.bean.ActivityProductBean;

import java.util.Date;
import java.util.List;

/**
 * 活动商品 dao接口
 */
public interface ActivityProductDao extends IDao<Integer, ActivityProductBean> {

    /**
     * 根据商品ID查询商品-活动关联数据，只查询上架的活动
     * @param proId
     * @return
     */
    public List<ActivityProductBean> getAllByProIdForUpAct(int proId);

    /**
     * 根据商品ID集合查询商品-活动关联数据，只查询上架的活动
     * @param proIds
     * @return
     */
    public List<ActivityProductBean> getAllByProIdForUpAct(int[] proIds);

    /**
     * 根据商品ID查询商品-活动关联数据，查询上架的活动 & 完成上架但未开始的活动
     * @param proIds
     * @return
     */
    public List<ActivityProductBean> getAllByProIdActIsVaild(int[] proIds);

    /**
     * 查询某个时间端内参加活动的活动商品，查询上架 or 创建的活动
     * @param proId
     * @return
     */
    public List<ActivityProductBean> getAPByProIdAndTimes(int proId,Date newStartTime,Date newEndTime);

    /**
     *  通过进活动id 删除 活动商品
     * @param actId
     * @return
     */
    public Integer deleteByActId(Integer actId);

}