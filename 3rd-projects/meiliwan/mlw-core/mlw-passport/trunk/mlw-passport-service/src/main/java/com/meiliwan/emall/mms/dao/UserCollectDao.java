package com.meiliwan.emall.mms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.mms.bean.UserCollect;

/**
 * 用户收藏 dao接口
 */
public interface UserCollectDao  extends IDao<Integer, UserCollect> {
    /**
     * 通过商品id 更改商品名称
     *
     * @param entity 传入须要更新的实体对
     * @return
     */
    int updateProNameByProId(UserCollect entity);
}