package com.meiliwan.emall.cms2.dao;

import com.meiliwan.emall.cms2.bean.ThematicPageModel;
import com.meiliwan.emall.core.db.IDao;

import java.util.List;

/**
 * User: wenlepeng
 * Date: 14-4-9
 * Time: 下午3:24
 */
public interface ThematicPageModelDao extends IDao<Integer, ThematicPageModel> {

    /**
     * 根据专题ID获取专题对应的区域列表
     *
     * @param pageId
     * @return
     */
    List<ThematicPageModel> getPMListByPageId(int pageId);

    List<ThematicPageModel> getPMListByPageIdAndState(int pageId);


    /**
     * 根据关系ID，删除专题页相关区域
     *
     * @param id
     * @return
     */
    int updatePMHide(int id);

    /**
     * 修改专题页与区域关系表的json数据
     *
     * @param id
     * @param json
     * @return
     */
    int updatePageModelJsonData(int id, String json);
}
