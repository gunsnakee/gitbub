package com.meiliwan.emall.base.dao.impl;

import com.meiliwan.emall.base.bean.BaseInfoContent;
import com.meiliwan.emall.base.dao.BaseInfoContentDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * 资讯内容管理DAO实现
 * User: wuzixin
 * Date: 13-6-13
 * Time: 下午5:29
 */
@Repository
public class BaseInfoContentDaoImpl extends BaseDao<Integer,BaseInfoContent> implements BaseInfoContentDao{
    @Override
    public String getMapperNameSpace() {
        return BaseInfoContentDao.class.getName();
    }
}
